package com.abseliamov.cinemaservice.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "seats")
@AttributeOverride(name = "id", column = @Column(name = "seat_id"))
public class Seat extends GenericModel {

    @Enumerated(EnumType.STRING)
    private SeatTypes seatTypes;

    @Column(name = "number", nullable = false)
    private long number;

    @OneToMany(mappedBy = "seat", fetch = FetchType.EAGER)
    private List<Ticket> tickets;

    public Seat() {
    }

    public Seat(long id, SeatTypes seatTypes, long number) {
        super(id);
        this.seatTypes = seatTypes;
        this.number = number;
    }

    public SeatTypes getSeatTypes() {
        return seatTypes;
    }

    public void setSeatTypes(SeatTypes seatTypes) {
        this.seatTypes = seatTypes;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return String.format("%-2s%-8s%-1s\n%-1s", " ", getId(), getSeatTypes(), "|-------|------------|");
    }
}
