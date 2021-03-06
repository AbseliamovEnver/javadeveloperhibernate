package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.exceptions.ConnectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class AbstractDao<T> implements GenericDao<T> {
    private static final Logger logger = LogManager.getLogger(AbstractDao.class);
    private static final String ERROR_MESSAGE = "Cannot connect to database. ";
    private String name;
    protected SessionFactory sessionFactory;
    private Class<T> clazz;

    public AbstractDao(String name, SessionFactory sessionFactory, Class<T> clazz) {
        this.name = name;
        this.sessionFactory = sessionFactory;
        this.clazz = clazz;
    }

    @Override
    public void add(T entity) {
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                session.save(entity);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                logger.error(ERROR_MESSAGE, e);
                throw new ConnectionException(ERROR_MESSAGE, e);
            }
        }
    }

    @Override
    public T getById(long id) {
        T entity;
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                entity = session.get(clazz, id);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                logger.error(ERROR_MESSAGE, e);
                throw new ConnectionException(ERROR_MESSAGE, e);
            }
        }
        return entity;
    }

    @Override
    public List<T> getAll() {
        List<T> entities;
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                entities = session.createQuery("from " + name).list();
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                logger.error(ERROR_MESSAGE, e);
                throw new ConnectionException(ERROR_MESSAGE, e);
            }
        }
        return entities;
    }

    @Override
    public boolean update(long id, T entity) {
        boolean result;
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                T entityOld = getById(id);
                if (entityOld != null) {
                    session.update(entity);
                    session.getTransaction().commit();
                }
                result = true;
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                logger.error(ERROR_MESSAGE, e);
                throw new ConnectionException(ERROR_MESSAGE, e);
            }
        }
        return result;
    }

    @Override
    public boolean delete(long id) {
        boolean result;
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                T entity = getById(id);
                session.delete(entity);
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
