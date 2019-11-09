package com.abseliamov.cinemaservice.controller;

import com.abseliamov.cinemaservice.model.enums.Role;
import com.abseliamov.cinemaservice.service.RoleService;

import java.util.List;

public class RoleController {
    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    public List<Role> getAll() {
        return roleService.getAll();
    }

    public List<Role> printAllRoles() {
        return roleService.printAllRoles();
    }

    public Role getById(long roleId) {
        return roleService.getById(roleId);
    }
}
