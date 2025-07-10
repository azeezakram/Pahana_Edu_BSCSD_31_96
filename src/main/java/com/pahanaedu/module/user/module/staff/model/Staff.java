package com.pahanaedu.module.user.module.staff.model;

import com.pahanaedu.module.user.model.User;

import java.time.LocalDateTime;

public class Staff extends User {

    private String username;
    private String password;
    private Boolean isActive;

    public Staff(Long id, String name, String role, String username, String password, boolean isActive, LocalDateTime createdAt,
                 LocalDateTime updatedAt) {

        super(id, name, role, createdAt, updatedAt);
        this.username = username;
        this.password = password;
        this.isActive = isActive;

    }

    public Staff() {
    }

    public Staff(String name, String username, String password) {
        super(name);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Staff { " + getId() + " : " + getName() + " : " + getRole() + " + " + getUsername() + " ; "
            + getPassword() + " : " + getIsActive() + " : " + getCreatedAt() + " : " + getUpdatedAt() + " }";
    }
}
