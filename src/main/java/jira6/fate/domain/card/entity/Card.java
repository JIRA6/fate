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
import java.time.LocalDate;

import jira6.fate.domain.board.entity.Team;
import jira6.fate.domain.column.entity.Columns;
import jira6.fate.domain.user.entity.User;
import jira6.fate.global.entity.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Temporal(TemporalType.DATE)
    private LocalDate deadlineAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "column_id", nullable = false)
    private Columns column;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public Card(String cardTitle, String cardContents, Long cardOrder,
        LocalDate deadlineAt, Columns column, User user, Team team) {
        this.cardTitle = cardTitle;
        this.cardContents = cardContents;
        this.cardOrder = cardOrder;
        this.deadlineAt = deadlineAt;
        this.column = column;
        this.user = user;
        this.team = team;
    }

    public void update(String cardTitle, String cardContents,
        LocalDate deadlineAt, Columns column, Team team) {
        this.cardTitle = cardTitle;
        this.cardContents = cardContents;
        this.deadlineAt = deadlineAt;
        this.column = column;
        this.team = team;
    }

}

