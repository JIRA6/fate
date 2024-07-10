package jira6.fate.domain.board.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardResponseDto {

    private Long id;
    private Long userid;
    private LocalDateTime createdAt;
    private String title;
    private String intro;
}
