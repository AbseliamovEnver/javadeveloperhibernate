package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.model.enums.SeatTypes;
import org.hibernate.SessionFactory;

public class SeatTypesDao extends AbstractDao<SeatTypes>{

    public SeatTypesDao(String entityName, SessionFactory sessionFactory, Class<SeatTypes> clazz) {
        super(entityName, sessionFactory, clazz);
    }

    @Override
    public void add(SeatTypes item) {
    }

    @Override
    public boolean update(long id, SeatTypes item) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
