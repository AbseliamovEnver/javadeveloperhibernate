package com.abseliamov.cinemaservice.model;

import com.abseliamov.cinemaservice.model.enums.SeatTypes;
import com.abseliamov.cinemaservice.model.enums.SeatTypesConverter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "seat_id", nullable = false, updatable = false)
    private long id;

    @Column(name = "seat_type", nullable = false)
    @Convert(converter = SeatTypesConverter.class)
    @Enumerated(EnumType.STRING)
    private SeatTypes seatTypes;

    @Column(name = "number", nullable = false)
    private long number;

    @OneToMany(mappedBy = "seat", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Ticket> tickets;

    public Seat() {
    }

    public Seat(long id, SeatTypes seatTypes, long number) {
        this.id = id;
        this.seatTypes = seatTypes;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
