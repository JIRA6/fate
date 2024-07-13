package jira6.fate.domain.card.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class CardCreateRequestDto {

    @NotBlank
    private String cardTitle;

    private String cardContents;

    @Min(1)
    private Long cardOrder;

    private LocalDate deadlineAt;

    @Min(1)
    private Long teamId;

}
