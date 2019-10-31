package com.abseliamov.cinemaservice.service;

import com.abseliamov.cinemaservice.dao.TicketDaoImpl;
import com.abseliamov.cinemaservice.dao.ViewerDaoImpl;
import com.abseliamov.cinemaservice.model.Movie;
import com.abseliamov.cinemaservice.model.Seat;
import com.abseliamov.cinemaservice.model.Ticket;
import com.abseliamov.cinemaservice.model.Viewer;
import com.abseliamov.cinemaservice.utils.CurrentViewer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TicketService {
    private TicketDaoImpl ticketDao;
    private ViewerDaoImpl viewerDao;
    private CurrentViewer currentViewer;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private DateTimeFormatter weekdayFormatter = DateTimeFormatter.ofPattern("EEEE").withLocale(Locale.ENGLISH);

    public TicketService(TicketDaoImpl ticketDao, ViewerDaoImpl viewerDao, CurrentViewer currentViewer) {
        this.ticketDao = ticketDao;
        this.viewerDao = viewerDao;
        this.currentViewer = currentViewer;
    }

    public boolean createTicket(Movie movie, Seat seat, double price, LocalDateTime dateTime) {
//        Ticket newTicket = new Ticket(0, dateTime, movie, seat, price, 0);
//        List<Ticket> tickets = ticketDao.getAll();
//        Ticket ticket = tickets.stream()
//                .filter(ticketItem -> ticketItem.getDateTime().equals(dateTime) &&
//                        ticketItem.getMovie().equals(movie) &&
//                        ticketItem.getSeat().equals(seat))
//                .findFirst()
//                .orElse(null);
//        if (ticket == null) {
//            ticketDao.add(newTicket);
//            System.out.println("Ticket successfully added.");
//            return true;
//        } else {
//            System.out.println("Such ticket already exists.");
//        }
        return false;
    }

    public List<Ticket> getTicketByMovieTitle(String movieTitle) {
        List<Ticket> ticketList = ticketDao.getTicketByMovieTitle(movieTitle);
        printTicket(ticketList);
        return ticketList;
    }

    public List<Ticket> getTicketByGenre(long genreId) {
        List<Ticket> ticketList = ticketDao.getTicketByGenre(genreId);
        printTicket(ticketList);
        return ticketList;
    }

    public List<Ticket> getTicketBySeatType(long seatTypeId) {
        List<Ticket> ticketList = ticketDao.getTicketBySeatType(seatTypeId);
        printTicket(ticketList);
        return ticketList;
    }

    public Ticket getById(long ticketId) {
        Ticket ticket = ticketDao.getById(ticketId);
//        if (ticket.getStatus() == 0) {
//            List<Ticket> list = Arrays.asList(ticket);
//            printTicket(list);
//        } else ticket = null;
        return ticket;
    }

    public Ticket getByIdAdmin(long ticketId) {
        return ticketDao.getById(ticketId);
    }

    public boolean buyTicket(long ticketId) {
        Ticket ticket = ticketDao.getById(ticketId);
        return ticketDao.buyTicket(ticket);
    }

    public List<Ticket> getAllTicketViewer() {
        List<Ticket> ticketList = ticketDao.getAllTicketViewer();
        printTicket(ticketList);
        return ticketList;
    }

    public Ticket getOrderedTicketById(long ticketId) {
        Ticket ticket = ticketDao.getOrderedTicketById(ticketId);
        List<Ticket> list = Arrays.asList(ticket);
        printTicket(list);
        return ticket;
    }

    public boolean returnTicket(long ticketId) {
        Ticket ticket = ticketDao.getOrderedTicketById(ticketId);
        return ticketDao.returnTicket(ticket);
    }

    public List<Ticket> getAllTicketByViewerId(long viewerId) {
        List<Ticket> ticketList = ticketDao.getAllTicketByViewerId(viewerId);
        Viewer viewer = viewerDao.getById(viewerId);
        if (ticketList.size() != 0) {
            System.out.println("List of tickets of a viewer with a name \'" + viewer.getName() + "\'.");
            printTicket(ticketList);
        } else {
            System.out.println("List of tickets of a viewer with a name \'" + viewer.getName() + "\' is empty.");
        }
        return ticketList;
    }

    public Map<LocalDate, Long> getAllDate() {
        Map<LocalDate, Long> dateMap = new TreeMap<>();
        List<Ticket> ticketList = ticketDao.getAllDate();
        if (ticketList != null) {
            for (Ticket ticket : ticketList) {
                dateMap.put(ticket.getDateTime().toLocalDate(), ticket.getId());
            }
            printAllDate(dateMap);
        } else {
            System.out.println("Date list is empty.");
        }
        return dateMap;
    }

    public List<Ticket> getAllTicketByDate(long ticketId) {
        List<Ticket> result = null;
        LocalDate date;
        List<Ticket> ticketList = ticketDao.getAll();
        Ticket ticket = ticketDao.getById(ticketId);
        if (ticketList != null && ticket != null) {
            date = ticket.getDateTime().toLocalDate();
            result = ticketList.stream()
                    .filter(ticketItem -> ticketItem.getDateTime().toLocalDate().equals(date))
//                    .filter(ticketItem -> ticketItem.getStatus() == 0)
                    .collect(Collectors.toList());
            printTicket(result);
        } else {
            System.out.println("");
        }
        return result;
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
//        List<Ticket> tickets = ticketDao.getAll();
//        Ticket updateTicket = new Ticket(ticketId, dateTime, movie, seat, price, buyStatus);
//        Ticket ticket = tickets.stream()
//                .filter(ticketItem -> ticketItem.getDateTime().equals(dateTime) &&
//                        ticketItem.getMovie().equals(movie) &&
//                        ticketItem.getSeat().equals(seat))
//                .findFirst()
//                .orElse(null);
//        if (ticket == null && ticketDao.update(ticketId, updateTicket)) {
//            System.out.println("Ticket with id \'" + ticketId + "\' updated successfully.");
//        }
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
        boolean ticketAvailable = false;
//        Ticket ticket = ticketDao.getById(ticketId);
//        if (ticket.getStatus() == 0) {
//            ticketAvailable = true;
//        } else {
//            System.out.println("Ticket with id \'" + ticketId + "\' not available.\n" +
//                    "Please try again.");
//        }
        return ticketAvailable;
    }

    private void printTicket(List<Ticket> ticketList) {
        if (!ticketList.isEmpty()) {
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
        if (!tickets.isEmpty()) {
            System.out.println("\n|--------------------------------------------------------------------" +
                    "-----------------------------------------------------------|");
            System.out.printf("%-55s%-1s\n", " ", "LIST OF TICKETS");
            System.out.println("|----------------------------------------------------------------------" +
                    "---------------------------------------------------------|");
            System.out.printf("%-3s%-15s%-29s%-17s%-12s%-9s%-12s%-15s%-9s%-1s\n",
                    " ", "ID", "MOVIE TITLE", "GENRE", "DATE", "TIME", "SEAT TYPE", "SEAT NUMBER", "PRICE", "STATUS");
            System.out.println("|-------|------------------------------|-------------------|------------|----------" +
                    "|-----------|-------------|---------|--------|");
            tickets.stream()
                    .sorted(Comparator.comparing(Ticket::getId))
                    .collect(Collectors.toList())
                    .forEach(ticket -> System.out.printf("%-2s%-8s%-31s%-20s%-13s%-11s%-16s%-11s%-11s%-1s\n%1s\n",
                            " ", ticket.getId(), ticket.getMovie().getName(), ticket.getMovie().getGenres(),
                            ticket.getDateTime().toLocalDate().format(dateFormatter),
                            ticket.getDateTime().toLocalTime().format(timeFormatter),
                            ticket.getSeat().getSeatTypes(), ticket.getSeat().getNumber(),
//                            ticket.getPrice(), ticket.getStatus(),
                            "|-------|------------------------------|-------------------|------------|----------" +
                                    "|-----------|-------------|---------|--------|"));
        } else {
            System.out.println("List tickets is empty.");
        }
    }
}
