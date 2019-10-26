package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.exceptions.ConnectionException;
import com.abseliamov.cinemaservice.model.Genre;
import com.abseliamov.cinemaservice.utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreDaoImpl extends AbstractDao<Genre> {
    Connection connection = ConnectionUtil.getConnection();

    public GenreDaoImpl(Connection connection, String tableName) {
        super(connection, tableName);
    }

    @Override
    public Genre convertToEntity(ResultSet resultSet) throws SQLException {
        return new Genre(resultSet.getLong("id"), resultSet.getString("name"));
    }

    @Override
    public boolean update(long id, Genre genre) {
        try (PreparedStatement statement = connection
                .prepareStatement("UPDATE genres SET name = ? WHERE id = ?")) {
            statement.setString(1, genre.getName().trim());
            statement.setLong(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            throw new ConnectionException(e);
        }
        return true;
    }
}
