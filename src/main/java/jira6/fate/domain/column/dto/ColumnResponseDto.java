package jira6.fate.domain.column.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ColumnResponseDto {
  private Long id;
  private String columnName;
  private Long boardId;

  @Builder
  public ColumnResponseDto(Long id, String columnName, Long boardId) {
    this.id = id;
    this.columnName= columnName;
    this.boardId = boardId;
  }

}
