package jira6.fate.domain.card.service;

import jakarta.transaction.Transactional;
import jira6.fate.domain.card.dto.CardCreateRequestDto;
import jira6.fate.domain.card.entity.Card;
import jira6.fate.domain.card.repository.CardRepository;
import jira6.fate.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static jira6.fate.global.exception.ErrorCode.COLUMN_NOT_FOUND;

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

    public Columns existsByColumn(Long columnId) {
        return columnRepository.findById(columnId).orElseThrow(COLUMN_NOT_FOUND);
    }

}
