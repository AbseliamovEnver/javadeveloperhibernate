package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.model.Genre;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;

public class GenreDaoImpl extends AbstractDao<Genre> {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(GenreDaoImpl.class);

    public GenreDaoImpl(String entityName, SessionFactory sessionFactory, Class<Genre> clazz) {
        super(entityName, sessionFactory, clazz);
    }
}
