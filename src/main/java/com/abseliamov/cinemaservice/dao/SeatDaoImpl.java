package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.model.Seat;
import org.hibernate.SessionFactory;

public class SeatDaoImpl extends AbstractDao<Seat> {

    public SeatDaoImpl(String entityName, SessionFactory sessionFactory, Class<Seat> clazz) {
        super(entityName, sessionFactory, clazz);
    }
}
