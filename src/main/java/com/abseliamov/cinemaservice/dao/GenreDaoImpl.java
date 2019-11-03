package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.model.Genre;
import org.hibernate.SessionFactory;

public class GenreDaoImpl extends AbstractDao<Genre> {

    public GenreDaoImpl(String entityName, SessionFactory sessionFactory, Class<Genre> clazz) {
        super(entityName, sessionFactory, clazz);
    }
}
