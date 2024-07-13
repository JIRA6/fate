package jira6.fate.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret_key}")
    private String secretKey;

    @Value("${jwt.expiration.access_token}")
    private Long accessTokenExpiration;

    @Value("${jwt.expiration.refresh_token}")
    private Long refreshTokenExpiration;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(String username, String role, Date expirationDate) {
        return Jwts.builder()
                .setSubject(username)
                .claim("auth", role)
                .setExpiration(expirationDate)
                .setIssuedAt(new Date())
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String generateAccessToken(String username, String role) {
        Date expirationDate = createExpirationDate(accessTokenExpiration);

        return generateToken(username, role, expirationDate);
    }

    public String generateRefreshToken(String username, String role) {
        Date expirationDate = createExpirationDate(refreshTokenExpiration);

        return generateToken(username, role, expirationDate);
    }

    public String getAccessTokenFromHeader(HttpServletRequest request) {

        String authorization = request.getHeader(AUTHORIZATION_HEADER);

        if (!StringUtils.hasText(authorization) || !authorization.startsWith(BEARER_PREFIX)) {
            return null;
        }

        return substringBearer(authorization);
    }

    public String getRefreshTokenFromHeader(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie: cookies) {
            if ("RefreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("RefreshToken", refreshToken)
                .httpOnly(true)
                .maxAge(refreshTokenExpiration / 1000)
                .path("/")
                .sameSite("None")
                .build();
    }

    public void addAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken);
    }

    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        response.addHeader(HttpHeaders.SET_COOKIE, refreshToken);
    }

    public Claims getClaimsFromToken(String token) throws ExpiredJwtException, JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date createExpirationDate(Long ms) {
        Date date = new Date();

        return new Date(date.getTime() + ms);
    }

    private String substringBearer(String authorization) {
        return authorization.substring(7);
    }

}
