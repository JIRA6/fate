package jira6.fate.domain.oauth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jira6.fate.domain.oauth.service.OAuthService;
import jira6.fate.global.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OAuthController {

    @Value("${kakao.rest.api.key}")
    private String CLIENT_ID;

    private final OAuthService oAuthService;

    @GetMapping("/kakao/callback")
    public ResponseEntity<MessageResponse> kakaoLogin(@RequestParam String code) throws IOException {

        HttpHeaders headers = oAuthService.kakaoLogin(code, CLIENT_ID);

        MessageResponse messageResponse = MessageResponse.builder()
                .statusCode(200)
                .message("카카오 로그인 성공")
                .build();

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(messageResponse);
    }

}
