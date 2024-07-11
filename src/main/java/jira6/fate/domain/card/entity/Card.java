package jira6.fate.domain.card.entity;

import jakarta.persistence.*;
import jira6.fate.domain.user.entity.User;
import jira6.fate.global.entity.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private String manager_name;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deadlineAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "column_id", nullable = false)
    private Column column;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}

