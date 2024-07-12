package jira6.fate.global.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jira6.fate.global.exception.ErrorCode;
import jira6.fate.global.jwt.JwtProvider;
import jira6.fate.global.security.UserDetailsServiceImpl;
import jira6.fate.global.security.dto.SecurityErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtProvider.getAccessTokenFromHeader(request);

        if (StringUtils.hasText(accessToken)) {

            boolean isValidToken = tokenValidation(response, accessToken);

            if (!isValidToken) {
                return;
            }

        }

        filterChain.doFilter(request, response);

    }

    public void setAuthentication(String username) {

        Authentication authentication = createAuthentication(username);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

    }

    private boolean tokenValidation(HttpServletResponse response, String token) throws IOException {
        try {
            Claims userInfo = jwtProvider.getClaimsFromToken(token);
            setAuthentication(userInfo.getSubject());

            return true;
        } catch (ExpiredJwtException e) {
            SecurityErrorResponse securityErrorResponse = new SecurityErrorResponse();
            securityErrorResponse.sendResponse(response, ErrorCode.TOKEN_EXPIRED);

            return false;
        } catch (JwtException e) {
            SecurityErrorResponse securityErrorResponse = new SecurityErrorResponse();
            securityErrorResponse.sendResponse(response, ErrorCode.INVALID_TOKEN);

            return false;
        }
    }

    private Authentication createAuthentication(String username) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
