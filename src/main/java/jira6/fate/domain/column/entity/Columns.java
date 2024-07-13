package jira6.fate.domain.column.entity;

import jakarta.persistence.*;
import jira6.fate.domain.board.entity.Board;
import jira6.fate.global.entity.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "table_column")
@Getter
@NoArgsConstructor
public class Columns extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String columnName;

  @Column(nullable = false)
  private Long columnOrder;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "board_id", nullable = false)
  private Board board;

  @Builder
  public Columns(String columnName, Long columnOrder, Board board) {
    this.columnName = columnName;
    this.columnOrder = columnOrder;
    this.board = board;
  }

  public void update(String columnName) {
    this.columnName = columnName;
  }

  public void updateColumnOrder(Long columnOrder) {
    this.columnOrder = columnOrder;
  }
}
