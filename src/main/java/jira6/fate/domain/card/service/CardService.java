package jira6.fate.domain.card.service;

import static jira6.fate.global.exception.ErrorCode.COLUMN_NOT_FOUND;

import jakarta.transaction.Transactional;
import jira6.fate.domain.card.dto.CardCreateRequestDto;
import jira6.fate.domain.card.dto.CardUpdateRequestDto;
import jira6.fate.domain.card.entity.Card;
import jira6.fate.domain.card.repository.CardRepository;
import jira6.fate.domain.user.entity.User;
import jira6.fate.global.exception.CustomException;
import jira6.fate.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Columns;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final ColumnRepository columnRepository;

    @Transactional
    public void createCard(Long columnId, CardCreateRequestDto requestDto, User user) {
        Columns column = existsByColumn(columnId);

        Card card = Card.builder()
            .cardTitle(requestDto.getCardTitle())
            .cardContents(requestDto.getCardContents())
            .cardOrder(requestDto.getCardOrder())
            .managerName(requestDto.getManagerName())
            .deadlineAt(requestDto.getDeadlineAt())
            .column(columns)
            .user(user)
            .build();

        cardRepository.save(card);
    }

    @Transactional
    public void updateCard(Long columnId, Long cardId, CardUpdateRequestDto requestDto, User user) {
        Columns column = findColumn(columnId);
        Card card = findCard(cardId);

        String cardCreatorName = card.getUser().getUserName();
        String currentUserName = user.getUserName();

        if (!checkCardCreator(cardCreatorName, currentUserName)) {
            throw new CustomException(ErrorCode.NOT_UNAUTHORIZED);
        }

        card.update(
            requestDto.getCardTitle(),
            requestDto.getCardContents(),
            requestDto.getManagerName(),
            requestDto.getDeadlineAt(),
            column
        );
    }

    @Transactional
    public void deleteCard(Long columnId, Long cardId, User user) {
        findColumn(columnId);
        Card card = findCard(cardId);

        cardRepository.delete(card);
    }

    public Columns findColumn(Long columnId) {
        return columnRepository.findById(columnId).orElseThrow(
            () -> new CustomException(ErrorCode.COLUMN_NOT_FOUND)
        );
    }

    public Card findCard(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(
            () -> new CustomException(ErrorCode.CARD_NOT_FOUND)
        );
    }

    public Boolean checkCardCreator(String cardCreatorName, String currentUserName) {
        return cardCreatorName.equals(currentUserName);
    }

}
