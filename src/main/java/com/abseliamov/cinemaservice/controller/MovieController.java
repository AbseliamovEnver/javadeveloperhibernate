package com.abseliamov.cinemaservice.controller;

import com.abseliamov.cinemaservice.model.Genre;
import com.abseliamov.cinemaservice.model.Movie;
import com.abseliamov.cinemaservice.service.MovieService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class MovieController {
    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    public void createMovie(String movieTitle, Set<Genre> genres) {
        movieService.createMovie(movieTitle, genres);
    }

    public Movie getById(long movieId) {
        return movieService.getById(movieId);
    }

    public List<Movie> getAll() {
        return movieService.getAll();
    }

    public void updateMovie(long movieId, String movieTitle, Set<Genre> genres, BigDecimal cost) {
        movieService.update(movieId, movieTitle, genres, cost);
    }

    public void deleteMovie(long movieId) {
        movieService.delete(movieId);
    }

    public List<Movie> printAllMovie() {
        return movieService.printAllMovie();
    }
}
