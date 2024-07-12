package jira6.fate.domain.card.repository;

import java.util.List;
import jira6.fate.domain.card.entity.Card;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Card, Long> {

    List<Card> findByColumnId(Long columnId);
}
