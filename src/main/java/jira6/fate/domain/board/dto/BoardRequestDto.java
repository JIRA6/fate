package jira6.fate.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String intro;

    @Builder
    public BoardRequestDto(String title, String intro) {
        this.title = title;
        this.intro = intro;
    }
}
