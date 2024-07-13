package jira6.fate.domain.user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jira6.fate.domain.user.dto.UserSignupRequestDto;
import jira6.fate.domain.user.entity.User;
import jira6.fate.domain.user.entity.UserRole;
import jira6.fate.domain.user.entity.UserStatus;
import jira6.fate.domain.user.repository.UserRepository;
import jira6.fate.global.exception.CustomException;
import jira6.fate.global.exception.ErrorCode;
import jira6.fate.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Value("${manager.password}")
    private String managerPassword;

    @Transactional
    public void signup(UserSignupRequestDto requestDto) {

        if (userRepository.existsByUserName(requestDto.getUsername())) {
            throw new CustomException(ErrorCode.USER_NOT_UNIQUE);
        }

        String encryptedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = User.builder()
                .userName(requestDto.getUsername())
                .password(encryptedPassword)
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.USER)
                .build();

        if (StringUtils.hasText(requestDto.getManagerPassword())) {
            checkPassword(requestDto.getManagerPassword(), managerPassword);
            user.updateRoleToManager();
        }

        userRepository.save(user);

    }

    @Transactional
    public void logout(Long id) {

        User user = userRepository.findById(id).orElseThrow( () -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.updateRefreshToken(null);

    }

    @Transactional
    public void withdrawal(Long id) {

        User user = userRepository.findById(id).orElseThrow( () -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.updateRefreshToken(null);
        user.setStatusToLeave();

    }

    @Transactional
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = jwtProvider.getRefreshTokenFromHeader(request);

        if (!StringUtils.hasText(refreshToken)) {
            throw new CustomException(ErrorCode.TOKEN_MISMATCH);
        }

        try {

            Claims userInfo = jwtProvider.getClaimsFromToken(refreshToken);
            Date expirationDate = userInfo.getExpiration();
            User user = userRepository.findByUserName(userInfo.getSubject()).orElseThrow( () -> new CustomException(ErrorCode.USER_NOT_FOUND));

            if (!user.getRefreshToken().equals(refreshToken)) {
                throw new CustomException(ErrorCode.TOKEN_MISMATCH);
            }

            String newAccessToken = jwtProvider.generateAccessToken(user.getUserName(), user.getUserRole().toString());
            String newRefreshToken = jwtProvider.generateToken(user.getUserName(), user.getUserRole().toString(), expirationDate);
            ResponseCookie responseCookie = jwtProvider.createRefreshTokenCookie(newRefreshToken);
            jwtProvider.addAccessTokenHeader(response, newAccessToken);
            jwtProvider.addRefreshTokenCookie(response, responseCookie.toString());

            user.updateRefreshToken(newRefreshToken);

        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

    }

    private void checkPassword(String inputPassword, String password) {
        if (!passwordEncoder.matches(inputPassword, password)) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }
    }

}
