package jira6.fate.domain.oauth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KakaoDto {

    private Long id;
    private String nickname;

    @Builder
    public KakaoDto(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

}
