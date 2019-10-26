package com.abseliamov.cinemaservice.model;

public enum SeatTypes {
    VIP(1),
    STANDARD(2);

    private long id;

    SeatTypes(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
