package jira6.fate.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER("USER"),
    MANAGER("MANAGER");

    private final String userRole;

}
