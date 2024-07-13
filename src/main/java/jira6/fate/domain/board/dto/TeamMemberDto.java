package jira6.fate.domain.board.dto;

import jira6.fate.domain.board.entity.Team;
import jira6.fate.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamMemberDto {

    private Long userId;

    private String userName;

    public TeamMemberDto(Team team) {
        User user = team.getUser();
        this.userId = user.getId();
        this.userName = user.getUserName();
    }
}
