package jira6.fate.domain.card.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CardOrderRequestDto {

    @NotBlank
    private Long cardId;

    @NotBlank
    private Long cardOrder;

}
