package jira6.fate.domain.card.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CardUpdateRequestDto {

    @NotBlank
    private String cardTitle;

    private String cardContents;

    private LocalDate deadlineAt;

    private Long teamId;

}
