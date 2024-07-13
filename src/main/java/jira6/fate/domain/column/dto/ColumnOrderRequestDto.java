package jira6.fate.domain.column.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ColumnOrderRequestDto {
  private Long columnId;
  private Long columnOrder;
}
