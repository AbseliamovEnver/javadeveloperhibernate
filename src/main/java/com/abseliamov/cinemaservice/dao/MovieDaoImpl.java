package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.model.Movie;
import com.abseliamov.cinemaservice.utils.ConnectionUtil;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;

import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDaoImpl extends AbstractDao<Movie> {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(MovieDaoImpl.class);
    private static final String ERROR_MESSAGE = "Cannot connect to database: ";
    private Connection connection = ConnectionUtil.getConnection();

    public MovieDaoImpl(String entityName, SessionFactory sessionFactory, Class<Movie> clazz) {
        super(entityName, sessionFactory, clazz);
    }


    public List<Movie> searchMostProfitableMovie() {
        List<Movie> movies = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("" +
                     "SELECT movies.id, movies.name, movie_price.total_price FROM (" +
                     "    SELECT movie_id FROM tickets WHERE (QUARTER(date_time) = QUARTER(CURDATE()))) AS date " +
                     "RIGHT JOIN (" +
                     "    SELECT movie_id AS movie_price_id, SUM(price) AS total_price FROM tickets WHERE buy_status > 0 " +
                     "      GROUP BY movie_id ORDER BY total_price DESC LIMIT 1) AS movie_price " +
                     "ON date.movie_id = movie_price.movie_price_id " +
                     "INNER JOIN movies " +
                     "ON movies.id = movie_price.movie_price_id LIMIT 1")) {
            while (resultSet.next()) {
                movies.add(createMovieByRequest(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
        }
        return movies;
    }

    public List<Movie> searchLeastProfitableMovie() {
        List<Movie> movies = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("" +
                     "SELECT movies.id, movies.name, movie_price.total_price FROM (" +
                     "    SELECT movie_id FROM tickets WHERE (QUARTER(date_time) = QUARTER(CURDATE()))) AS date " +
                     "RIGHT JOIN (" +
                     "    SELECT movie_id AS movie_price_id, SUM(price) AS total_price FROM tickets WHERE buy_status > 0 " +
                     "      GROUP BY movie_id ORDER BY total_price LIMIT 1) AS movie_price " +
                     "ON date.movie_id = movie_price.movie_price_id " +
                     "INNER JOIN movies " +
                     "ON movies.id = movie_price.movie_price_id LIMIT 1")) {
            while (resultSet.next()) {
                movies.add(createMovieByRequest(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
        }
        return movies;
    }

    private Movie createMovieByRequest(ResultSet resultSet) throws SQLException {
        return new Movie(
                resultSet.getLong("id"),
                resultSet.getString("title"),
                resultSet.getBigDecimal("total_price").setScale(2, RoundingMode.DOWN));
    }
}
