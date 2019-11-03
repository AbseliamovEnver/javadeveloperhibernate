package com.abseliamov.cinemaservice.service;

import com.abseliamov.cinemaservice.dao.SeatDaoEntityImpl;
import com.abseliamov.cinemaservice.model.Seat;
import com.abseliamov.cinemaservice.model.enums.SeatTypes;

import java.util.List;

public class SeatService {
    private SeatDaoEntityImpl seatDao;

    public SeatService(SeatDaoEntityImpl seatDao) {
        this.seatDao = seatDao;
    }

    public void createSeat(long seatNumber, SeatTypes seatType) {
        List<Seat> seats = seatDao.getAll();
        Seat seat = seats.stream()
                .filter(seatItem ->
                        seatItem.getNumber() == seatNumber && seatItem.getSeatTypes().getId() == seatType.getId())
                .findFirst().orElse(null);
        if (seat == null) {
            seatDao.add(new Seat(0, seatType, seatNumber));
        } else {
            System.out.println("Such seat already exists.");
        }
    }

    public Seat getById(long seatId) {
        return seatDao.getById(seatId);
    }

    public List<Seat> getAll() {
        List<Seat> seats = seatDao.getAll();
        printSeats(seats);
        return seats;
    }

    public List<Seat> getAllSeatType() {
        List<Seat> seatTypes = seatDao.getAll();
        printSeatTypes(seatTypes);
        return seatTypes;
    }

    public void update(long seatId, SeatTypes seatType, long seatNumber) {
        List<Seat> seats = seatDao.getAll();
        Seat seat = seats.stream()
                .filter(seatItem -> seatItem.getNumber() == seatNumber &&
                        seatItem.getSeatTypes().getId() == seatType.getId())
                .findFirst()
                .orElse(null);
        if (seat == null) {
            if (seatDao.update(seatId, new Seat(seatId, seatType, seatNumber))) {
                System.out.println("Update successfully.");
            }
        } else {
            System.out.println("Seat with number \'" + seatNumber + "\' " +
                    "and type " + seat.getSeatTypes() + " already exists.");
        }
    }

    public void delete(long seatId) {
        if (seatDao.delete(seatId)) {
            System.out.println("Seat with id \'" + seatId + "\' deleted.");
        }
    }

    private void printSeats(List<Seat> seats) {
        if (seats.size() != 0) {
            System.out.println("|--------------------------|");
            System.out.printf("%-9s%-1s\n", " ", "LIST SEATS");
            System.out.println("|--------------------------|");
            System.out.printf("%-3s%-6s%-11s%-1s\n%-1s\n", " ", "ID", "NUMBER", "TYPE",
                    "|------|--------|----------|");
            seats.forEach(seat -> System.out.printf("%-3s%-8s%-7s%-1s\n%-1s\n",
                    " ", seat.getId(), seat.getNumber(), seat.getSeatTypes(),
                    "|------|--------|----------|"));
        } else {
            System.out.println("List seats is empty.");
        }
    }

    private void printSeatTypes(List<Seat> seats) {

    }
}
