package com.abseliamov.cinemaservice.service;

import com.abseliamov.cinemaservice.dao.SeatTypesDao;
import com.abseliamov.cinemaservice.model.enums.SeatTypes;

import java.util.List;

public class SeatTypesService {
    private SeatTypesDao seatTypesDao;

    public SeatTypesService(SeatTypesDao seatTypesDao) {
        this.seatTypesDao = seatTypesDao;
    }

    public List<SeatTypes> getAllSeatType() {
        return seatTypesDao.getAll();
    }

    public SeatTypes getById(long seatTypeId) {
        return seatTypesDao.getById(seatTypeId);
    }
}
