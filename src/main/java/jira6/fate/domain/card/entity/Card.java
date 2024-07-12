package jira6.fate.domain.card.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import jira6.fate.domain.user.entity.User;
import jira6.fate.global.entity.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;

@Entity
@Table(name = "table_card")
@Getter
@NoArgsConstructor
public class Card extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cardTitle;

    @Column
    private String cardContents;

    @Column(nullable = false)
    private Long cardOrder;

    @Column
    private String managerName;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deadlineAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "column_id", nullable = false)
    private Columns column;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Card(String cardTitle, String cardContents, Long cardOrder, String managerName,
        LocalDateTime deadlineAt, Columns column, User user) {
        this.cardTitle = cardTitle;
        this.cardContents = cardContents;
        this.cardOrder = cardOrder;
        this.managerName = managerName;
        this.deadlineAt = deadlineAt;
        this.column = column;
        this.user = user;
    }

    public void update(String cardTitle, String cardContents, String managerName,
        LocalDateTime deadlineAt, Columns column) {
        this.cardTitle = cardTitle;
        this.cardContents = cardContents;
        this.managerName = managerName;
        this.deadlineAt = deadlineAt;
        this.column = column;
    }

}

