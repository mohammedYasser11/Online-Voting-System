package com.sprints.online_voting_system.model;

import lombok.Data;
import lombok.Getter;


// I hate lombok, no matter what I do, it seems to work sometimes and at others,
// acts like lombok doesn't work
// Class for user information, instead of dealing with maps
public class UserInfo {
    private Long userId;
    private Role role;

    // Default constructor (required by Lombok @Data)
    public UserInfo() {
    }

    // Constructor with parameters
    public UserInfo(Long userId, Role role) {
        this.userId = userId;
        this.role = role;
    }

    // Static factory method (alternative to builder pattern)
    public static UserInfo of(Long userId, Role role) {
        return new UserInfo(userId, role);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserInfo{userId=" + userId + ", role=" + role + "}";
    }
}