package jira6.fate.global.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateAccessToken(String username, String role) {
        Date expirationDate = createExpirationDate(accessTokenExpiration);

        return generateToken(username, role, expirationDate);
    }

    public String generateRefreshToken(String username, String role) {
        Date expirationDate = createExpirationDate(refreshTokenExpiration);

        return generateToken(username, role, expirationDate);
    }

    private String generateToken(String username, String role, Date expirationDate) {
        return Jwts.builder()
                .setSubject(username)
                .claim("auth", role)
                .setExpiration(expirationDate)
                .setIssuedAt(new Date())
                .signWith(key, signatureAlgorithm)
                .compact();

    }

    private Date createExpirationDate(Long ms) {
        Date date = new Date();

        return new Date(date.getTime() + ms);
    }

}
