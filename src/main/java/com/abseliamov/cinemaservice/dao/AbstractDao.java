package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.exceptions.ConnectionException;
import com.abseliamov.cinemaservice.model.GenericModel;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class AbstractDao<T extends GenericModel> implements GenericDao<T> {
    private static final String ERROR_MESSAGE = "Cannot connect to database. ";
    private org.slf4j.Logger logger = LoggerFactory.getLogger(AbstractDao.class);
    private String entityName;
    protected SessionFactory sessionFactory;
    private Class<T> clazz;

    public AbstractDao(String entityName, SessionFactory sessionFactory, Class<T> clazz) {
        this.entityName = entityName;
        this.sessionFactory = sessionFactory;
        this.clazz = clazz;
    }

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

    public List<T> getAll() {
        List<T> entities;
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                entities = session.createQuery("FROM " + entityName).list();
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                logger.error(ERROR_MESSAGE, e);
                throw new ConnectionException(ERROR_MESSAGE, e);
            }
        }
        return entities;
    }

    public boolean update(long id, T entity) {
        boolean result;
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                T entityOld = getById(id);
                entity.setId(entityOld.getId());
                session.update(entity);
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
