package jira6.fate.domain.column.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ColumnResponseDto {
  private Long id;
  private String columnName;

  @Builder
  public ColumnResponseDto(Long id, String columnName) {
    this.id = id;
    this.columnName= columnName;
  }

}
