package jira6.fate.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserSignupRequestDto {

    @NotBlank(message = "아이디를 입력해주세요")
    @Size(min = 4, max = 15, message = "아이디는 4 ~ 15자 입니다")
    @Pattern(regexp = "^[a-z0-9]+$", message = "아이디 형식을 확인해주세요")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 4, max = 20, message = "비밀번호는 4 ~ 20자 입니다")
    @Pattern(regexp = "^[A-Za-z0-9!@#$%^&*]+$", message = "비밀번호 형식을 확인해주세요")
    private String password;

    private String managerPassword;

}
