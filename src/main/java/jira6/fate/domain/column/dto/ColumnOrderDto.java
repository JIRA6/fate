package jira6.fate.domain.column.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ColumnOrderDto {
  private Long columnId;
  private Long columnOrder;
}
