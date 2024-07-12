package jira6.fate.domain.user.entity;

import jakarta.persistence.*;
import jira6.fate.global.entity.Timestamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "table_user")
@Getter
@NoArgsConstructor
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column
    private String refreshToken;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    @Builder
    public User(String userName, String password, UserStatus userStatus, UserRole userRole) {
        this.userName = userName;
        this.password = password;
        this.userStatus = userStatus;
        this.userRole = userRole;
    }

    public void updateRoleToManager() {
        this.userRole = UserRole.MANAGER;
    }

    public void setStatusToLeave() {
        this.userStatus = UserStatus.LEAVE;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
