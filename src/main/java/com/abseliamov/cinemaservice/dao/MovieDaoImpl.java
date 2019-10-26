package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.model.Movie;
import com.abseliamov.cinemaservice.utils.ConnectionUtil;

import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDaoImpl extends AbstractDao<Movie> {
    private static final String ERROR_MESSAGE = "Cannot connect to database: ";
    private Connection connection = ConnectionUtil.getConnection();
    private GenreDaoImpl genreDao;

    public MovieDaoImpl(Connection connection, GenreDaoImpl genreDao, String tableName) {
        super(connection, tableName);
        this.genreDao = genreDao;
    }

    @Override
    public Movie convertToEntity(ResultSet resultSet) throws SQLException {
        return new Movie(
                resultSet.getLong("id"),
                resultSet.getString("title"),
                genreDao.getById(resultSet.getLong("genre_id")),
                resultSet.getBigDecimal("cost").setScale(2, RoundingMode.DOWN));
    }

    @Override
    public void add(Movie movie) {
        try (PreparedStatement statement = connection
                .prepareStatement("INSERT INTO movies VALUES(?,?,?,?)")) {
            statement.setLong(1, movie.getId());
            statement.setString(2, movie.getName());
            statement.setLong(3, movie.getGenre().getId());
            statement.setBigDecimal(4, movie.getCost().setScale(2, RoundingMode.DOWN));
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
        }
    }

    @Override
    public boolean update(long id, Movie movie) {
        boolean updateExist = false;
        try (PreparedStatement statement = connection
                .prepareStatement("UPDATE movies SET title = ?, genre_id = ?, cost = ? WHERE id = ?")) {
            statement.setString(1, movie.getName());
            statement.setLong(2, movie.getGenre().getId());
            statement.setBigDecimal(3, movie.getCost().setScale(2, RoundingMode.DOWN));
            statement.setLong(4, id);
            statement.executeUpdate();
            updateExist = true;
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
        }
        return updateExist;
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
