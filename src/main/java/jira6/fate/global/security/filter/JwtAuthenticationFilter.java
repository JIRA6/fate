package jira6.fate.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jira6.fate.domain.user.dto.UserLoginRequestDto;
import jira6.fate.domain.user.entity.User;
import jira6.fate.domain.user.entity.UserStatus;
import jira6.fate.domain.user.repository.UserRepository;
import jira6.fate.global.dto.MessageResponse;
import jira6.fate.global.exception.ErrorCode;
import jira6.fate.global.jwt.JwtProvider;
import jira6.fate.global.security.UserDetailsImpl;
import jira6.fate.global.security.dto.SecurityErrorResponse;
import jira6.fate.global.security.dto.SecurityMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        setFilterProcessesUrl("/api/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {

            SecurityErrorResponse securityErrorResponse = new SecurityErrorResponse();

            try {
                securityErrorResponse.sendResponse(response, ErrorCode.INVALID_URL_ACCESS);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return null;
        }

        try {

            UserLoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequestDto.class);

            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword(), null));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        String username = userDetails.getUsername();
        String userRole = userDetails.getAuthorities().iterator().next().getAuthority();

        User user = userRepository.findByUserName(username).orElseThrow( () -> new UsernameNotFoundException("아이디, 비밀번호를 확인해주세요."));

        if (user.getUserStatus().equals(UserStatus.LEAVE)) {

            SecurityErrorResponse securityErrorResponse = new SecurityErrorResponse();
            securityErrorResponse.sendResponse(response, ErrorCode.CHECK_USERNAME_PASSWORD);

            return;
        }

        String accessToken = jwtProvider.generateAccessToken(username, userRole);
        String refreshToken = jwtProvider.generateRefreshToken(username, userRole);
        ResponseCookie responseCookie = jwtProvider.createRefreshTokenCookie(refreshToken);
        jwtProvider.addAccessTokenHeader(response, accessToken);
        jwtProvider.addRefreshTokenCookie(response, responseCookie.toString());

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        MessageResponse messageResponse = MessageResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message(user.getUserRole().toString() + " 로그인 성공")
                .build();
        SecurityMessageResponse securityMessageResponse = new SecurityMessageResponse();
        securityMessageResponse.sendResponse(response, messageResponse);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {

        SecurityErrorResponse securityErrorResponse = new SecurityErrorResponse();
        securityErrorResponse.sendResponse(response, ErrorCode.CHECK_USERNAME_PASSWORD);

    }

}
