package jira6.fate.domain.board.dto;

import java.time.LocalDateTime;
import jira6.fate.domain.board.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardResponseDto {

    private Long id;
    private String title;
    private String intro;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public BoardResponseDto(Board board) {
        this.title = board.getTitle();
        this.intro = board.getIntro();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
    }
}
