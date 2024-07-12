package jira6.fate.domain.user.controller;

import jakarta.validation.Valid;
import jira6.fate.domain.user.dto.UserSignupRequestDto;
import jira6.fate.domain.user.service.UserService;
import jira6.fate.global.dto.MessageResponse;
import jira6.fate.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<MessageResponse> signup(@Valid @RequestBody UserSignupRequestDto requestDto) {

        userService.signup(requestDto);

        MessageResponse response = MessageResponse.builder()
                .statusCode(201)
                .message("회원가입 성공")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/users/logout")
    public ResponseEntity<MessageResponse> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        userService.logout(userDetails.getUser().getId());

        MessageResponse response = MessageResponse.builder()
                .statusCode(200)
                .message("로그아웃 성공")
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
