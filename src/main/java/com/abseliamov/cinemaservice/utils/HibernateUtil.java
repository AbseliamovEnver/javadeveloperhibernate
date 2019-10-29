package com.abseliamov.cinemaservice.utils;

import com.abseliamov.cinemaservice.exceptions.ConnectionException;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                sessionFactory = new Configuration().configure().buildSessionFactory();
            } catch (Throwable e) {
                logger.error("Cannot create sessionFactory. ", e);
                throw new ConnectionException("Cannot create sessionFactory. ", e);
            }
        }
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        try {
            sessionFactory.close();
        } catch (HibernateException e) {
            logger.error("Cannot close sessionFactory. ", e);
            throw new ConnectionException("Cannot close sessionFactory. ", e);
        }
    }
}
