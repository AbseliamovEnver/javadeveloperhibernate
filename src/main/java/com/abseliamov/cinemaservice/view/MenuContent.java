package com.abseliamov.cinemaservice.view;

import java.util.ArrayList;
import java.util.List;

public class MenuContent {
    private static List<String> headerMenu = new ArrayList<>();
    private static List<String> authorizationMenu  = new ArrayList<>();
    private static List<String> viewerMenu = new ArrayList<>();
    private static List<String> searchMenu = new ArrayList<>();
    private static List<String> requestMenu = new ArrayList<>();
    private static List<String> adminMenu = new ArrayList<>();
    private static List<String> adminMenuCreate = new ArrayList<>();
    private static List<String> adminMenuSelect = new ArrayList<>();
    private static List<String> adminMenuUpdate = new ArrayList<>();
    private static List<String> adminMenuDelete = new ArrayList<>();
    private static List<String> footerMenu = new ArrayList<>();

    private MenuContent() {
    }

    public static void createMenuContent() {
        headerMenu.add("*********************************");
        headerMenu.add("   ** WELCOME TO THE CINEMA **");
        headerMenu.add("*********************************");

        authorizationMenu.add("AUTHORIZATION MENU");
        authorizationMenu.add("Log in");
        authorizationMenu.add("Registration");
        authorizationMenu.add("Exit");

        viewerMenu.add("VIEWER MENU");
        viewerMenu.add("Search ticket");
        viewerMenu.add("Buy ticket");
        viewerMenu.add("Return ticket");
        viewerMenu.add("Search tickets by viewer");
        viewerMenu.add("Test request");
        viewerMenu.add("Exit");

        searchMenu.add("SEARCH MENU");
        searchMenu.add("Search tickets by movie title");
        searchMenu.add("Search tickets by genre");
        searchMenu.add("Search tickets by date");
        searchMenu.add("Search tickets by seat type");
        searchMenu.add("Exit");

        requestMenu.add("REQUEST MENU");
        requestMenu.add("Search for the most profitable movie for the current quarter");
        requestMenu.add("Search for the least profitable movie for the current quarter");
        requestMenu.add("Search for a viewer who has visited more than 10 films in the current quarter by the specified genre");
        requestMenu.add("Search for viewers who attend a session between + -3 days from their birthday");
        requestMenu.add("Search for viewers who bought tickets for the specified genre, " +
                "for an amount greater than specified in the request, in the specified period of time");
        requestMenu.add("Search for dates on which more than 5 viewers have a birthday");
        requestMenu.add("Exit");

        adminMenu.add("ADMIN MENU");
        adminMenu.add("Create entity");
        adminMenu.add("Select entity");
        adminMenu.add("Update entity");
        adminMenu.add("Delete entity");
        adminMenu.add("Viewer menu");
        adminMenu.add("Exit");

        adminMenuCreate.add("CREATE MENU");
        adminMenuCreate.add("Create genre");
        adminMenuCreate.add("Create movie");
        adminMenuCreate.add("Create seat");
        adminMenuCreate.add("Create viewer");
        adminMenuCreate.add("Create ticket");
        adminMenuCreate.add("Exit");

        adminMenuSelect.add("SELECT MENU");
        adminMenuSelect.add("Select genre");
        adminMenuSelect.add("Select movie");
        adminMenuSelect.add("Select seat");
        adminMenuSelect.add("Select viewer");
        adminMenuSelect.add("Select ticket");
        adminMenuSelect.add("Exit");

        adminMenuUpdate.add("UPDATE MENU");
        adminMenuUpdate.add("Update genre");
        adminMenuUpdate.add("Update movie");
        adminMenuUpdate.add("Update seat");
        adminMenuUpdate.add("Update viewer");
        adminMenuUpdate.add("Update ticket");
        adminMenuUpdate.add("Exit");

        adminMenuDelete.add("DELETE MENU");
        adminMenuDelete.add("Delete genre");
        adminMenuDelete.add("Delete movie");
        adminMenuDelete.add("Delete seat");
        adminMenuDelete.add("Delete viewer");
        adminMenuDelete.add("Delete ticket");
        adminMenuDelete.add("Exit");

        footerMenu.add("**************************************");
        footerMenu.add("-= THANK FOR USING OUR APPLICATION! =-");
        footerMenu.add("****-= ENJOY WATCHING THE MOVIE =-****");
    }

    public static List<String> getHeaderMenu() {
        return headerMenu;
    }

    public static List<String> getAuthorizationMenu() {
        return authorizationMenu;
    }

    public static List<String> getViewerMenu() {
        return viewerMenu;
    }

    public static List<String> getSearchMenu() {
        return searchMenu;
    }

    public static List<String> getRequestMenu() {
        return requestMenu;
    }

    public static List<String> getAdminMenu() {
        return adminMenu;
    }

    public static List<String> getAdminMenuCreate() {
        return adminMenuCreate;
    }

    public static List<String> getAdminMenuSelect() {
        return adminMenuSelect;
    }

    public static List<String> getAdminMenuUpdate() {
        return adminMenuUpdate;
    }

    public static List<String> getAdminMenuDelete() {
        return adminMenuDelete;
    }

    public static List<String> getFooterMenu() {
        return footerMenu;
    }
}
