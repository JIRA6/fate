package jira6.fate.domain.card.repository;

import java.util.List;
import jira6.fate.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByColumnId(Long columnId);
    List<Card> findByTeamId(Long teamId);

}
