package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.model.Genre;
import org.hibernate.SessionFactory;

public class GenreDaoEntityImpl extends AbstractDao<Genre> {

    public GenreDaoEntityImpl(String entityName, SessionFactory sessionFactory, Class<Genre> clazz) {
        super(entityName, sessionFactory, clazz);
    }
}
