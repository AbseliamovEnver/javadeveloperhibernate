package com.abseliamov.cinemaservice;

import com.abseliamov.cinemaservice.utils.Injector;
import com.abseliamov.cinemaservice.view.MenuContent;

public class Main {
    public static void main(String[] args) {
        MenuContent.createMenuContent();

        Injector.getAuthorizationMenu().authorizationMenu();
    }
}
