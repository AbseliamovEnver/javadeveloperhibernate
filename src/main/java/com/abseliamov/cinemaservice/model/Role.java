package com.abseliamov.cinemaservice.model;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "roles")
public enum Role {
    ANONYMOUS(1),
    USER(2),
    ADMIN(3);

    @Column(name = "id")
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private long id;

    Role(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%-2s%-8s%-1s\n%-1s", " ", getId(), name(), "|-------|------------|");
    }
}
