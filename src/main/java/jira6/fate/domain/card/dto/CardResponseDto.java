package jira6.fate.domain.card.dto;

import java.time.LocalDate;
import jira6.fate.domain.card.entity.Card;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CardResponseDto {
    private Long cardId;
    private Long cardOrder;
    private String cardTitle;
    private LocalDate deadlineAt;

    @Builder
    public CardResponseDto(Long cardId, Long cardOrder, String cardTitle, LocalDate deadlineAt) {
        this.cardId = cardId;
        this.cardOrder = cardOrder;
        this.cardTitle = cardTitle;
        this.deadlineAt = deadlineAt;
    }

    public CardResponseDto(Card card) {
        this.cardId = card.getId();
        this.cardOrder = card.getCardOrder();
        this.cardTitle = card.getCardTitle();
        this.deadlineAt = card.getDeadlineAt();
    }
}
