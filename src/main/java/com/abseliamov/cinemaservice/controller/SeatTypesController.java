package com.abseliamov.cinemaservice.controller;

import com.abseliamov.cinemaservice.model.SeatTypes;
import com.abseliamov.cinemaservice.service.SeatTypesService;

import java.util.List;

public class SeatTypesController {
    private SeatTypesService seatTypesService;

    public SeatTypesController(SeatTypesService seatTypesService) {
        this.seatTypesService = seatTypesService;
    }

    public List<SeatTypes> getAllSeatType() {
        return seatTypesService.getAllSeatType();
    }

    public SeatTypes getById(long seatTypeId){
        return seatTypesService.getById(seatTypeId);
    }
}
