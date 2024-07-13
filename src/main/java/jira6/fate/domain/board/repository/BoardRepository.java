package jira6.fate.domain.board.repository;

import jira6.fate.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    boolean existsByTitle(String title);
}
