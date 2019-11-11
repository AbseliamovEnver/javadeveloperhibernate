package com.abseliamov.cinemaservice.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "genres")
@AttributeOverride(name = "id", column = @Column(name = "genre_id"))
public class Genre extends GenericModel {

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST},
            targetEntity = Movie.class)
    @JoinTable(name = "movie_genres",
            joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"),
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private List<Movie> movies = new ArrayList<>();

    public Genre() {
    }

    public Genre(long id, String name) {
        super(id, name);
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public String toString() {
        StringBuilder genreList = new StringBuilder();
        if (movies != null) {
            for (Movie movie : movies) {
                for (Genre genre : movie.getGenres()) {
                    genreList.append(genre.getName());
                    genreList.append("\n");
                }
            }
        }
        return String.valueOf(genreList);
    }
}
