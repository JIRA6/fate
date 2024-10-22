package jira6.fate.domain.board.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import jira6.fate.domain.board.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardResponseDto {

    private Long id;
    private String title;
    private String intro;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TeamMemberDto> teamMembers;

    @Builder
    public BoardResponseDto(Board board, Boolean includeMembers) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.intro = board.getIntro();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
        if (includeMembers) {
            this.teamMembers = board.getTeams().stream()
                .map(TeamMemberDto::new)
                .collect(Collectors.toList());
        }
    }
}
