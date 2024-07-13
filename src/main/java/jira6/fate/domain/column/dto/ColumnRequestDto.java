package jira6.fate.domain.column.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ColumnRequestDto {

  private String columnName;
  private Long columnOrder;

}
