package jira6.fate.domain.card.repository;

import jira6.fate.domain.card.entity.Card;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Card, Long> {

}
