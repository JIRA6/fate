package jira6.fate.domain.team.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import jira6.fate.domain.board.entity.Board;
import jira6.fate.domain.user.entity.User;
import jira6.fate.global.entity.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "table_team")
@Getter
@NoArgsConstructor
public class Team extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String boardTitle;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TeamUser> userTeams = new HashSet<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public Team(String boardTitle, User user, Board board) {
        this.boardTitle = boardTitle;
        this.user = user;
        this.board = board;
    }

    public void addUser(User user) {
        TeamUser teamUser = new TeamUser(user, this);
        teamUser.add(teamUser);
    }
}
