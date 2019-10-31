package com.abseliamov.cinemaservice.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "tickets")
@AttributeOverride(name = "id", column = @Column(name = "ticket_id"))
public class Ticket extends GenericModel {

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "viewer_id", nullable = false)
    private Viewer viewer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Column(name = "price", nullable = false)
    private double price;


    public Ticket() {
    }

    public Ticket(long id, LocalDateTime dateTime, Movie movie, Seat seat, double price, Viewer viewer) {
        super(id);
        this.dateTime = dateTime;
        this.viewer = viewer;
        this.movie = movie;
        this.seat = seat;
        this.price = price;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Viewer getViewer() {
        return viewer;
    }

    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
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
