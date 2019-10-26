package com.abseliamov.cinemaservice.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ticket extends GenericModel {
    private LocalDateTime dateTime;
    private Movie movie;
    private Seat seat;
    private double price;
    private long status;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (Double.compare(ticket.price, price) != 0) return false;
        if (status != ticket.status) return false;
        if (movie != null ? !movie.equals(ticket.movie) : ticket.movie != null) return false;
        if (dateTime != null ? !dateTime.equals(ticket.dateTime) : ticket.dateTime != null) return false;
        return seat != null ? seat.equals(ticket.seat) : ticket.seat == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = movie != null ? movie.hashCode() : 0;
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        result = 31 * result + (seat != null ? seat.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (status ^ (status >>> 32));
        return result;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("%-2s%-8s%-31s%-20s%-13s%-11s%-16s%-11s%-1s\n%1s",
                " ", getId(), getMovie().getName(), getMovie().getGenre().getName(),
                getDateTime().toLocalDate().format(dateFormatter), getDateTime().toLocalTime().format(timeFormatter),
                getSeat().getSeatTypes(), getSeat().getNumber(), getPrice(),
                "|-------|------------------------------|-------------------|------------|----------" +
                        "|-----------|-------------|---------|");
    }
}
