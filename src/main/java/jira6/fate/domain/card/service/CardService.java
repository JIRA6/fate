package jira6.fate.domain.card.service;

import jira6.fate.domain.card.dto.CardCreateRequestDto;
import jira6.fate.domain.card.dto.CardDetailResponseDto;
import jira6.fate.domain.card.dto.CardUpdateRequestDto;
import jira6.fate.domain.card.entity.Card;
import jira6.fate.domain.card.repository.CardRepository;
import jira6.fate.domain.user.entity.User;
import jira6.fate.domain.user.repository.UserRepository;
import jira6.fate.global.exception.CustomException;
import jira6.fate.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Columns;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final ColumnsRepository columnsRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public void createCard(Long columnId, CardCreateRequestDto requestDto, User user) {
        Columns columns = findColumn(columnId);
        Team team = findTeam(requestDto.getTeamId());

        Card card = Card.builder()
            .cardTitle(requestDto.getCardTitle())
            .cardContents(requestDto.getCardContents())
            .cardOrder(requestDto.getCardOrder())
            .deadlineAt(requestDto.getDeadlineAt())
            .columns(columns)
            .user(user)
            .team(team)
            .build();

        cardRepository.save(card);
    }

    @Transactional(readOnly = true)
    public CardDetailResponseDto getCard(Long columnId, Long cardId) {
        Columns columns = findColumn(columnId);
        Card card = findCard(cardId);

        String managerName = card.getTeam().getUser().getUserName();

        return CardDetailResponseDto.builder()
            .columnName(card.getCardTitle())
            .cardContents(card.getCardContents())
            .managerName(managerName)
            .deadlineAt(card.getDeadlineAt())
            .columnName(columns.getColumnName())
            .build();
    }

    @Transactional
    public void updateCard(Long columnId, Long cardId, CardUpdateRequestDto requestDto, User user) {
        Columns columns = findColumn(columnId);
        Card card = findCard(cardId);
        Team team = findTeam(requestDto.getTeamId());

        String cardCreatorName = card.getUser().getUserName();
        String currentUserName = user.getUserName();

        if (!checkCardCreator(cardCreatorName, currentUserName)) {
            throw new CustomException(ErrorCode.NOT_UNAUTHORIZED);
        }

        card.update(
            requestDto.getCardTitle(),
            requestDto.getCardContents(),
            requestDto.getDeadlineAt(),
            columns,
            team
        );
    }

    @Transactional
    public void deleteCard(Long columnId, Long cardId, User user) {
        findColumn(columnId);
        Card card = findCard(cardId);

        String cardCreatorName = card.getUser().getUserName();
        String currentUserName = user.getUserName();

        if (!checkCardCreator(cardCreatorName, currentUserName)) {
            throw new CustomException(ErrorCode.NOT_UNAUTHORIZED);
        }

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

    public Team findTeam(Long teamId) {
        return teamRepository.findById(teamId).orElseThrow(
            () -> new CustomException(ErrorCode.USER_NOT_TEAM)
        );
    }

    public Boolean checkCardCreator(String cardCreatorName, String currentUserName) {
        return cardCreatorName.equals(currentUserName);
    }

}
