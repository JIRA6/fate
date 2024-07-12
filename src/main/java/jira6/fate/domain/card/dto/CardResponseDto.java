package jira6.fate.domain.card.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CardResponseDto {
    private Long cardId;
    private String cardTitle;
    private LocalDate deadlineAt;

    @Builder
    public CardResponseDto(Long cardId, String cardTitle, LocalDate deadlineAt) {
        this.cardId = cardId;
        this.cardTitle = cardTitle;
        this.deadlineAt = deadlineAt;
    }

}
