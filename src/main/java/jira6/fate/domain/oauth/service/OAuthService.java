package jira6.fate.domain.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jira6.fate.domain.oauth.dto.KakaoDto;
import jira6.fate.domain.user.entity.User;
import jira6.fate.domain.user.entity.UserRole;
import jira6.fate.domain.user.entity.UserStatus;
import jira6.fate.domain.user.repository.UserRepository;
import jira6.fate.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtProvider jwtProvider;

    @Transactional
    public HttpHeaders kakaoLogin(String code, String CLIENT_ID) throws JsonProcessingException {

        String token = getToken(code, CLIENT_ID);
        KakaoDto kakaoDto = getKakaoUserInfo(token);
        User kakaoUser = registerKakaoUserIfNeeded(kakaoDto);

        String accessToken = jwtProvider.generateAccessToken(kakaoUser.getUserName(), kakaoUser.getUserRole().toString());
        String refreshToken = jwtProvider.generateRefreshToken(kakaoUser.getUserName(), kakaoUser.getUserRole().toString());
        ResponseCookie responseCookie = jwtProvider.createRefreshTokenCookie(refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());

        kakaoUser.updateRefreshToken(refreshToken);
        userRepository.save(kakaoUser);

        return headers;
    }

    private String getToken(String code, String CLIENT_ID) throws JsonProcessingException {

        URI uri = UriComponentsBuilder.fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("redirect_uri", "http://localhost:3000/auth");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity.post(uri)
                .headers(headers)
                .body(body);
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

        return jsonNode.get("access_token").asText();
    }

    private KakaoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {

        URI uri = UriComponentsBuilder.fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity.post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();

        return KakaoDto.builder().id(id).nickname(nickname).build();
    }

    private User registerKakaoUserIfNeeded(KakaoDto kakaoDto) {

        User kakaoUser = userRepository.findByKakaoId(kakaoDto.getId()).orElse(null);

        if (kakaoUser == null) {

            String password = UUID.randomUUID().toString();
            String encryptedPassword = passwordEncoder.encode(password);
            kakaoUser = User.builder()
                    .userName(kakaoDto.getId() + kakaoDto.getNickname())
                    .password(encryptedPassword)
                    .userStatus(UserStatus.ACTIVE)
                    .userRole(UserRole.USER)
                    .kakaoId(kakaoDto.getId())
                    .build();

        }

        return kakaoUser;
    }

}
