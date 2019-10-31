package com.abseliamov.cinemaservice;

import com.abseliamov.cinemaservice.utils.HibernateUtil;
import com.abseliamov.cinemaservice.utils.Injector;
import com.abseliamov.cinemaservice.view.MenuContent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        logger.debug("Start Main class");
        MenuContent.createMenuContent();

        Injector.getAuthorizationMenu().authorizationMenu();

        HibernateUtil.closeSessionFactory();
    }
}
