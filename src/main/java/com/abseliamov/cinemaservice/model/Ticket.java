package com.abseliamov.cinemaservice.model;

import com.abseliamov.cinemaservice.model.enums.TicketStatus;
import com.abseliamov.cinemaservice.model.enums.TicketStatusConvert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id", nullable = false, updatable = false)
    private long id;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "status", nullable = false)
    @Convert(converter = TicketStatusConvert.class)
    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "viewer_id", nullable = false)
    private Viewer viewer;

    public Ticket() {
    }

    public Ticket(long id, LocalDateTime dateTime, Movie movie, Seat seat, double price) {
        this.id = id;
        this.dateTime = dateTime;
        this.movie = movie;
        this.seat = seat;
        this.price = price;
    }

    public Ticket(long id, LocalDateTime dateTime, Movie movie, Seat seat,
                  double price, TicketStatus status, Viewer viewer) {
        this.id = id;
        this.dateTime = dateTime;
        this.movie = movie;
        this.seat = seat;
        this.price = price;
        this.status = status;
        this.viewer = viewer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        StringBuilder builder = new StringBuilder();
        Genre firstGenre;

        if (getMovie().getGenres().size() > 1) {
            firstGenre = getMovie().getGenres().iterator().next();
            builder.append(String.format("%-2s%-8s%-31s%-20s%-13s%-11s%-16s%-11s%-1s\n",
                    " ", getId(), getMovie().getName(), firstGenre.getName(),
                    getDateTime().toLocalDate().format(dateFormatter), getDateTime().toLocalTime().format(timeFormatter),
                    getSeat().getSeatTypes(), getSeat().getNumber(), getPrice()));
            for (Genre genre : getMovie().getGenres()) {
                if (genre.equals(firstGenre)) {
                    continue;
                }
                builder.append(String.format("%-41s%-1s\n", " ", genre.getName()));
            }
            builder.append("|-------|------------------------------|-------------------|------------|----------" +
                    "|-----------|-------------|---------|");
            return String.valueOf(builder);
        }
        return String.format("%-2s%-8s%-31s%-20s%-13s%-11s%-16s%-11s%-1s\n%-1s",
                " ", getId(), getMovie().getName(), getMovie().getGenres().iterator().next().getName(),
                getDateTime().toLocalDate().format(dateFormatter), getDateTime().toLocalTime().format(timeFormatter),
                getSeat().getSeatTypes(), getSeat().getNumber(), getPrice(),
                "|-------|------------------------------|-------------------|------------|----------" +
                        "|-----------|-------------|---------|");
    }
}
