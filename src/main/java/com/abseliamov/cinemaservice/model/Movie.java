package com.abseliamov.cinemaservice.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
@AttributeOverride(name = "id", column = @Column(name = "movie_id"))
@AttributeOverride(name = "name", column = @Column(name = "title"))
public class Movie extends GenericModel {

    @Column(name = "total_cost", nullable = false)
    private BigDecimal cost;

    @OneToMany(mappedBy = "movie", fetch = FetchType.EAGER)
    private List<Ticket> tickets;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "genre_id"))
    private List<Genre> genres = new ArrayList<>();

    public Movie() {
    }

    public Movie(long id, String title, List<Genre> genres, BigDecimal cost) {
        super(id, title);
        this.genres = genres;
        this.cost = cost;
    }

    public Movie(long id, String title, BigDecimal totalPrice) {
        super(id, title);
        this.cost = totalPrice;
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
