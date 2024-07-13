package jira6.fate.domain.card.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CardDetailResponseDto {
    private String cardTitle;
    private String cardContents;
    private String managerName;
    private LocalDate deadlineAt;
    private String columnName;

    @Builder
    public CardDetailResponseDto(String cardTitle, String cardContents, String managerName, LocalDate deadlineAt, String columnName) {
        this.cardTitle = cardTitle;
        this.cardContents = cardContents;
        this.managerName = managerName;
        this.deadlineAt = deadlineAt;
        this.columnName = columnName;
    }
}
