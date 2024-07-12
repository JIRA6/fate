package jira6.fate.domain.column.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ColumnRequestDto {
  private String columnName;
  private Long boardId;
}
