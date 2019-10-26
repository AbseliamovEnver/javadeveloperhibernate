package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.exceptions.ConnectionException;
import com.abseliamov.cinemaservice.model.SeatTypes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SeatTypesDao {
    private static final String ERROR_MESSAGE = "Cannot connect to database: ";
    private Connection connection;
    private String tableName;

    public SeatTypesDao(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    public SeatTypes convertToSeatType(ResultSet resultSet) throws SQLException {
        SeatTypes seatTypes = null;
        for (SeatTypes type : SeatTypes.values()) {
            if (type.getId() == resultSet.getLong("id")) {
                seatTypes = type;
            }
        }
        return seatTypes;
    }

    public SeatTypes getById(long id) {
        SeatTypes result = null;
        try (PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM " + tableName + " WHERE id = ?;")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = convertToSeatType(resultSet);
            }
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(ERROR_MESSAGE, e);
        }
        return result;
    }

    public List<SeatTypes> getAll(){
        List<SeatTypes> seatTypes = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + ";")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                seatTypes.add(convertToSeatType(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(ERROR_MESSAGE, e);
        }
        printSeatTypes(seatTypes);
        return seatTypes;
    }

    public void printSeatTypes(List<SeatTypes> seatTypesList){
        if (!seatTypesList.isEmpty()){
            List<SeatTypes> sortedTypeList = seatTypesList
                    .stream()
                    .sorted(Comparator.comparingLong(SeatTypes::getId))
                    .collect(Collectors.toList());
            System.out.println("\n|--------------------|");
            System.out.printf("%-2s%-1s\n", " ", "LIST OF SEAT TYPES");
            System.out.println("|--------------------|");
            System.out.printf("%-3s%-7s%-1s\n", " ", "ID", "SEAT TYPE");
            System.out.println("|-------|------------|");
            sortedTypeList
                    .forEach(seatTypes -> System.out.printf(String.format("%-2s%-8s%-1s\n%-1s\n",
                            " ", seatTypes.getId(), seatTypes.name(), "|-------|------------|")));
        } else {
            System.out.println("Seat type list is empty.");
        }
    }
}
