package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.exceptions.ConnectionException;
import com.abseliamov.cinemaservice.model.Ticket;
import com.abseliamov.cinemaservice.model.TicketStatus;
import com.abseliamov.cinemaservice.utils.ConnectionUtil;
import com.abseliamov.cinemaservice.utils.CurrentViewer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDaoImpl extends AbstractDao<Ticket> {
    private static final Logger logger = LogManager.getLogger(TicketDaoImpl.class);
    private static final String ERROR_MESSAGE = "Cannot connect to database: ";
    private Connection connection = ConnectionUtil.getConnection();
    private CurrentViewer currentViewer;
    private MovieDaoEntityImpl movieDao;
    private SeatDaoEntityImpl seatDao;

    public TicketDaoImpl(String entityName, SessionFactory sessionFactory, Class<Ticket> clazz,
                         CurrentViewer currentViewer) {
        super(entityName, sessionFactory, clazz);
        this.currentViewer = currentViewer;
    }

    public boolean buyTicket(Ticket ticket) {
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                ticket.setStatus(TicketStatus.INACTIVE);
                ticket.setViewer(currentViewer.getViewer());
                ticket.getMovie()
                        .setCost(ticket.getMovie().getCost()
                                .add(BigDecimal.valueOf(ticket.getPrice()))
                                .setScale(2, RoundingMode.DOWN));
                update(ticket.getId(), ticket);
                session.getTransaction().commit();
                return true;
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                logger.error(ERROR_MESSAGE, e);
                throw new ConnectionException(ERROR_MESSAGE, e);
            }
        }
    }

    public List<Ticket> getAllDate() {
        List<Ticket> ticketList = new ArrayList<>();
        try (PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM tickets WHERE date_time >= CURRENT_TIME() " +
                        " AND buy_status = 0")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
//                ticketList.add(convertToEntity(resultSet));
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
//                ticketList.add(convertToEntity(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(e);
        }
        return ticketList;
    }

    public List<Ticket> getAllTicketViewer() {
        List<Ticket> ticketList = new ArrayList<>();
        try (PreparedStatement statement = connection
                .prepareStatement("SELECT * FROM tickets WHERE buy_status = ?")) {
            statement.setLong(1, currentViewer.getViewer().getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
//                ticketList.add(convertToEntity(resultSet));
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
//                ticket = convertToEntity(resultSet);
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
//                ticketList.add(convertToEntity(resultSet));
            }
        } catch (SQLException e) {
            System.out.println(ERROR_MESSAGE + e);
            throw new ConnectionException(e);
        }
        return ticketList;
    }
}
