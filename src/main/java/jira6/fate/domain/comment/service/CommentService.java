package jira6.fate.domain.comment.service;

import java.util.List;
import jira6.fate.domain.card.entity.Card;
import jira6.fate.domain.comment.dto.CommentAllResponseDto;
import jira6.fate.domain.comment.dto.CommentCreateRequestDto;
import jira6.fate.domain.comment.entity.Comment;
import jira6.fate.domain.comment.repository.CommentRepository;
import jira6.fate.domain.user.entity.User;
import jira6.fate.global.exception.CustomException;
import jira6.fate.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    public void createComment(Long cardId, CommentCreateRequestDto commentCreateRequestDto,
        User user) {
        Card card = findCard(cardId);

        Comment comment = Comment.builder()
            .commentContents(commentCreateRequestDto.getCommentContents())
            .user(user)
            .card(card)
            .build();

        commentRepository.save(comment);
    }

    public List<CommentAllResponseDto> getAllComment(Long cardId) {
        Card card = findCard(cardId);

        return commentRepository.findAllByCardId(cardId).stream().map(CommentAllResponseDto::new)
            .toList();
    }

    public Card findCard(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(
            () -> new CustomException(ErrorCode.CARD_NOT_FOUND)
        );
    }

}
