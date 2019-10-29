package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.model.Role;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;

public class RoleDao extends AbstractDaoEnum<Role> {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(RoleDao.class);

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
