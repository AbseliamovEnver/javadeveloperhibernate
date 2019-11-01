package com.abseliamov.cinemaservice.model;

public enum TicketStatus {
    PENDING(0),
    ACTIVE(1),
    INACTIVE(2),
    DELETED(3);

    private long status;

    TicketStatus(long status) {
        this.status = status;
    }

    public long getStatus() {
        return status;
    }
}
