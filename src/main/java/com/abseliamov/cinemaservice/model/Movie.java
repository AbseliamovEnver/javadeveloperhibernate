package com.abseliamov.cinemaservice.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie extends GenericModel {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "cost", nullable = false)
    private BigDecimal cost;

    @OneToMany(mappedBy = "movie", fetch = FetchType.EAGER)
    private List<Ticket> tickets;

    @ManyToMany
    @JoinTable(name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    public Movie() {
    }

    public Movie(long id, String title, List<Genre> genres, BigDecimal cost) {
        super(id);
        this.title = title;
        this.genres = genres;
        this.cost = cost;
    }

    public Movie(long id, String title, BigDecimal totalPrice) {
        super(id);
        this.title = title;
        this.cost = totalPrice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return String.format("%-2s%-8s%-26s%-1s\n%-1s\n", " ", getId(), getName(), getCost(),
                "|-------|-------------------------|----------|");
    }
}
