package com.abseliamov.cinemaservice.model;

public class Seat extends GenericModel {
    private SeatTypes seatTypes;
    private long number;

    public Seat(long id, SeatTypes seatTypes, long number) {
        super(id);
        this.seatTypes = seatTypes;
        this.number = number;
    }

    public SeatTypes getSeatTypes() {
        return seatTypes;
    }

    public void setSeatTypes(SeatTypes seatTypes) {
        this.seatTypes = seatTypes;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Seat seat = (Seat) o;

        if (number != seat.number) return false;
        return seatTypes == seat.seatTypes;

    }

    @Override
    public int hashCode() {
        int result = seatTypes != null ? seatTypes.hashCode() : 0;
        result = 31 * result + (int) (number ^ (number >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return String.format("%-2s%-8s%-1s\n%-1s", " ", getId(), getSeatTypes(), "|-------|------------|");
    }
}
