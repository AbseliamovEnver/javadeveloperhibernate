package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.exceptions.ConnectionException;
import com.abseliamov.cinemaservice.model.GenericModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T extends GenericModel> implements GenericDao<T> {
    private static final String ERROR_MESSAGE = "Cannot connect to database. In table: ";
    private String tableName;
    private Connection connection;

    public AbstractDao(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    public void add(T item) {
        try (PreparedStatement statement = connection
                .prepareStatement("INSERT INTO " + tableName + " (name)VALUES (?);")) {
            statement.setString(1, item.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + tableName + ". " + e);
            throw new ConnectionException(ERROR_MESSAGE, e);
        }
    }

    public T getById(long id) {
        T result = null;
        try (PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM " + tableName + " WHERE id = ?;")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = convertToEntity(resultSet);
            }
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + tableName + ". " + e);
            throw new ConnectionException(ERROR_MESSAGE, e);
        }
        return result;
    }

    public List<T> getAll() {
        List<T> result = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName)) {
            while (resultSet.next()) {
                T entity = convertToEntity(resultSet);
                result.add(entity);
            }
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + tableName + ". " + e);
            throw new ConnectionException(ERROR_MESSAGE, e);
        }
        return result;
    }

    public boolean delete(long id) {
        try (PreparedStatement statement = connection
                .prepareStatement("DELETE FROM " + tableName + " WHERE id = ?;")) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + tableName + ". " + e);
            throw new ConnectionException(ERROR_MESSAGE, e);
        }
        return true;
    }

    public abstract T convertToEntity(ResultSet resultSet) throws SQLException;
}
