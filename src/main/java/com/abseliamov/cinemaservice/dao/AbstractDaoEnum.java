package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.exceptions.ConnectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class AbstractDaoEnum<T> implements GenericDao<T> {
    private static final Logger logger = LogManager.getLogger(AbstractDaoEnum.class);
    private static final String ERROR_MESSAGE = "Cannot connect to database. ";
    private SessionFactory sessionFactory;
    private Class<T> clazz;
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<T> criteriaQuery;
    private Root<T> root;
    private Query<T> query;

    public AbstractDaoEnum(SessionFactory sessionFactory, Class<T> clazz) {
        this.sessionFactory = sessionFactory;
        this.clazz = clazz;
    }

    public T getById(long id) {
        T entity;
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();

                criteriaBuilder = session.getCriteriaBuilder();
                criteriaQuery = criteriaBuilder.createQuery(clazz);
                root = criteriaQuery.from(clazz);
                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
                query = session.createQuery(criteriaQuery);
                entity = query.getSingleResult();

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
        List<T> seatTypes;
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();

                criteriaBuilder = session.getCriteriaBuilder();
                criteriaQuery = criteriaBuilder.createQuery(clazz);
                root = criteriaQuery.from(clazz);
                criteriaQuery.select(root);
                query = session.createQuery(criteriaQuery);
                seatTypes = query.getResultList();

                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                logger.error(ERROR_MESSAGE, e);
                throw new ConnectionException(ERROR_MESSAGE, e);
            }
        }
        return seatTypes;
    }
}
