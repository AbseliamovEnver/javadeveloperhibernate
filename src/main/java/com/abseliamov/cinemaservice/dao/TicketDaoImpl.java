package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.exceptions.ConnectionException;
import com.abseliamov.cinemaservice.model.Ticket;
import com.abseliamov.cinemaservice.utils.ConnectionUtil;
import com.abseliamov.cinemaservice.utils.CurrentViewer;
import com.abseliamov.cinemaservice.utils.Injector;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDaoImpl extends AbstractDao<Ticket> {
    private static final String ERROR_MESSAGE = "Cannot connect to database: ";
    private Connection connection = ConnectionUtil.getConnection();
    private CurrentViewer currentViewer;
    private MovieDaoImpl movieDao;
    private SeatDaoImpl seatDao;

    public TicketDaoImpl(Connection connection, CurrentViewer currentViewer, MovieDaoImpl movieDao,
                         SeatDaoImpl seatDao, String tableName) {
        super(connection, tableName);
        this.currentViewer = currentViewer;
        this.movieDao = movieDao;
        this.seatDao = seatDao;
    }

    @Override
    public Ticket convertToEntity(ResultSet resultSet) throws SQLException {
        return new Ticket(
                resultSet.getLong("id"),
                resultSet.getTimestamp("date_time").toLocalDateTime(),
                movieDao.getById(resultSet.getLong("movie_id")),
                seatDao.getById(resultSet.getLong("seat_id")),
                resultSet.getDouble("price"),
                resultSet.getLong("buy_status"));
    }

    @Override
    public void add(Ticket ticket) {
        try (PreparedStatement statement = connection
                .prepareStatement("INSERT INTO " + Injector.TICKETS_TABLE + " VALUES (?,?,?,?,?,?);")) {
            statement.setLong(1, ticket.getId());
            statement.setTimestamp(2, Timestamp.valueOf(ticket.getDateTime()));
            statement.setBigDecimal(3, BigDecimal.valueOf(ticket.getPrice()));
            statement.setLong(4, ticket.getStatus());
            statement.setLong(5, ticket.getMovie().getId());
            statement.setLong(6, ticket.getSeat().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(e);
        }
    }

    @Override
    public boolean update(long id, Ticket ticket) {
        try (PreparedStatement statement = connection
                .prepareStatement("UPDATE tickets SET date_time = ?, price = ?, buy_status = ?, " +
                        "movie_id = ?, seat_id = ? WHERE id = ?")) {
            statement.setTimestamp(1, Timestamp.valueOf(ticket.getDateTime()));
            statement.setBigDecimal(2, BigDecimal.valueOf(ticket.getPrice()));
            statement.setLong(3, ticket.getStatus());
            statement.setLong(4, ticket.getMovie().getId());
            statement.setLong(5, ticket.getSeat().getId());
            statement.setLong(6, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(e);
        }
        return true;
    }

    public List<Ticket> getTicketByMovieTitle(String movieTitle) {
        List<Ticket> ticketList = new ArrayList<>();
        try (PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM tickets WHERE movie_id IN (" +
                        "SELECT id FROM movies WHERE movies.name = ?) " +
                        "AND buy_status = 0")) {
            statement.setString(1, movieTitle);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ticketList.add(convertToEntity(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(e);
        }
        return ticketList;
    }

    public List<Ticket> getTicketByGenre(long genreId) {
        List<Ticket> ticketList = new ArrayList<>();
        try (PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM tickets WHERE movie_id IN(" +
                        "SELECT id FROM movies WHERE movies.genre_id = ?)" +
                        "AND buy_status = 0")) {
            statement.setLong(1, genreId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ticketList.add(convertToEntity(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(e);
        }
        return ticketList;
    }

    public List<Ticket> getAllDate() {
        List<Ticket> ticketList = new ArrayList<>();
        try (PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM tickets WHERE date_time >= CURRENT_TIME() " +
                        " AND buy_status = 0")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ticketList.add(convertToEntity(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(e);
        }
        return ticketList;
    }

    public List<Ticket> getTicketBySeatType(long seatTypeId) {
        List<Ticket> ticketList = new ArrayList<>();
        try (PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM tickets WHERE seat_id IN(" +
                        "SELECT id FROM seats WHERE seats.seat_type_id = ?) " +
                        "AND buy_status = 0")) {
            statement.setLong(1, seatTypeId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ticketList.add(convertToEntity(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(e);
        }
        return ticketList;
    }

    public boolean buyTicket(Ticket ticket) {
        boolean exist = false;
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE tickets SET buy_status = ? WHERE id = ?")) {
            statement.setLong(1, currentViewer.getViewer().getId());
            statement.setLong(2, ticket.getId());
            statement.executeUpdate();
            exist = true;
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(e);
        }
        return exist;
    }

    public List<Ticket> getAllTicketViewer() {
        List<Ticket> ticketList = new ArrayList<>();
        try (PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM tickets WHERE buy_status = ?")) {
            statement.setLong(1, currentViewer.getViewer().getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ticketList.add(convertToEntity(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(e);
        }
        return ticketList;
    }

    public Ticket getOrderedTicketById(long ticketId) {
        Ticket ticket = null;
        try (PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM tickets WHERE id = ? " +
                        "AND buy_status = ?")) {
            statement.setLong(1, ticketId);
            statement.setLong(2, currentViewer.getViewer().getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ticket = convertToEntity(resultSet);
            }
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(e);
        }
        return ticket;
    }

    public boolean returnTicket(Ticket ticket) {
        try (PreparedStatement statement = connection
                .prepareStatement("UPDATE tickets SET buy_status = ? WHERE id = ?")) {
            statement.setLong(1, 0);
            statement.setLong(2, ticket.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(e);
        }
        return true;
    }

    public List<Ticket> getAllTicketByViewerId(long viewerId) {
        List<Ticket> ticketList = new ArrayList<>();
        try (PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM tickets WHERE buy_status = ?")) {
            statement.setLong(1, viewerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ticketList.add(convertToEntity(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(e);
        }
        return ticketList;
    }
}
