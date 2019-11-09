package com.abseliamov.cinemaservice.service;

import com.abseliamov.cinemaservice.dao.RoleDao;
import com.abseliamov.cinemaservice.model.enums.Role;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Role> printAllRoles() {
        List<Role> roles = roleDao.getAll();
        if (!roles.isEmpty()) {
            List<Role> sortedRoleList = roles
                    .stream()
                    .sorted(Comparator.comparingLong(Role::getId))
                    .collect(Collectors.toList());
            System.out.println("\n|--------------------|");
            System.out.printf("%-5s%-1s\n", " ", "LIST OF ROLES");
            System.out.println("|--------------------|");
            System.out.printf("%-3s%-9s%-1s\n", " ", "ID", "ROLE");
            System.out.println("|-------|------------|");
            sortedRoleList.forEach(System.out::println);
        } else {
            System.out.println("Role list is empty.");
        }
        return roles;
    }
}
