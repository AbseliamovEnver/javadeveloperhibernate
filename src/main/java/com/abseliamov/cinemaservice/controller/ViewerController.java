package com.abseliamov.cinemaservice.controller;

import com.abseliamov.cinemaservice.model.enums.Role;
import com.abseliamov.cinemaservice.model.Viewer;
import com.abseliamov.cinemaservice.service.ViewerService;

import java.time.LocalDate;
import java.util.List;

public class ViewerController {
    private ViewerService viewerService;

    public ViewerController(ViewerService viewerService) {
        this.viewerService = viewerService;
    }

    public boolean authorization(String name, String password) {
        return viewerService.authorization(name, password);
    }

    public boolean createViewer(String firstName, String lastName, String password, Role role, LocalDate birthday) {
        return viewerService.createViewer(firstName, lastName, password, role, birthday);
    }

    public Viewer getById(long viewerId) {
        return viewerService.getById(viewerId);
    }

    public List<Viewer> getAll() {
        return viewerService.getAll();
    }

    public void updateViewer(long viewerId, String firstName, String lastName,
                             String password, LocalDate birthday, Role role) {
        viewerService.update(viewerId, firstName, lastName, password, birthday, role);
    }

    public void deleteSeat(long viewerId) {
        viewerService.delete(viewerId);
    }

    public void searchDateWithSeveralViewersBirthday() {
        viewerService.searchDateWithSeveralViewersBirthday();
    }

    public List<Role> printAllRoles() {
        return viewerService.printAllRoles();
    }

    public List<Viewer> printAllViewer() {
        return viewerService.printAllViewer();
    }

    public Role getRoleById(long roleId) {
        return viewerService.getRoleById(roleId);
    }
}
