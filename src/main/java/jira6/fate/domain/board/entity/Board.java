package jira6.fate.domain.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jira6.fate.global.entity.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
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

    @Builder
    public Board(String title, String intro) {

        this.title = title;
        this.intro = intro;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateIntro(String intro) {
        this.intro = intro;
    }
}
