package com.abseliamov.cinemaservice.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "tickets")
public class Ticket extends GenericModel {

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "buy_status", nullable = false)
    private long status;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "viewer_id")
    private Viewer viewer;

    public Ticket(long id, LocalDateTime dateTime, Movie movie, Seat seat, double price, long status) {
        super(id);
        this.dateTime = dateTime;
        this.movie = movie;
        this.seat = seat;
        this.price = price;
        this.status = status;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("%-2s%-8s%-31s%-20s%-13s%-11s%-16s%-11s%-1s\n%1s",
                " ", getId(), getMovie().getName(), getMovie().getGenres().get(1).getName(),
                getDateTime().toLocalDate().format(dateFormatter), getDateTime().toLocalTime().format(timeFormatter),
                getSeat().getSeatTypes(), getSeat().getNumber(), getPrice(),
                "|-------|------------------------------|-------------------|------------|----------" +
                        "|-----------|-------------|---------|");
    }
}
