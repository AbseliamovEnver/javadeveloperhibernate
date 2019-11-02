package com.abseliamov.cinemaservice.service;

import com.abseliamov.cinemaservice.dao.MovieDaoEntityImpl;
import com.abseliamov.cinemaservice.model.Genre;
import com.abseliamov.cinemaservice.model.Movie;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class MovieService {
    private MovieDaoEntityImpl movieDao;

    public MovieService(MovieDaoEntityImpl movieDao) {
        this.movieDao = movieDao;
    }

    public void createMovie(String movieTitle, Genre genre) {
        List<Movie> movies;
        if ((movies = movieDao.getAll()) != null) {
            Movie movie = movies.stream()
                    .filter(movieItem -> movieItem.getName().equalsIgnoreCase(movieTitle))
                    .findFirst()
                    .orElse(null);
            /*if (movie == null) {
                movieDao.add(new Movie(0, movieTitle, genre,
                        new BigDecimal(0).setScale(2, RoundingMode.DOWN)));
            }*/
        }
    }

    public Movie getById(long movieId) {
        List<Movie> movies = movieDao.getAll();
        return movies.stream()
                .filter(movie -> movie.getId() == movieId)
                .findFirst()
                .orElse(null);
    }

    public List<Movie> getAll() {
        List<Movie> movies = movieDao.getAll();
        printMovie(movies);
        return movies;
    }

    public void update(long movieId, String movieTitle, Genre genre, BigDecimal cost) {
//        movieDao.update(movieId, new Movie(movieId, movieTitle, genre, cost.setScale(2, RoundingMode.DOWN)));
    }

    public void delete(long movieId) {
        if (movieDao.delete(movieId)) {
            System.out.println("Movie with id \'" + movieId + "\' deleted.");
        }
    }

    public boolean increaseCostMovie(BigDecimal ticketCost, Movie movie) {
        long movieId = movie.getId();
        Movie newMovie = new Movie(movieId, movie.getName(), movie.getGenres(),
                (movie.getCost()).add(ticketCost).setScale(2, RoundingMode.DOWN));
        return movieDao.update(movieId, newMovie);
    }

    public boolean reduceCostMovie(BigDecimal ticketCost, Movie movie) {
        long movieId = movie.getId();
        Movie newMovie = new Movie(movieId, movie.getName(), movie.getGenres(),
                movie.getCost().subtract(ticketCost).setScale(2, RoundingMode.DOWN));
        return movieDao.update(movieId, newMovie);
    }

    public List<Movie> searchMostProfitableMovie() {
        List<Movie> movies = movieDao.searchMostProfitableMovie();
        printMovieByRequest(movies);
        return movies;
    }

    public List<Movie> searchLeastProfitableMovie() {
        List<Movie> movies = movieDao.searchLeastProfitableMovie();
        printMovieByRequest(movies);
        return movies;
    }

    private void printMovie(List<Movie> movies) {
        if (movies.size() != 0) {
            System.out.println("|---------------------------------------------------------------------|");
            System.out.printf("%-30s%-1s\n", " ", "LIST MOVIES");
            System.out.println("|---------------------------------------------------------------------|");
            System.out.printf("%-3s%-18s%-25s%-13s%-1s\n%-1s\n", " ", "ID", "TITLE", "GENRE", "TOTAL COST",
                    "|------|-----------------------------|-------------------|------------|");
            movies.forEach(movie -> System.out.printf("%-3s%-6s%-30s%-21s%-1s\n%-1s\n",
                    " ", movie.getId(), movie.getName(), movie.getGenres(),
                    movie.getCost().setScale(2, RoundingMode.DOWN),
                    "|------|-----------------------------|-------------------|------------|"));
        } else {
            System.out.println("List movies is empty.");
        }
    }

    private void printMovieByRequest(List<Movie> movies) {
        if (movies.size() != 0) {
            System.out.println("|-------------------------------------------------|");
            System.out.printf("%-19s%-1s\n", " ", "REQUEST RESULT");
            System.out.println("|-------------------------------------------------|");
            System.out.printf("%-3s%-13s%-23s%-1s\n%-1s\n", " ", "ID", "MOVIE TITLE", "TOTAL COST",
                    "|------|-----------------------------|------------|");
            movies.forEach(movie -> System.out.printf("%-3s%-6s%-32s%-1s\n%-1s\n",
                    " ", movie.getId(), movie.getName(), movie.getCost().setScale(2, RoundingMode.DOWN),
                    "|------|-----------------------------|------------|"));
        } else {
            System.out.println("At your request a movie is not found");
        }
    }
}
