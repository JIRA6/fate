package jira6.fate.domain.card.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CardCreateRequestDto {

    @NotBlank
    private String cardTitle;

    private String cardContents;

    @NotBlank
    private Long cardOrder;

    private String managerName;

    private LocalDateTime deadlineAt;

}
