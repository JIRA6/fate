package jira6.fate.domain.board.entity;

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
import java.util.List;
import jira6.fate.domain.column.entity.Columns;
import jira6.fate.domain.user.entity.User;
import jira6.fate.global.entity.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "table_board")
@Getter
@NoArgsConstructor
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String intro;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Columns> columns;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Team> teams;


    @Builder
    public Board(String title, String intro, User user, List<Team> teams) {
        this.title = title;
        this.intro = intro;
        this.user = user;
        this.teams = teams;
    }

    public void update(String title, String intro) {
        this.title = title;
        this.intro = intro;
    }
}
