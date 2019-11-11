package com.abseliamov.cinemaservice.controller;

import com.abseliamov.cinemaservice.model.Movie;
import com.abseliamov.cinemaservice.model.Seat;
import com.abseliamov.cinemaservice.model.Ticket;
import com.abseliamov.cinemaservice.model.Viewer;
import com.abseliamov.cinemaservice.model.enums.TicketStatus;
import com.abseliamov.cinemaservice.service.TicketService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class TicketController {
    private TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public Ticket getById(long ticketId) {
        return ticketService.getById(ticketId);
    }

    public List<Ticket> getTicketByMovieTitle(String movieTitle) {
        return ticketService.getTicketByMovieTitle(movieTitle);
    }

    public List<Ticket> getTicketByGenre(long genreId) {
        return ticketService.getTicketByGenre(genreId);
    }

    public List<Ticket> getTicketBySeatType(long seatTypeId) {
        return ticketService.getTicketBySeatType(seatTypeId);
    }

    public boolean buyTicket(long ticketId) {
        return ticketService.buyTicket(ticketId);
    }

    public boolean returnTicket(long ticketId) {
        return ticketService.returnTicket(ticketId);
    }

    public List<Ticket> getAllViewerActualTicket() {
        return ticketService.getAllViewerActualTicket();
    }

    public Ticket getOrderedTicketById(long ticketId) {
        return ticketService.getOrderedTicketById(ticketId);
    }

    public List<Ticket> getAllViewerTicket() {
        return ticketService.getAllViewerTicket();
    }

    public void searchMostProfitableMovie() {
        ticketService.searchMostProfitableMovie();
    }

    public void searchLeastProfitableMovie() {
        ticketService.searchLeastProfitableMovie();
    }

    public void searchViewerMovieCountByGenre(long genreId) {
        ticketService.searchViewerMovieCountByGenre(genreId);
    }

    public void searchViewersVisitingMovieInIntervalDaysFromBirthday() {
        ticketService.searchViewersVisitingMovieInIntervalDaysFromBirthday();
    }

    public void searchViewerByComplexQuery(long genreId, double amount, List<LocalDate> dates) {
        ticketService.searchViewerByComplexQuery(genreId, amount, dates);
    }

    public Map<LocalDate, Long> printAllActiveTicketDate() {
        return ticketService.printAllActiveTicketDate();
    }

    public List<Ticket> getAllTicketByDate(long dateId, Map<LocalDate, Long> dateMap) {
        return ticketService.getAllTicketByDate(dateId, dateMap);
    }

    public boolean checkTicketAvailable(long ticketId) {
        return ticketService.checkTicketAvailable(ticketId);
    }

    public Ticket getByIdAdmin(long ticketId) {
        return ticketService.getByIdAdmin(ticketId);
    }

    public boolean createTicket(Movie movie, Seat seat, double price, LocalDateTime dateTime) {
        return ticketService.createTicket(movie, seat, price, dateTime);
    }

    public List<Ticket> getAll() {
        return ticketService.getAll();
    }

    public List<Ticket> printAllTicket() {
        return ticketService.printAllTicket();
    }

    public void updateTicket(long ticketId, LocalDateTime dateTime, Movie movie, Seat seat,
                             double price, TicketStatus status, Viewer viewer) {
        ticketService.update(ticketId, dateTime, movie, seat, price, status, viewer);
    }

    public void deleteTicket(long ticketId) {
        ticketService.delete(ticketId);
    }

    public List<TicketStatus> printAllTicketStatus() {
        return ticketService.printAllTicketStatus();
    }
}
