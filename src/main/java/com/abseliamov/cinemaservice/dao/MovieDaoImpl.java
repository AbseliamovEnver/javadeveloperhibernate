package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.exceptions.ConnectionException;
import com.abseliamov.cinemaservice.model.Movie;
import com.abseliamov.cinemaservice.utils.ConnectionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDaoImpl extends AbstractDao<Movie> {
    private static final Logger logger = LogManager.getLogger(MovieDaoImpl.class);
    private static final String ERROR_MESSAGE = "Cannot connect to database: ";

    public MovieDaoImpl(String entityName, SessionFactory sessionFactory, Class<Movie> clazz) {
        super(entityName, sessionFactory, clazz);
    }

    @Override
    public boolean update(long id, Movie updateMovie) {
        boolean result;
        Movie movie;
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();

                movie = getById(id);
                movie.setName(updateMovie.getName());
                movie.setGenres(updateMovie.getGenres());
                movie.setCost(updateMovie.getCost());
                session.update(movie);

                session.getTransaction().commit();
                result = true;
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                logger.error(ERROR_MESSAGE, e);
                throw new ConnectionException(ERROR_MESSAGE, e);
            }
        }
        return result;
    }
}
