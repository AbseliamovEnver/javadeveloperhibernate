package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.exceptions.ConnectionException;
import com.abseliamov.cinemaservice.model.Ticket;
import com.abseliamov.cinemaservice.model.enums.TicketStatus;
import com.abseliamov.cinemaservice.utils.ConnectionUtil;
import com.abseliamov.cinemaservice.utils.CurrentViewer;
import com.abseliamov.cinemaservice.utils.EntityManagerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public List searchMostProfitableMovie() {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        String sql = "SELECT ticket_id, date_time, movie_id, seat_id, SUM(price) AS price, status, viewer_id " +
                " FROM tickets " +
                " WHERE(QUARTER(date_time) = QUARTER(CURDATE())" +
                "       AND status = 'INACTIVE')" +
                " GROUP BY movie_id ORDER BY price DESC LIMIT 1";
        Query query = entityManager.createNativeQuery(sql, Ticket.class);
        return query.getResultList();
    }

    public List searchLeastProfitableMovie() {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        String sql = "SELECT ticket_id, date_time, movie_id, seat_id, SUM(price) AS price, status, viewer_id " +
                " FROM tickets " +
                " WHERE(QUARTER(date_time) = QUARTER(CURDATE())" +
                "       AND status = 'INACTIVE')" +
                " GROUP BY movie_id ORDER BY price LIMIT 1";
        Query query = entityManager.createNativeQuery(sql, Ticket.class);
        return query.getResultList();
    }

    public List searchViewerMovieCountByGenre(long genreId) {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        String sql = "SELECT * FROM tickets t " +
                " JOIN movie_genres m_g ON t.movie_id = m_g.movie_id " +
                " WHERE m_g.genre_id = ?1 " +
                "   AND QUARTER(t.date_time) = QUARTER(CURDATE()) " +
                "   AND status = 'INACTIVE' " +
                " GROUP BY t.viewer_id HAVING COUNT(viewer_id) > 10";
        Query query = entityManager.createNativeQuery(sql, Ticket.class)
                .setParameter(1, genreId);
        return query.getResultList();
    }

    public List searchViewersVisitingMovieInIntervalDaysFromBirthday() {
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        String sql = "SELECT * FROM tickets t " +
                " JOIN viewers v ON t.viewer_id = v.viewer_id " +
                " WHERE t.date_time >= CURRENT_TIME " +
                "   AND t.status = 'INACTIVE' " +
                "   AND (DAYOFYEAR(t.date_time) BETWEEN (DAYOFYEAR(v.birthday) - 3) AND DAYOFYEAR(v.birthday) " +
                "       OR DAYOFYEAR(t.date_time) BETWEEN DAYOFYEAR(v.birthday) AND (DAYOFYEAR(v.birthday) + 3 ))";
        Query query = entityManager.createNativeQuery(sql, Ticket.class);
        return query.getResultList();
    }

    public List searchViewerByComplexQuery(long genreId, double amount, List<LocalDate> dates) {
        LocalDateTime startDate = LocalDateTime.from(dates.get(0).atStartOfDay());
        LocalDateTime endDate = LocalDateTime.from(dates.get(1).atTime(23,59));
        EntityManager entityManager = EntityManagerUtil.getEntityManager();
        String sql = "SELECT t.ticket_id, t.date_time, t.movie_id, t.seat_id, SUM(price) AS price, t.status, t.viewer_id " +
                "       FROM tickets t " +
                "     JOIN movie_genres m_g ON t.movie_id = m_g.movie_id " +
                "       WHERE m_g.genre_id = ?1 " +
                "           AND t.status = 'INACTIVE' " +
                "           AND t.date_time >= ?2 " +
                "           AND t.date_time <= ?3 " +
                "   GROUP BY t.viewer_id HAVING price >= ?4";
        Query query = entityManager.createNativeQuery(sql, Ticket.class)
                .setParameter(1, genreId)
                .setParameter(2, startDate)
                .setParameter(3, endDate)
                .setParameter(4, amount);
        return query.getResultList();
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
