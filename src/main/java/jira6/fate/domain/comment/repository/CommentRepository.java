package jira6.fate.domain.comment.repository;

import java.util.List;
import jira6.fate.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByCardId(Long cardId);

}
