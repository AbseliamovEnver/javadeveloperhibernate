package com.abseliamov.cinemaservice.model;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "ticket_status")
public enum TicketStatus {
    PENDING(1),
    ACTIVE(2),
    INACTIVE(3),
    DELETED(4);

    @Column(name = "id")
    private long id;


    TicketStatus(long id) {
        this.id = id;
    }

    public static TicketStatus getTicketStatus(Long id) {
        if (id == null) {
            return null;
        }
        for (TicketStatus status : TicketStatus.values()) {
            if (id == status.getId()) {
                return status;
            }
        }
        return null;
    }

    public long getId() {
        return id;
    }
}
