package jira6.fate.domain.card.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CardListResponseDto<T> {
    private Long columnId;
    private String columnName;
    private List<T> cardData;

    public CardListResponseDto(Long columnId, String columnName, List<T> cardData) {
        this.columnId = columnId;
        this.columnName = columnName;
        this.cardData = cardData;
    }

}
