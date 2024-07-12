package jira6.fate.domain.team.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jira6.fate.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team_table")
@Getter
@NoArgsConstructor
public class TeamUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public TeamUser(User user, Team team) {
        this.user = user;
        this.team = team;
    }
}
