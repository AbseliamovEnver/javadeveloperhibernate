package com.abseliamov.cinemaservice.dao;

import com.abseliamov.cinemaservice.model.SeatTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SeatTypesDao extends AbstractDaoEnum<SeatTypes>{
    private static final Logger logger = LogManager.getLogger(AbstractDao.class);

    public SeatTypesDao(SessionFactory sessionFactory, Class<SeatTypes> clazz) {
        super(sessionFactory, clazz);
    }

    public void printSeatTypes(List<SeatTypes> seatTypesList) {
        if (!seatTypesList.isEmpty()) {
            List<SeatTypes> sortedTypeList = seatTypesList
                    .stream()
                    .sorted(Comparator.comparingLong(SeatTypes::getId))
                    .collect(Collectors.toList());
            System.out.println("\n|--------------------|");
            System.out.printf("%-2s%-1s\n", " ", "LIST OF SEAT TYPES");
            System.out.println("|--------------------|");
            System.out.printf("%-3s%-7s%-1s\n", " ", "ID", "SEAT TYPE");
            System.out.println("|-------|------------|");
            sortedTypeList
                    .forEach(seatTypes -> System.out.printf(String.format("%-2s%-8s%-1s\n%-1s\n",
                            " ", seatTypes.getId(), seatTypes.name(), "|-------|------------|")));
        } else {
            logger.warn("Seat types list is empty.");
            System.out.println("Seat type list is empty.");
        }
    }

    @Override
    public void add(SeatTypes item) {

    }

    @Override
    public boolean update(long id, SeatTypes item) {
        return false;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}
