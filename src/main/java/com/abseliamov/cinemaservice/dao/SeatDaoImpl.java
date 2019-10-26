package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.exceptions.ConnectionException;
import com.abseliamov.cinemaservice.model.Seat;
import com.abseliamov.cinemaservice.utils.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SeatDaoImpl extends AbstractDao<Seat> {
    private SeatTypesDao seatTypesDao;
    private Connection connection = ConnectionUtil.getConnection();
    private static final String ERROR_MESSAGE = "Cannot connect to database: ";

    public SeatDaoImpl(Connection connection, SeatTypesDao seatTypesDao, String tableName) {
        super(connection, tableName);
        this.seatTypesDao = seatTypesDao;
    }

    @Override
    public Seat convertToEntity(ResultSet resultSet) throws SQLException {
        return new Seat(
                resultSet.getLong("id"),
                seatTypesDao.getById(resultSet.getLong("seat_type_id")),
                resultSet.getLong("number"));
    }

    @Override
    public void add(Seat seat){
        try (PreparedStatement statement = connection
                .prepareStatement("INSERT INTO seats VALUES(?,?,?)")) {
            statement.setLong(1, seat.getId());
            statement.setLong(2, seat.getNumber());
            statement.setLong(3, seat.getSeatTypes().getId());
            statement.executeUpdate();
            System.out.println("Seat number = " + seat.getNumber() +
                    " and type " + seat.getSeatTypes() + " successfully added.");
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
        }
    }

    @Override
    public boolean update(long id, Seat seat) {
        try (PreparedStatement statement = connection
                .prepareStatement("UPDATE seats SET number = ?, seat_type_id = ? WHERE id = ?")) {
            statement.setLong(1, seat.getNumber());
            statement.setLong(2, seat.getSeatTypes().getId());
            statement.setLong(3, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(e);
        }
        return true;
    }
}
