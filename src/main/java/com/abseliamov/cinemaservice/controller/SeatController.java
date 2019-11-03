package com.abseliamov.cinemaservice.controller;

import com.abseliamov.cinemaservice.model.Seat;
import com.abseliamov.cinemaservice.model.enums.SeatTypes;
import com.abseliamov.cinemaservice.service.SeatService;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SeatController {
    private SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    public void createSeat(long seatNumber, SeatTypes seatType) {
        seatService.createSeat(seatNumber, seatType);
    }

    public Seat getById(long seatId) {
        return seatService.getById(seatId);
    }

    public List<Seat> getAll() {
        return seatService.getAll();
    }

    public List<Seat> getAllSeatType() {
        return seatService.getAllSeatType();
    }

    public void updateSeat(long seatId, SeatTypes seatType, long seatNumber) {
        seatService.update(seatId, seatType, seatNumber);
    }

    public void deleteSeat(long seatId) {
        seatService.delete(seatId);
    }

    public Set<SeatTypes> printAllSeatType() {
        return seatService.printAllSeatType();
    }
}
