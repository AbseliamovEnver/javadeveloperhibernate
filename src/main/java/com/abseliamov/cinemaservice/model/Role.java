package com.abseliamov.cinemaservice.model;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "roles")
public enum Role {
    ANONYMOUS(1),
    USER(2),
    ADMIN(3);

    @Column(name = "id")
    private long id;

    Role(long id) {
        this.id = id;
    }

    public static Role getRole(Long id) {
        if (id == null) {
            return null;
        }
        for (Role role : Role.values()) {
            if (id == role.getId()) {
                return role;
            }
        }
        return null;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%-2s%-8s%-1s\n%-1s", " ", getId(), name(), "|-------|------------|");
    }
}
