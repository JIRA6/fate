package jira6.fate.domain.card.dto;

import jakarta.validation.constraints.Min;

import lombok.Getter;

@Getter
public class CardOrderRequestDto {

    @Min(1)
    private Long cardId;

    @Min(1)
    private Long cardOrder;

}
