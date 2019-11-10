package com.abseliamov.cinemaservice.service;

import com.abseliamov.cinemaservice.dao.MovieDaoImpl;
import com.abseliamov.cinemaservice.model.Genre;
import com.abseliamov.cinemaservice.model.Movie;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

public class MovieService {
    private MovieDaoImpl movieDao;

    public MovieService(MovieDaoImpl movieDao) {
        this.movieDao = movieDao;
    }

    public void createMovie(String movieTitle, Set<Genre> genres) {
        List<Movie> movies;
        if ((movies = movieDao.getAll()) != null) {
            Movie movie = movies.stream()
                    .filter(movieItem -> movieItem.getName().equalsIgnoreCase(movieTitle))
                    .findFirst()
                    .orElse(null);
            if (movie == null) {
                movieDao.add(new Movie(0, movieTitle, genres,
                        new BigDecimal(0).setScale(2, RoundingMode.DOWN)));
            }
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
        return movieDao.getAll();
    }

    public void update(long movieId, String movieTitle, Set<Genre> genres, BigDecimal cost) {
        if (movieDao.update(movieId, new Movie(movieTitle, genres, cost.setScale(2, RoundingMode.DOWN)))) {
            System.out.println("\nMovie update successfully");
        }
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

    public List<Movie> printAllMovie() {
        List<Movie> movies = movieDao.getAll();
        Genre firstGenre;
        if (movies.size() != 0) {
            System.out.println("\n|---------------------------------------------------------------------|");
            System.out.printf("%-30s%-1s\n", " ", "LIST MOVIES");
            System.out.println("|---------------------------------------------------------------------|");
            System.out.printf("%-3s%-18s%-25s%-13s%-1s\n%-1s\n", " ", "ID", "TITLE", "GENRES", "TOTAL COST",
                    "|------|-----------------------------|-------------------|------------|");
            for (Movie movie : movies) {
                firstGenre = movie.getGenres().iterator().next();
                if (movie.getGenres().size() > 1) {
                    System.out.printf("%-3s%-6s%-30s%-21s%-1s\n",
                            " ", movie.getId(), movie.getName(), firstGenre.getName(),
                            movie.getCost().setScale(2, RoundingMode.DOWN));
                    for (Genre genre : movie.getGenres()) {
                        if (genre.equals(firstGenre)) {
                            continue;
                        }
                        System.out.printf("%-39s%-1s\n",
                                " ", genre.getName());
                    }
                    System.out.println("|------|-----------------------------|-------------------|------------|");
                } else {
                    System.out.printf("%-3s%-6s%-30s%-21s%-1s\n%-1s\n",
                            " ", movie.getId(), movie.getName(), firstGenre.getName(),
                            movie.getCost().setScale(2, RoundingMode.DOWN),
                            "|------|-----------------------------|-------------------|------------|");
                }
            }
        } else {
            System.out.println("List movies is empty.");
        }
        return movies;
    }
}
