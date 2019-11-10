package com.abseliamov.cinemaservice.controller;

import com.abseliamov.cinemaservice.model.Genre;
import com.abseliamov.cinemaservice.model.Movie;
import com.abseliamov.cinemaservice.service.MovieService;

import java.math.BigDecimal;
import java.util.List;

public class MovieController {
    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    public void createMovie(String movieTitle, List<Genre> genres) {
        movieService.createMovie(movieTitle, genres);
    }



    public Movie getById(long movieId) {
        return movieService.getById(movieId);
    }

    public List<Movie> getAll() {
        return movieService.getAll();
    }

    public void updateMovie(long movieId, String movieTitle, Genre genre, BigDecimal cost) {
        movieService.update(movieId, movieTitle, genre, cost);
    }

    public void deleteMovie(long movieId) {
        movieService.delete(movieId);
    }

    public boolean increaseCostMovie(BigDecimal ticketCost, Movie movie) {
        return movieService.increaseCostMovie(ticketCost, movie);
    }

    public boolean reduceCostMovie(BigDecimal ticketCost, Movie movie) {
        return movieService.reduceCostMovie(ticketCost, movie);
    }

    public void searchMostProfitableMovie() {
        movieService.searchMostProfitableMovie();
    }

    public void searchLeastProfitableMovie() {
        movieService.searchLeastProfitableMovie();
    }

    public List<Movie> printAllMovie() {
        return movieService.printAllMovie();
    }
}
