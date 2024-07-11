package jira6.fate.domain.board.repository;

import java.util.Optional;
import jira6.fate.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
