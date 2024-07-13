package jira6.fate.domain.comment.dto;

import jira6.fate.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentAllResponseDto {

    private String commentContents;

    @Builder
    public CommentAllResponseDto(Comment comment) {
        this.commentContents = comment.getCommentContents();
    }
}
