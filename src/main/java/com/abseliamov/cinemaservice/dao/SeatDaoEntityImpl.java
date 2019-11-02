package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.model.Seat;
import org.hibernate.SessionFactory;

public class SeatDaoEntityImpl extends AbstractDao<Seat> {

    public SeatDaoEntityImpl(String entityName, SessionFactory sessionFactory, Class<Seat> clazz) {
        super(entityName, sessionFactory, clazz);
    }
}
