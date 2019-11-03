package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.exceptions.ConnectionException;
import com.abseliamov.cinemaservice.model.Ticket;
import com.abseliamov.cinemaservice.model.enums.TicketStatus;
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
    private ViewerDaoImpl viewerDao;
    private MovieDaoImpl movieDao;
    private SeatDaoImpl seatDao;

    public TicketDaoImpl(String entityName, SessionFactory sessionFactory, Class<Ticket> clazz,
                         CurrentViewer currentViewer, ViewerDaoImpl viewerDao) {
        super(entityName, sessionFactory, clazz);
        this.currentViewer = currentViewer;
        this.viewerDao = viewerDao;
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

    public boolean returnTicket(Ticket ticket) {
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                ticket.setStatus(TicketStatus.ACTIVE);
                ticket.setViewer(viewerDao.getById(1));
                ticket.getMovie()
                        .setCost(ticket.getMovie().getCost()
                                .subtract(BigDecimal.valueOf(ticket.getPrice()))
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
