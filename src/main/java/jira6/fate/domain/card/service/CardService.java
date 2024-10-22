package jira6.fate.domain.card.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import jira6.fate.domain.board.entity.Board;
import jira6.fate.domain.board.entity.Team;
import jira6.fate.domain.board.repository.BoardRepository;
import jira6.fate.domain.board.repository.TeamRepository;
import jira6.fate.domain.card.dto.CardCreateRequestDto;
import jira6.fate.domain.card.dto.CardDetailResponseDto;
import jira6.fate.domain.card.dto.CardListResponseDto;
import jira6.fate.domain.card.dto.CardOrderListRequestDto;
import jira6.fate.domain.card.dto.CardOrderRequestDto;
import jira6.fate.domain.card.dto.CardResponseDto;
import jira6.fate.domain.card.dto.CardUpdateRequestDto;
import jira6.fate.domain.card.entity.Card;
import jira6.fate.domain.card.repository.CardRepository;
import jira6.fate.domain.column.entity.Columns;
import jira6.fate.domain.column.repository.ColumnRepository;
import jira6.fate.domain.user.entity.User;
import jira6.fate.global.exception.CustomException;
import jira6.fate.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final BoardRepository boardRepository;
    private final ColumnRepository columnRepository;
    private final CardRepository cardRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public void createCard(Long columnId, CardCreateRequestDto requestDto, User user) {
        Columns column = findColumn(columnId);
        Team team = findTeam(requestDto.getTeamId());

        Card card = Card.builder()
            .cardTitle(requestDto.getCardTitle())
            .cardContents(requestDto.getCardContents())
            .cardOrder(requestDto.getCardOrder())
            .deadlineAt(requestDto.getDeadlineAt())
            .column(column)
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

    public Board findBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
            () -> new CustomException(ErrorCode.BOARD_NOT_FOUND)
        );
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

    @Transactional(readOnly = true)
    public List<CardResponseDto> getAllCardByColumn(Long columnId) {
        findColumn(columnId);

        List<Card> cards = cardRepository.findByColumnId(columnId);
        return cards.stream()
            .map(CardResponseDto::fromCard)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CardListResponseDto<List<CardResponseDto>>> getAllCardByTeam(Long teamId) {
        findTeam(teamId);

        List<Card> cards = cardRepository.findByTeamId(teamId);

        return groupAndMapToResponse(cards);
    }

    @Transactional(readOnly = true)
    public List<CardListResponseDto<List<CardResponseDto>>> getAllCard(Long boardId) {
        findBoard(boardId);

        List<Card> cards = StreamSupport.stream(cardRepository.findAll().spliterator(), false).toList();

        return groupAndMapToResponse(cards);
    }

    private List<CardListResponseDto<List<CardResponseDto>>> groupAndMapToResponse(List<Card> cards) {
        // 그룹화하여 컬럼별로 카드 목록을 나눔
        Map<Columns, List<CardResponseDto>> groupedByColumns = cards.stream()
            .collect(Collectors.groupingBy(
                Card::getColumn,
                Collectors.mapping(
                    card -> CardResponseDto.builder()
                        .cardId(card.getId())
                        .cardOrder(card.getCardOrder())
                        .cardTitle(card.getCardTitle())
                        .deadlineAt(card.getDeadlineAt())
                        .build(),
                    Collectors.toList()
                )
            ));

        return groupedByColumns.entrySet()
            .stream()
            .map(entry -> CardListResponseDto.<List<CardResponseDto>>builder()
                .columnId(entry.getKey().getId())
                .columnName(entry.getKey().getColumnName())
                .cardData(entry.getValue())
                .build())
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateCardOrder(Long columnId, CardOrderListRequestDto requestDto) {
        findColumn(columnId);

        List<CardOrderRequestDto> cardOrders = requestDto.getOrderData();

        for (CardOrderRequestDto cardOrder : cardOrders) {
            Long cardId = cardOrder.getCardId();
            Long order = cardOrder.getCardOrder();

            Card card = cardRepository.findById(cardId).orElseThrow(
                () -> new CustomException(ErrorCode.CARD_NOT_FOUND)
            );

            card.updateCardOrder(order);

            cardRepository.save(card); // 순서 업데이트 저장
        }
    }
}
