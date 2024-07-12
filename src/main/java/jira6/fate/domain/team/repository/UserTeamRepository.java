package jira6.fate.domain.team.repository;

import jira6.fate.domain.team.entity.TeamUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTeamRepository extends JpaRepository<TeamUser, Long> {

}
