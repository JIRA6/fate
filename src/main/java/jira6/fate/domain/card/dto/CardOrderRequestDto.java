package jira6.fate.domain.card.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CardOrderRequestDto {

    private Long cardId;

    private Long cardOrder;

}
