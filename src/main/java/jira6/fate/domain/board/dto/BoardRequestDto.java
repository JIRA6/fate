package jira6.fate.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDto {

    private String title;
    private String intro;

    @Builder
    public BoardRequestDto(String title, String intro) {
        this.title = title;
        this.intro = intro;
    }
}
