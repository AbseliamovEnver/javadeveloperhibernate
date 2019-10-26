package com.abseliamov.cinemaservice.controller;

import com.abseliamov.cinemaservice.model.Movie;
import com.abseliamov.cinemaservice.model.Seat;
import com.abseliamov.cinemaservice.model.Ticket;
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

    public List<Ticket> getTicketByMovieTitle(String movieTitle) {
        return ticketService.getTicketByMovieTitle(movieTitle);
    }

    public List<Ticket> getTicketByGenre(long genreId) {
        return ticketService.getTicketByGenre(genreId);
    }

    public List<Ticket> getTicketBySeatType(long seatTypeId) {
        return ticketService.getTicketBySeatType(seatTypeId);
    }

    public Ticket getById(long ticketId) {
        return ticketService.getById(ticketId);
    }

    public boolean buyTicket(long ticketId) {
        return ticketService.buyTicket(ticketId);
    }

    public List<Ticket> getAllTicketViewer() {
        return ticketService.getAllTicketViewer();
    }

    public boolean returnTicket(long ticketId) {
        return ticketService.returnTicket(ticketId);
    }

    public List<Ticket> getAllTicketByViewerId(long viewerId) {
        return ticketService.getAllTicketByViewerId(viewerId);
    }

    public Ticket getOrderedTicketById(long ticketId) {
        return ticketService.getOrderedTicketById(ticketId);
    }

    public Map<LocalDate, Long> getAllDate() {
        return ticketService.getAllDate();
    }

    public List<Ticket> getAllTicketByDate(long dateId) {
        return ticketService.getAllTicketByDate(dateId);
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

    public List<Ticket> getAllTicket() {
        return ticketService.getAllTicket();
    }

    public List<Ticket> getAllTicketWithStatus() {
        return ticketService.getAllTicketWithStatus();
    }

    public void updateTicket(long ticketId, Movie movie, Seat seat, long buyStatus, double price,
                             LocalDateTime dateTime) {
        ticketService.update(ticketId, movie, seat, buyStatus, price, dateTime);
    }

    public void deleteTicket(long ticketId) {
        ticketService.delete(ticketId);
    }
}
