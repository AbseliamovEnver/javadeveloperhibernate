package com.abseliamov.cinemaservice.model;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "seat_types")
public enum SeatTypes {
    VIP(1),
    STANDARD(2);

    @Column(name = "seat_type_id")
    private long id;

    SeatTypes(long id) {
        this.id = id;
    }

    public static SeatTypes getSeatTypes(Long id) {
        if (id == null) {
            return null;
        }
        for (SeatTypes seatTypes : SeatTypes.values()) {
            if (id == seatTypes.getId()) {
                return seatTypes;
            }
        }
        return null;
    }

    public long getId() {
        return id;
    }
}
