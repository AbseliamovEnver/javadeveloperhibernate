package com.abseliamov.cinemaservice.view;

import com.abseliamov.cinemaservice.controller.*;
import com.abseliamov.cinemaservice.model.enums.Role;
import com.abseliamov.cinemaservice.model.Ticket;
import com.abseliamov.cinemaservice.model.Viewer;
import com.abseliamov.cinemaservice.utils.CurrentViewer;
import com.abseliamov.cinemaservice.utils.IOUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewerMenu {
    private CurrentViewer currentViewer;
    private ViewerController viewerController;
    private TicketController ticketController;
    private GenreController genreController;
    private SeatController seatController;
    private SeatTypesController seatTypesController;
    private MovieController movieController;

    public ViewerMenu() {
    }

    public ViewerMenu(CurrentViewer currentViewer, ViewerController viewerController, TicketController ticketController,
                      GenreController genreController, SeatController seatController,
                      SeatTypesController seatTypesController, MovieController movieController) {
        this.currentViewer = currentViewer;
        this.viewerController = viewerController;
        this.ticketController = ticketController;
        this.genreController = genreController;
        this.seatController = seatController;
        this.seatTypesController = seatTypesController;
        this.movieController = movieController;
    }

    public long viewerMenu() {
        long mainMenuItem = -1;
        long ticketId = 0;
        IOUtil.printMenuHeader(MenuContent.getHeaderMenu());
        Role role = currentViewer.getViewer().getRole();
        while (true) {
            if (mainMenuItem == -1) {
                IOUtil.printMenuItem(MenuContent.getViewerMenu());
                mainMenuItem = IOUtil.getValidLongInputData("Select VIEWER MENU item: ");
            }
            switch ((int) mainMenuItem) {
                case 0:
                    if (role == Role.ADMIN) {
                        return -1;
                    } else if (role == Role.USER) {
                        IOUtil.printMenuHeader(MenuContent.getFooterMenu());
                        System.exit(0);
                    }
                    break;
                case 1:
                    ticketId = searchMenu();
                    mainMenuItem = ticketId != 0 ? 2 : -1;
                    break;
                case 2:
                    if (ticketId != 0 && buyTicketById(ticketId)) {
                        ticketId = 0;
                        mainMenuItem = -1;
                    } else {
                        if (!buyTicket()) {
                            mainMenuItem = -1;
                        }
                    }
                    break;
                case 3:
                    mainMenuItem = returnTicket() ? 3 : -1;
                    break;
                case 4:
                    mainMenuItem = searchTicketByViewer() ? 4 : -1;
                    break;
                case 5:
                    mainMenuItem = requestMenu() == 0 ? -1 : 5;
                    break;
                default:
                    if (mainMenuItem >= MenuContent.getViewerMenu().size() - 1) {
                        mainMenuItem = -1;
                        System.out.println("Enter correct viewer menu item.\n*********************************");
                    }
                    break;
            }
        }
    }

    private long searchMenu() {
        long searchMenuItem = -1;
        long ticketId;
        while (true) {
            if (searchMenuItem == -1) {
                IOUtil.printMenuItem(MenuContent.getSearchMenu());
                searchMenuItem = IOUtil.getValidLongInputData("Select SEARCH MENU item: ");
            }
            switch ((int) searchMenuItem) {
                case 0:
                    return 0;
                case 1:
                    ticketId = searchTicketByMovieTitle();
                    if (ticketId != 0) {
                        return ticketId;
                    } else {
                        searchMenuItem = -1;
                    }
                    break;
                case 2:
                    ticketId = searchTicketByGenre();
                    if (ticketId != 0) {
                        return ticketId;
                    } else {
                        searchMenuItem = -1;
                    }
                    break;
                case 3:
                    ticketId = searchTicketByDate();
                    if (ticketId != 0) {
                        return ticketId;
                    } else {
                        searchMenuItem = -1;
                    }
                    break;
                case 4:
                    ticketId = searchTicketByTypeSeat();
                    if (ticketId != 0) {
                        return ticketId;
                    } else {
                        searchMenuItem = -1;
                    }
                    break;
                default:
                    if (searchMenuItem >= MenuContent.getSearchMenu().size() - 1) {
                        searchMenuItem = -1;
                        System.out.println("Enter correct search menu item.\n*********************************");
                    }
                    break;
            }
        }
    }

    private long requestMenu() {
        long requestMenuItem = -1;
        while (true) {
            if (requestMenuItem == -1) {
                IOUtil.printMenuItem(MenuContent.getRequestMenu());
                requestMenuItem = IOUtil.getValidLongInputData("Select REQUEST MENU item: ");
            }
            switch ((int) requestMenuItem) {
                case 0:
                    return 0;
                case 1:
                    movieController.searchMostProfitableMovie();
                    requestMenuItem = -1;
                    break;
                case 2:
                    movieController.searchLeastProfitableMovie();
                    requestMenuItem = -1;
                    break;
                case 3:
                    searchViewerMovieCountByGenre();
                    requestMenuItem = -1;
                    break;
                case 4:
                    viewerController.searchViewersVisitingMovieInIntervalDaysFromBirthday();
                    requestMenuItem = -1;
                    break;
                case 5:
                    searchViewerByComplexQuery();
                    requestMenuItem = -1;
                    break;
                case 6:
                    viewerController.searchDateWithSeveralViewersBirthday();
                    requestMenuItem = -1;
                    break;
                default:
                    if (requestMenuItem >= MenuContent.getRequestMenu().size() - 1) {
                        requestMenuItem = -1;
                        System.out.println("Enter correct request menu item.\n*********************************");
                    }
                    break;
            }
        }
    }

    private long searchTicketByMovieTitle() {
        long ticketId = 0;
        String movieTitle = IOUtil.readString("Enter movie title: ");
        List<Ticket> ticketList = ticketController.getTicketByMovieTitle(movieTitle);
        if (ticketList != null) {
            ticketId = checkTicketAvailable(ticketList);
        } else {
            System.out.println("\nMovie tickets for " + movieTitle + " not found.");
        }
        return ticketId;
    }

    private long searchTicketByGenre() {
        long ticketId = 0;
        long genreListSize;
        List<Ticket> ticketList;
        if ((genreListSize = genreController.printGenre().size()) != 0) {
            long genreId = IOUtil.readNumber("\nEnter ID genre or \'0\' to return: ");
            if (genreId != 0 && genreId <= genreListSize) {
                if ((ticketList = ticketController.getTicketByGenre(genreId)).size() != 0) {
                    ticketId = checkTicketAvailable(ticketList);
                } else {
                    System.out.println("\nTickets for the requested genre not found.");
                }
            } else {
                System.out.println("\nGenre with id \'" + genreId + "\' not available.\n" +
                        "Please try again.");
            }
        }
        return ticketId;
    }

    private long searchTicketByDate() {
        long ticketId = 0;
        List<Ticket> ticketList;
        Map<LocalDate, Long> dateMap;
        if ((dateMap = ticketController.printAllActiveTicketDate()).size() != 0) {
            long dateId = IOUtil.readNumber("\nEnter ID date or \'0\' to return: ");
            if (dateId != 0 && (ticketList = ticketController.getAllTicketByDate(dateId, dateMap)).size() != 0) {
                ticketId = checkTicketAvailable(ticketList);
            } else {
                System.out.println("\nTickets for the requested date not found.");
            }
        } else {
            System.out.println("\nList date is empty. ");
        }
        return ticketId;
    }

    private long searchTicketByTypeSeat() {
        long ticketId = 0;
        long seatTypeListSize;
        List<Ticket> ticketList;
        if ((seatTypeListSize = seatController.printAllSeatType().size()) != 0) {
            long seatTypeId = IOUtil.readNumber("\nEnter ID seat type or \'0\' to return: ");
            if (seatTypeId != 0 && seatTypeId <= seatTypeListSize) {
                if ((ticketList = ticketController.getTicketBySeatType(seatTypeId)).size() != 0) {
                    ticketId = checkTicketAvailable(ticketList);
                } else {
                    System.out.println("\nTickets for the requested seat type not found.");
                }
            } else {
                System.out.println("\nSeat type with id \'" + seatTypeId + "\' not available.\n" +
                        "Please try again.");
            }
        }
        return ticketId;
    }

    private boolean buyTicketById(long ticketId) {
        boolean buyExist = false;
        if (ticketId != 0 && ticketController.checkTicketAvailable(ticketId)) {
            long ticketConfirm = IOUtil.readNumber("\nEnter the ticket ID to confirm the purchase or \'0\' to return: ");
            if (ticketConfirm == 0) {
                return buyExist;
            } else if (ticketId == ticketConfirm) {
                if (ticketController.buyTicket(ticketId)) {
                    System.out.println("\nThanks for your purchase\n");
                    buyExist = true;
                } else {
                    System.out.println("\nPurchase failed. Please try again.\n");
                }
            } else {
                System.out.println("\nThe entered \'" + ticketConfirm + "\' does not match the available ticket.");
                System.out.println("Please try again.");
            }
        } else {
            System.out.println("\nTicket with id \'" + ticketId + "\' not found.\n");
        }
        return buyExist;
    }

    private boolean buyTicket() {
        boolean buyExist = false;
        long ticketId = IOUtil.readNumber("\nPlease enter the ticket ID or \'0\' to return: ");
        if (ticketId != 0) {
            buyExist = buyTicketById(ticketId);
        }
        return buyExist;
    }

    private boolean returnTicket() {
        boolean returnExist = false;
        if (ticketController.getAllViewerTicket().size() != 0) {
            long ticketId = IOUtil.readNumber("\nEnter the ticket ID to return or \'0\' to return menu: ");
            if (ticketId != 0 && ticketController.getOrderedTicketById(ticketId) != null) {
                long ticketIdConfirm = IOUtil.readNumber("\nEnter ticket ID to confirm return the ticket or \'0\' to return: ");
                if (ticketId == ticketIdConfirm && ticketController.returnTicket(ticketId)) {
                    returnExist = true;
                    System.out.println("\nTicket returned.\n");
                } else {
                    System.out.println("\nTicket not returned.\n");
                }
            }
        } else {
            System.out.println("\nList of tickets ordered is empty.");
        }
        return returnExist;
    }

    private boolean searchTicketByViewer() {
        boolean ticketExist = false;
        List<Viewer> viewerList;
        if (currentViewer.getViewer().getRole() == Role.ADMIN) {
            viewerList = viewerController.getAll();
            long viewerId = IOUtil.readNumber("\nEnter viewer ID or \'0\' to return menu: ");
            if (viewerId != 0 && confirmViewer(viewerList, viewerId)) {
                if (ticketController.getAllTicketByViewerId(viewerId) != null) {
                    ticketExist = true;
                }
            }
        } else {
            System.out.println("\nThis menu requires administrator rights\n.");
        }
        return ticketExist;
    }

    private long checkTicketAvailable(List<Ticket> ticketList) {
        long ticketId = 0;
        if (ticketList != null) {
            ticketId = IOUtil.readNumber("\nEnter ticket ID to buy or enter \'0\' for a new search: ");
            if (ticketId == 0) {
                return ticketId;
            } else {
                ticketId = ticketController.checkTicketAvailable(ticketId) ? ticketId : 0;
                if (ticketId != 0 && confirmTicketId(ticketList, ticketId)) {
                    return ticketId;
                } else if (ticketId != 0) {
                    System.out.println("Ticket id \'" + ticketId + "\' not confirmed.\n");
                    ticketId = 0;
                }
            }
        } else {
            System.out.println("Ticket list is empty.");
        }
        return ticketId;
    }

    private boolean confirmTicketId(List<Ticket> ticketList, long ticketId) {
        return ticketList.stream()
                .anyMatch(ticket -> ticket.getId() == ticketId);
    }

    private boolean confirmViewer(List<Viewer> viewers, long viewerId) {
        boolean viewerExist = false;
        if (viewers != null) {
            viewerExist = viewers.stream().anyMatch(viewer -> viewer.getId() == viewerId);
            System.out.println((viewerExist) ? "" : "Viewer with id \'" + viewerId + "\' not found.\n");
        } else {
            System.out.println("List viewers is empty.");
        }
        return viewerExist;
    }

    private void searchViewerMovieCountByGenre() {
        long genreListSize;
        if ((genreListSize = genreController.getAll().size()) != 0) {
            long genreId = IOUtil.readNumber("\nEnter ID genre or \'0\' to return: ");
            if (genreId != 0 && genreId <= genreListSize) {
                viewerController.searchViewerMovieCountByGenre(genreId);
            } else {
                System.out.println("Genre with id \'" + genreId + "\' not available.\n" +
                        "Please try again.");
            }
        }
    }

    private void searchViewerByComplexQuery() {
        long genreId;
        double amount;
        List<LocalDate> dates;
        if ((genreId = getGenreId()) != 0) {
            amount = getAmountOfOrdersViewer();
            if ((dates = getDatePeriod()) != null) {
                viewerController.searchViewerByComplexQuery(genreId, amount, dates);
            }
        }
    }

    private long getGenreId() {
        long genreListSize;
        long genreId = 0;
        if ((genreListSize = genreController.getAll().size()) != 0) {
            genreId = IOUtil.readNumber("\nEnter ID genre or \'0\' to return: ");
            if (genreId != 0 && genreId <= genreListSize) {
                return genreId;
            } else {
                System.out.println("Genre with id \'" + genreId + "\' not available.\n" +
                        "Please try again.");
            }
        }
        return genreId;
    }

    private double getAmountOfOrdersViewer() {
        double amount;
        amount = IOUtil.readNumber("\nEnter amount of orders from the viewer or \'0\' to return: ");
        return amount > 0 ? amount : 0;
    }

    private List<LocalDate> getDatePeriod() {
        List<LocalDate> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fromDate, toDate;
        while (true) {
            fromDate = IOUtil.readString("\nEnter the START date of the period in the format \'dd-MM-yyyy\' or \'0\' to return: ");
            if (!fromDate.equals("0")) {
                toDate = IOUtil.readString("\nEnter the END date of the period in the format \'dd-MM-yyyy\' or \'0\' to return: ");
                try {
                    if (!toDate.equals("0")) {
                        LocalDate startDate = LocalDate.parse(fromDate, formatter);
                        LocalDate endDate = LocalDate.parse(toDate, formatter);
                        dates.add(startDate);
                        dates.add(endDate);
                        return dates;
                    } else return dates;
                } catch (DateTimeParseException e) {
                    System.out.println("The entered date is incorrect. \n" +
                            "Please enter the date in the correct format.");
                }
            } else {
                return dates;
            }
        }
    }
}
