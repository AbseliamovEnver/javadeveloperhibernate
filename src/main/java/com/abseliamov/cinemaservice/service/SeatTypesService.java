package com.abseliamov.cinemaservice.service;

import com.abseliamov.cinemaservice.dao.SeatTypesDao;
import com.abseliamov.cinemaservice.model.enums.SeatTypes;

public class SeatTypesService {
    private SeatTypesDao seatTypesDao;

    public SeatTypesService(SeatTypesDao seatTypesDao) {
        this.seatTypesDao = seatTypesDao;
    }

    public SeatTypes getById(long seatTypeId) {
        return seatTypesDao.getById(seatTypeId);
    }
}
