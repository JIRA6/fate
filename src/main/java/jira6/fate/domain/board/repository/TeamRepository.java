package jira6.fate.domain.board.repository;

import jira6.fate.domain.board.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
