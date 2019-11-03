package com.abseliamov.cinemaservice.model.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SeatTypesConverter implements AttributeConverter<SeatTypes, Long> {
    @Override
    public Long convertToDatabaseColumn(SeatTypes seatTypes) {
        return seatTypes.getId();
    }

    @Override
    public SeatTypes convertToEntityAttribute(Long value) {
        return SeatTypes.getSeatTypes(value);
    }
}
