package com.abseliamov.cinemaservice.service;

import com.abseliamov.cinemaservice.dao.GenreDaoImpl;
import com.abseliamov.cinemaservice.dao.TicketDaoImpl;
import com.abseliamov.cinemaservice.dao.ViewerDaoImpl;
import com.abseliamov.cinemaservice.model.*;
import com.abseliamov.cinemaservice.model.enums.TicketStatus;
import com.abseliamov.cinemaservice.utils.CurrentViewer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TicketService {
    private static final String REQUEST_IS_EMPTY = "\nON YOUR REQUEST IS NO RESULTS";
    private TicketDaoImpl ticketDao;
    private ViewerDaoImpl viewerDao;
    private GenreDaoImpl genreDao;
    private CurrentViewer currentViewer;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private DateTimeFormatter weekdayFormatter = DateTimeFormatter.ofPattern("EEEE").withLocale(Locale.ENGLISH);

    public TicketService(TicketDaoImpl ticketDao, ViewerDaoImpl viewerDao, GenreDaoImpl genreDao,
                         CurrentViewer currentViewer) {
        this.ticketDao = ticketDao;
        this.viewerDao = viewerDao;
        this.genreDao = genreDao;
        this.currentViewer = currentViewer;
    }

    public boolean createTicket(Movie movie, Seat seat, double price, LocalDateTime dateTime) {
        Ticket newTicket = new Ticket(0, dateTime, movie, seat, price, TicketStatus.ACTIVE, viewerDao.getById(1));
        List<Ticket> tickets = ticketDao.getAll();
        Ticket ticket = tickets.stream()
                .filter(ticketItem -> ticketItem.getDateTime().equals(dateTime) &&
                        ticketItem.getMovie().equals(movie) &&
                        ticketItem.getSeat().equals(seat) &&
                        ticketItem.getStatus() == TicketStatus.ACTIVE)
                .findFirst()
                .orElse(null);
        if (ticket == null) {
            ticketDao.add(newTicket);
            System.out.println("\nTicket successfully added.");
            return true;
        } else {
            System.out.println("\nSuch ticket already exists.");
        }
        return false;
    }

    public List<Ticket> getTicketByMovieTitle(String movieTitle) {
        List<Ticket> tickets = new ArrayList<>();
        List<Ticket> ticketList = ticketDao.getAll();
        if (ticketList != null) {
            tickets = ticketList.stream()
                    .filter(ticket -> ticket.getStatus() == TicketStatus.ACTIVE &&
                            ticket.getMovie().getName().equalsIgnoreCase(movieTitle))
                    .collect(Collectors.toList());
        }
        printTicket(tickets);
        return tickets;
    }

    public List<Ticket> getTicketByGenre(long genreId) {
        List<Ticket> activeTicketsList;
        List<Ticket> result = new ArrayList<>();
        List<Ticket> ticketList = ticketDao.getAll();
        if (ticketList != null) {
            activeTicketsList = ticketList.stream()
                    .filter(ticket -> ticket.getStatus() == TicketStatus.ACTIVE)
                    .collect(Collectors.toList());
            for (Ticket ticket : activeTicketsList) {
                for (Genre genre : ticket.getMovie().getGenres()) {
                    if (genre.getId() == genreId) {
                        result.add(ticket);
                    }
                }
            }
        }
        printTicket(result);
        return result;
    }

    public List<Ticket> getAllTicketByDate(long dateId, Map<LocalDate, Long> dateMap) {
        List<Ticket> result = null;
        List<Ticket> ticketList = ticketDao.getAll();
        LocalDate date;
        date = dateMap.entrySet().stream()
                .filter(dateItem -> dateItem.getValue() == dateId)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        if (ticketList != null && date != null) {
            result = ticketList.stream()
                    .filter(ticketActive -> ticketActive.getStatus() == TicketStatus.ACTIVE)
                    .filter(ticketDate -> ticketDate.getDateTime().toLocalDate().equals(date))
                    .collect(Collectors.toList());
            printTicket(result);
        } else {
            System.out.println("Tickets for this date is not found.");
        }
        return result;
    }

    public List<Ticket> getTicketBySeatType(long seatTypeId) {
        List<Ticket> ticketList = ticketDao.getAll();
        List<Ticket> result = null;
        if (ticketList != null) {
            result = ticketList.stream()
                    .filter(ticket -> ticket.getStatus() == TicketStatus.ACTIVE)
                    .filter(ticket -> ticket.getSeat().getSeatTypes().getId() == seatTypeId)
                    .collect(Collectors.toList());
        }
        if (result != null) {
            printTicket(result);
        }
        return result;
    }

    public Ticket getById(long ticketId) {
        Ticket ticket = ticketDao.getById(ticketId);
//        if (ticket.getStatus() == 0) {
//            List<Ticket> list = Arrays.asList(ticket);
//            printTicket(list);
//        } else ticket = null;
        return ticket;
    }

    public boolean buyTicket(long ticketId) {
        Ticket ticket = ticketDao.getById(ticketId);
        if (ticket != null && ticket.getStatus() == TicketStatus.ACTIVE) {
            return ticketDao.buyTicket(ticket);
        }
        return false;
    }

    public boolean returnTicket(long ticketId) {
        Ticket ticket = ticketDao.getById(ticketId);
        if (ticket != null && ticket.getStatus() == TicketStatus.INACTIVE) {
            return ticketDao.returnTicket(ticket);
        }
        return false;
    }

    public List<Ticket> getAllViewerActualTicket() {
        List<Ticket> tickets = ticketDao.getAll();
        List<Ticket> result = null;
        if (tickets != null) {
            result = tickets.stream()
                    .filter(ticket -> ticket.getViewer().getId() == currentViewer.getViewer().getId())
                    .filter(ticket -> ticket.getDateTime().isAfter(LocalDateTime.now()))
                    .filter(ticket -> ticket.getStatus() == TicketStatus.INACTIVE)
                    .collect(Collectors.toList());
        }
        if (result != null) {
            printTicket(result);
        }
        return result;
    }

    public Ticket getOrderedTicketById(long ticketId) {
        Ticket ticket = ticketDao.getById(ticketId);
        if (ticket != null && ticket.getViewer().getId() == currentViewer.getViewer().getId() &&
                ticket.getStatus() == TicketStatus.INACTIVE && ticket.getDateTime().isAfter(LocalDateTime.now())) {
            printTicket(Collections.singletonList(ticket));
        }
        return ticket;
    }

    public List<Ticket> getAllViewerTicket() {
        List<Ticket> tickets = ticketDao.getAll();
        List<Ticket> result = null;
        if (tickets != null) {
            result = tickets.stream()
                    .filter(ticket -> ticket.getViewer().getId() == currentViewer.getViewer().getId())
                    .collect(Collectors.toList());
        }
        if (result != null) {
            printTicketWithStatus(result);
        }
        return result;
    }

    public void searchMostProfitableMovie() {
        List result = ticketDao.searchMostProfitableMovie();
        Ticket ticket = (Ticket) result.get(0);
        if (ticket != null) {
            printMovieByRequest(ticket, "THE MOST PROFITABLE FILM FOR THE LAST QUARTER");
        } else {
            System.out.println(REQUEST_IS_EMPTY);
        }
    }

    public void searchLeastProfitableMovie() {
        List result = ticketDao.searchLeastProfitableMovie();
        Ticket ticket = (Ticket) result.get(0);
        if (ticket != null) {
            printMovieByRequest(ticket, "THE LEAST PROFITABLE FILM FOR THE LAST QUARTER");
        } else {
            System.out.println(REQUEST_IS_EMPTY);
        }
    }

    public void searchViewerMovieCountByGenre(long genreId) {
        List result = ticketDao.searchViewerMovieCountByGenre(genreId);
        List<Ticket> tickets = (List<Ticket>) result;
        if (tickets.size() != 0) {
            printViewerByRequest(tickets, "LIST OF VIEWERS BUYING MORE THAN 10 TICKETS FOR THE GENRE: \'" +
                    genreDao.getById(genreId).getName().toUpperCase() + "\'");
        } else {
            System.out.println(REQUEST_IS_EMPTY);
        }
    }

    public void searchViewersVisitingMovieInIntervalDaysFromBirthday() {
        List result = ticketDao.searchViewersVisitingMovieInIntervalDaysFromBirthday();
        List<Ticket> tickets = (List<Ticket>) result;
        if (tickets.size() != 0) {
            printViewerByRequest(tickets, "\t\tLIST OF VIEWERS BUYING A TICKET +-3 DAYS FROM THEIR BIRTHDAY");
        } else {
            System.out.println(REQUEST_IS_EMPTY);
        }
    }

    public void searchViewerByComplexQuery(long genreId, double amount, List<LocalDate> dates) {
        List result = ticketDao.searchViewerByComplexQuery(genreId, amount, dates);
        List<Ticket> tickets = (List<Ticket>) result;
        if (tickets.size() != 0) {
            printViewerByRequest(tickets, "\t\t\tLIST OF VIEWERS BY COMPLEX REQUEST");
        } else {
            System.out.println(REQUEST_IS_EMPTY);
        }
    }


    public Ticket getByIdAdmin(long ticketId) {
        return ticketDao.getById(ticketId);
    }

    public List<Ticket> getAllTicketByViewerId(long viewerId) {
        List<Ticket> ticketList = ticketDao.getAllTicketByViewerId(viewerId);
        Viewer viewer = viewerDao.getById(viewerId);
        if (ticketList.size() != 0) {
            System.out.println("List of tickets of a viewer with a name \'" + viewer.getFirstName() + "\'.");
            printTicket(ticketList);
        } else {
            System.out.println("List of tickets of a viewer with a name \'" + viewer.getFirstName() + "\' is empty.");
        }
        return ticketList;
    }

    public Map<LocalDate, Long> printAllActiveTicketDate() {
        Map<LocalDate, Long> dateMap = new TreeMap<>();
        List<Ticket> ticketList = ticketDao.getAll();
        if (ticketList != null) {
            ticketList.stream()
                    .filter(ticket -> ticket.getStatus() == TicketStatus.ACTIVE)
                    .forEach(ticket -> dateMap.put(ticket.getDateTime().toLocalDate(), ticket.getId()));
            printAllDate(dateMap);
        } else {
            System.out.println("Date list is empty.");
        }
        return dateMap;
    }

    public List<Ticket> getAll() {
        return ticketDao.getAll();
    }

    public List<Ticket> getAllTicket() {
        List<Ticket> tickets = ticketDao.getAll();
        printTicket(tickets);
        return tickets;
    }

    public List<Ticket> getAllTicketWithStatus() {
        List<Ticket> tickets = ticketDao.getAll();
        printTicketWithStatus(tickets);
        return tickets;
    }

    public void update(long ticketId, Movie movie, Seat seat, long buyStatus, double price, LocalDateTime dateTime) {

    }

    public void delete(long ticketId) {
        if (ticketDao.delete(ticketId)) {
            System.out.println("Ticket with id \'" + ticketId + "\' deleted.");
        }
    }

    private void printAllDate(Map<LocalDate, Long> dateList) {
        System.out.println("\n|------------------------------------|");
        System.out.printf("%-13s%-1s\n", " ", "LIST OF DATE");
        System.out.println("|------------------------------------|");
        System.out.printf("%-3s%-11s%-12s%-1s\n%-1s\n", " ", "ID", "DATE", "WEEKDAY",
                "|-------|-------------|--------------|");
        dateList.forEach((date, ticketId) -> System.out.printf("%-2s%-8s%-15s%-1s\n%-1s\n",
                " ", ticketId, date.format(dateFormatter), date.format(weekdayFormatter).toUpperCase(),
                "|-------|-------------|--------------|"));
    }

    public boolean checkTicketAvailable(long ticketId) {
        List<Ticket> tickets = new ArrayList<>();
        boolean ticketAvailable = false;
        Ticket ticket = ticketDao.getById(ticketId);
        if (ticket != null) {
            if (ticket.getDateTime().isAfter(LocalDateTime.now()) &&
                    ticket.getStatus() == TicketStatus.ACTIVE) {
                tickets.add(ticket);
                printTicket(tickets);
                ticketAvailable = true;
            } else {
                System.out.println("Ticket with id \'" + ticketId + "\' has already been sold.");
            }
        } else {
            System.out.println("Ticket with id \'" + ticketId + "\' not available.\n" +
                    "Please try again.");
        }
        return ticketAvailable;
    }

    private void printTicket(List<Ticket> ticketList) {
        if (ticketList != null) {
            System.out.println("\n|--------------------------------------------------------------------" +
                    "--------------------------------------------------|");
            System.out.printf("%-55s%-1s\n", " ", "LIST OF TICKETS");
            System.out.println("|----------------------------------------------------------------------" +
                    "------------------------------------------------|");
            System.out.printf("%-3s%-15s%-29s%-17s%-12s%-9s%-12s%-15s%-1s\n",
                    " ", "ID", "MOVIE TITLE", "GENRE", "DATE", "TIME", "SEAT TYPE", "SEAT NUMBER", "PRICE");
            System.out.println("|-------|------------------------------|-------------------|------------|----------" +
                    "|-----------|-------------|---------|");
            ticketList.stream()
                    .sorted(Comparator.comparing(Ticket::getId))
                    .collect(Collectors.toList())
                    .forEach(System.out::println);
        } else {
            System.out.println("At your request tickets available is not found");
        }
    }

    private void printTicketWithStatus(List<Ticket> tickets) {
        int firstGenre = 0;
        if (tickets != null) {
            System.out.println("\n|--------------------------------------------------------------------" +
                    "--------------------------------------------------------------|");
            System.out.printf("%-55s%-1s\n", " ", "LIST OF TICKETS");
            System.out.println("|----------------------------------------------------------------------" +
                    "------------------------------------------------------------|");
            System.out.printf("%-3s%-15s%-29s%-17s%-12s%-9s%-12s%-15s%-10s%-1s\n",
                    " ", "ID", "MOVIE TITLE", "GENRE", "DATE", "TIME", "SEAT TYPE", "SEAT NUMBER", "PRICE", "STATUS");
            System.out.println("|-------|------------------------------|-------------------|------------|----------" +
                    "|-----------|-------------|---------|-----------|");
            for (Ticket ticket : tickets) {
                if (ticket.getMovie().getGenres().size() > 1) {
                    System.out.printf("%-2s%-8s%-31s%-20s%-13s%-11s%-16s%-11s%-9s%-1s\n",
                            " ", ticket.getId(), ticket.getMovie().getName(),
                            ticket.getMovie().getGenres().get(0).getName(),
                            ticket.getDateTime().toLocalDate().format(dateFormatter),
                            ticket.getDateTime().toLocalTime().format(timeFormatter),
                            ticket.getSeat().getSeatTypes(), ticket.getSeat().getNumber(),
                            ticket.getPrice(), ticket.getStatus());
                    for (Genre genre : ticket.getMovie().getGenres()) {
                        if (firstGenre == 0) {
                            firstGenre = 1;
                            continue;
                        }
                        System.out.printf("%-41s%-1s\n",
                                " ", genre.getName());
                    }
                    System.out.println("|-------|------------------------------|-------------------|------------|" +
                            "----------|-----------|-------------|---------|-----------|");
                } else {
                    System.out.printf("%-2s%-8s%-31s%-20s%-13s%-11s%-16s%-11s%-9s%-1s\n%-1s",
                            " ", ticket.getId(), ticket.getMovie().getName(),
                            ticket.getMovie().getGenres().get(0).getName(),
                            ticket.getDateTime().toLocalDate().format(dateFormatter),
                            ticket.getDateTime().toLocalTime().format(timeFormatter),
                            ticket.getSeat().getSeatTypes(), ticket.getSeat().getNumber(),
                            ticket.getPrice(), ticket.getStatus(),
                            "|-------|------------------------------|-------------------|------------|----------" +
                                    "|-----------|-------------|---------|-----------|");
                }
            }
        } else {
            System.out.println("List tickets is empty.");
        }
    }

    private void printMovieByRequest(Ticket ticket, String request) {
        StringBuilder genres = new StringBuilder();
        int firstGenre = 0;
        if (ticket != null) {
            System.out.println("\n|-----------------------------------------------------------------------------------------|");
            System.out.printf("%-25s%-1s\n", " ", request.toUpperCase());
            System.out.println("|-----------------------------------------------------------------------------------------|");
            System.out.printf("%-2s%-19s%-29s%-14s%-15s%-1s\n",
                    " ", "MOVIE ID", "MOVIE TITLE", "GENRE", "QUARTER COST", "TOTAL COST");
            System.out.println("|----------|------------------------------|-------------------|--------------|------------|");
            if (ticket.getMovie().getGenres().size() > 1) {
                for (Genre genre : ticket.getMovie().getGenres()) {
                    if (firstGenre == 0) {
                        firstGenre = 1;
                        continue;
                    }
                    genres.append(genre.getName());
                    genres.append("\n");
                }
                System.out.printf("%-2s%-11s%-31s%-21s%-16s%-1s\n%-44s%-1s%1s",
                        " ", ticket.getMovie().getId(), ticket.getMovie().getName(),
                        ticket.getMovie().getGenres().get(0).getName(),
                        ticket.getPrice(),
                        ticket.getMovie().getCost(), " ", genres,
                        "|----------|------------------------------|-------------------|--------------|------------|");
            } else {
                System.out.printf("%-2s%-11s%-31s%-21s%-16s%-1s\n%-1s",
                        " ", ticket.getMovie().getId(), ticket.getMovie().getName(),
                        ticket.getMovie().getGenres().get(0).getName(),
                        ticket.getPrice(),
                        ticket.getMovie().getCost(),
                        "|----------|------------------------------|-------------------|--------------|------------|");
            }
        }
    }

    private void printViewerByRequest(List<Ticket> tickets, String request) {
        if (tickets != null) {
            System.out.println("\n|----------------------------------------------------------------------------------|");
            System.out.printf("%-6s%-1s\n", " ", request);
            System.out.println("|----------------------------------------------------------------------------------|");
            System.out.printf("%-3s%-12s%-23s%-21s%-13s%-1s\n",
                    " ", "ID", "FIRST NAME", "LAST NAME", "ROLE", "BIRTHDAY");
            System.out.println("|-------|---------------------|----------------------|--------------|--------------|");
            tickets.forEach(ticket -> System.out.printf("%-2s%-8s%-22s%-23s%-15s%-1s\n%-1s\n",
                    " ", ticket.getViewer().getId(), ticket.getViewer().getFirstName(),
                    ticket.getViewer().getLastName(), ticket.getViewer().getRole().name(),
                    ticket.getViewer().getBirthday(),
                    "|-------|---------------------|----------------------|--------------|--------------|"));
        }
    }
}
