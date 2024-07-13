package jira6.fate.domain.comment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import jira6.fate.domain.comment.dto.CommentAllResponseDto;
import jira6.fate.domain.comment.dto.CommentCreateRequestDto;
import jira6.fate.domain.comment.service.CommentService;
import jira6.fate.global.dto.DataResponse;
import jira6.fate.global.dto.MessageResponse;
import jira6.fate.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/cards/{cardId}/comments")
    public ResponseEntity<MessageResponse> createComment(
        @Min(1) @PathVariable Long cardId,
        @Valid @RequestBody CommentCreateRequestDto commentCreateRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        commentService.createComment(cardId, commentCreateRequestDto, userDetails.getUser());
        MessageResponse response = MessageResponse.builder()
            .statusCode(201)
            .message("카드 댓글 작성 성공")
            .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/cards/{cardId}/comments")
    public ResponseEntity<DataResponse<List<CommentAllResponseDto>>> getAllComment(
        @Min(1) @PathVariable Long cardId
    ) {
        List<CommentAllResponseDto> responseDto = commentService.getAllComment(cardId);
        DataResponse<List<CommentAllResponseDto>> response = DataResponse.<List<CommentAllResponseDto>>builder()
            .statusCode(200)
            .message("카드 댓글 조회 성공")
            .data(responseDto)
            .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
