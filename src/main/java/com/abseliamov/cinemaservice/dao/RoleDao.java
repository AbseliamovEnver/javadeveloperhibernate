package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.model.Role;
import org.hibernate.SessionFactory;

public class RoleDao extends AbstractDaoEnum<Role> {

    public RoleDao(SessionFactory sessionFactory, Class<Role> clazz) {
        super(sessionFactory, clazz);
    }

    @Override
    public void add(Role item) {

    }

    @Override
    public boolean update(long id, Role item) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
