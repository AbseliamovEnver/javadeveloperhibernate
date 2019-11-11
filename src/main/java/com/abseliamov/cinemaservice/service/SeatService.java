package com.abseliamov.cinemaservice.service;

import com.abseliamov.cinemaservice.dao.SeatDaoImpl;
import com.abseliamov.cinemaservice.model.Seat;
import com.abseliamov.cinemaservice.model.enums.SeatTypes;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SeatService {
    private SeatDaoImpl seatDao;

    public SeatService(SeatDaoImpl seatDao) {
        this.seatDao = seatDao;
    }

    public void createSeat(long seatNumber, SeatTypes seatType) {
        List<Seat> seats = seatDao.getAll();
        Seat seat = seats.stream()
                .filter(seatItem -> seatItem.getNumber() == seatNumber &&
                        seatItem.getSeatTypes().getId() == seatType.getId())
                .findFirst()
                .orElse(null);
        if (seat == null) {
            seatDao.add(new Seat(0, seatType, seatNumber));
            System.out.println("\nSeat with number \'" + seatNumber + "\' " +
                    "and type \'" + seatType.name() + "\' successfully added.");
        } else {
            System.out.println("\nSuch seat already exists.");
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
                System.out.println("\nUpdate successfully.");
            }
        } else {
            System.out.println("\nSeat with number \'" + seatNumber + "\' " +
                    "and type " + seat.getSeatTypes() + " already exists.");
        }
    }

    public void delete(long seatId) {
        if (seatDao.delete(seatId)) {
            System.out.println("Seat with id \'" + seatId + "\' deleted.");
        }
    }

    public SeatTypes getSeatTypeById(long seatTypeId) {
        List<SeatTypes> seatTypes = Arrays.asList(SeatTypes.values());
        return seatTypes.stream().
                filter(seatTypesItem -> seatTypesItem.getId() == seatTypeId)
                .findFirst()
                .orElse(null);
    }

    private void printSeats(List<Seat> seats) {
        if (seats.size() != 0) {
            System.out.println("\n|--------------------------|");
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

    public Set<SeatTypes> printAllSeatType() {
        List<Seat> seats = seatDao.getAll();
        Set<SeatTypes> seatTypes = null;
        if (seats.size() != 0) {
            System.out.println("\n|--------------------|");
            System.out.printf("%-3s%-1s\n", " ", "LIST SEAT TYPES");
            System.out.println("|--------------------|");
            System.out.printf("%-3s%-9s%-1s\n%-1s\n", " ", "ID", "TYPE",
                    "|------|-------------|");
            seatTypes = seats.stream().map(Seat::getSeatTypes).collect(Collectors.toSet());
            seatTypes.forEach(seatType -> System.out.printf("%-3s%-6s%-1s\n%-1s\n",
                    " ", seatType.getId(), seatType,
                    "|------|-------------|"));
        } else {
            System.out.println("List seats is empty.");
        }
        return seatTypes;
    }
}
