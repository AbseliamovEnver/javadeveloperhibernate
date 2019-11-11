package com.abseliamov.cinemaservice.service;

import com.abseliamov.cinemaservice.dao.RoleDao;
import com.abseliamov.cinemaservice.model.enums.Role;

import java.util.List;

public class RoleService {
    private RoleDao roleDao;

    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public Role getById(long roleId) {
        return roleDao.getById(roleId);
    }

    public List<Role> getAll() {
        return roleDao.getAll();
    }
}
