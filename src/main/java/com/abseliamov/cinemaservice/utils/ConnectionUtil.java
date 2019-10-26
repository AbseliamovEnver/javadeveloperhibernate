package com.abseliamov.cinemaservice.utils;

import com.abseliamov.cinemaservice.exceptions.ConnectionException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    private static Connection connection;

    private ConnectionUtil() {
    }

    public static Connection getConnection() {
        Properties property = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/liquibase.properties")) {
            property.load(fis);
            String url = property.getProperty("url");
            String user = property.getProperty("username");
            String password = property.getProperty("password");

            connection = DriverManager.getConnection(url, user, password);
        } catch (IOException e) {
            throw new ConnectionException("Cannot find file ", e);
        } catch (SQLException e) {
            throw new ConnectionException("Cannot get connection to database ", e);
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new ConnectionException("Cannot close connection ", e);
        }
    }
}
