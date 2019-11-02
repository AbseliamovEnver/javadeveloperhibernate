package com.abseliamov.cinemaservice.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TicketStatusConvert implements AttributeConverter<TicketStatus, Long> {
    @Override
    public Long convertToDatabaseColumn(TicketStatus status) {
        return status.getId();
    }

    @Override
    public TicketStatus convertToEntityAttribute(Long value) {
        return TicketStatus.getTicketStatus(value);
    }
}
