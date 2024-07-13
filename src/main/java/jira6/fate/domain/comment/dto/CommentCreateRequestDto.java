package jira6.fate.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentCreateRequestDto {

    @NotBlank
    private String commentContents;

}
