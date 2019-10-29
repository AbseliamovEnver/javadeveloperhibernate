package com.abseliamov.cinemaservice.model;

public enum Role {
    ADMIN(1),
    USER(2),
    ANONYMOUS(3);

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
