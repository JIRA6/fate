package jira6.fate.domain.user.service;

import jira6.fate.domain.user.dto.UserSignupRequestDto;
import jira6.fate.domain.user.entity.User;
import jira6.fate.domain.user.entity.UserRole;
import jira6.fate.domain.user.entity.UserStatus;
import jira6.fate.domain.user.repository.UserRepository;
import jira6.fate.global.exception.CustomException;
import jira6.fate.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    private void checkPassword(String inputPassword, String password) {
        if (!passwordEncoder.matches(inputPassword, password)) {
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }
    }

}
