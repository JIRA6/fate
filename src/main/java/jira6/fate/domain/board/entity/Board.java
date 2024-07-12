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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jira6.fate.domain.user.entity.User;
import jira6.fate.global.entity.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;

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
    private List<Column> columns;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Team> teams;

    @Builder
    public Board(String title, String intro) {
        this.title = title;
        this.intro = intro;
    }

    public void update(String title, String intro) {
        this.title = title;
        this.intro = intro;
    }
}
