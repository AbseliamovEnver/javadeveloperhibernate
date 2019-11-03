package com.abseliamov.cinemaservice.utils;

import com.abseliamov.cinemaservice.controller.*;
import com.abseliamov.cinemaservice.dao.*;
import com.abseliamov.cinemaservice.model.*;
import com.abseliamov.cinemaservice.model.enums.Role;
import com.abseliamov.cinemaservice.model.enums.SeatTypes;
import com.abseliamov.cinemaservice.service.*;
import com.abseliamov.cinemaservice.view.AdminMenu;
import com.abseliamov.cinemaservice.view.AuthorizationMenu;
import com.abseliamov.cinemaservice.view.ViewerMenu;
import org.hibernate.SessionFactory;

public class Injector {

    private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    private static CurrentViewer currentViewer = CurrentViewer.getInstance();

    private static ViewerDaoImpl viewerDao = new ViewerDaoImpl(Viewer.class.getSimpleName(), sessionFactory, Viewer.class);

    private static GenreDaoImpl genreDao = new GenreDaoImpl(Genre.class.getSimpleName(), sessionFactory, Genre.class);
    private static GenreService genreService = new GenreService(genreDao);
    private static GenreController genreController = new GenreController(genreService);

    private static MovieDaoImpl movieDao = new MovieDaoImpl(Movie.class.getSimpleName(), sessionFactory, Movie.class);
    private static MovieService movieService = new MovieService(movieDao);
    private static MovieController movieController = new MovieController(movieService);

    private static SeatTypesDao seatTypesDao = new SeatTypesDao(SeatTypes.class.getSimpleName(), sessionFactory, SeatTypes.class);
    private static SeatTypesService seatTypesService = new SeatTypesService(seatTypesDao);
    private static SeatTypesController seatTypesController = new SeatTypesController(seatTypesService);

    private static SeatDaoImpl seatDao = new SeatDaoImpl(Seat.class.getSimpleName(), sessionFactory, Seat.class);
    private static SeatService seatService = new SeatService(seatDao);
    private static SeatController seatController = new SeatController(seatService);

    private static RoleDao roleDao = new RoleDao(sessionFactory, Role.class);
    private static RoleService roleService = new RoleService(roleDao);
    private static RoleController roleController = new RoleController(roleService);

    private static TicketDaoImpl ticketDao = new TicketDaoImpl(Ticket.class.getSimpleName(),
            sessionFactory, Ticket.class, currentViewer, viewerDao);
    private static TicketService ticketService = new TicketService(ticketDao, viewerDao, currentViewer);
    private static TicketController ticketController = new TicketController(ticketService);

    private static ViewerService viewerService = new ViewerService(viewerDao, currentViewer, ticketDao);
    private static ViewerController viewerController = new ViewerController(viewerService);


    private static ViewerMenu viewerMenu = new ViewerMenu(currentViewer, viewerController, ticketController,
            genreController, seatController, seatTypesController, movieController);

    private static AdminMenu adminMenu = new AdminMenu(genreController, movieController, seatController,
            viewerController, ticketController, seatTypesController, roleController, viewerMenu);

    private static AuthorizationMenu authorizationMenu = new AuthorizationMenu(adminMenu, viewerMenu,
            viewerController, currentViewer);

    private Injector() {
    }

    public static AuthorizationMenu getAuthorizationMenu() {
        return authorizationMenu;
    }
}
