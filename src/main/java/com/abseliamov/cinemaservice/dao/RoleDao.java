package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.exceptions.ConnectionException;
import com.abseliamov.cinemaservice.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDao {
    private static final String ERROR_MESSAGE = "Cannot connect to database: ";
    private Connection connection;
    private String tableName;

    public RoleDao(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    public List<Role> getAll() {
        List<Role> roles = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + ";")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                roles.add(convertToRole(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(ERROR_MESSAGE, e);
        }
        return roles;
    }

    private Role convertToRole(ResultSet resultSet) throws SQLException {
        Role role = null;
        for (Role roleItem : Role.values()) {
            if (roleItem.getId() == resultSet.getLong("id")) {
                role = roleItem;
            }
        }
        return role;
    }

    public Role getById(long roleId) {
        Role role = null;
        try (PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM " + tableName + " WHERE id = ?;")) {
            statement.setLong(1, roleId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                role = convertToRole(resultSet);
            }
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(ERROR_MESSAGE, e);
        }
        return role;
    }
}
