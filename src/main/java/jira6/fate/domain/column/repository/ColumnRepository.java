package jira6.fate.domain.column.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jira6.fate.domain.column.entity.Columns;
import java.util.List;

public interface ColumnRepository extends JpaRepository<Columns, Long> {
  boolean existsByColumnNameAndBoardId(String name, Long boardId);
  List<Columns> findByBoardId(Long boardId);
}
