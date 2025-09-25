package com.restaurant.reservation.domain.event;

import com.restaurant.reservation.domain.valueobject.CustomerInfo;
import com.restaurant.reservation.domain.valueobject.ReservationId;
import com.restaurant.reservation.domain.valueobject.ReservationTime;
import com.restaurant.reservation.domain.valueobject.TableId;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Evento de domínio disparado quando uma reserva é modificada.
 * Contém as novas informações da reserva após a modificação.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Getter
public class ReservationModifiedEvent implements DomainEvent {
    
    private final String eventId;
    private final LocalDateTime occurredOn;
    private final String eventType;
    private final ReservationId reservationId;
    private final TableId newTableId;
    private final CustomerInfo customerInfo;
    private final ReservationTime newReservationTime;
    
    public ReservationModifiedEvent(ReservationId reservationId, TableId newTableId, 
                                 CustomerInfo customerInfo, ReservationTime newReservationTime) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.occurredOn = LocalDateTime.now();
        this.eventType = "ReservationModified";
        this.reservationId = reservationId;
        this.newTableId = newTableId;
        this.customerInfo = customerInfo;
        this.newReservationTime = newReservationTime;
    }
    
    @Override
    public String getEventId() {
        return eventId;
    }
    
    @Override
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public String getEventType() {
        return eventType;
    }
    
    @Override
    public String toString() {
        return String.format("ReservationModifiedEvent{reservationId=%s, newTableId=%s, " +
                           "customerName='%s', newTime=%s}", 
            reservationId, newTableId, customerInfo.getName(), newReservationTime.getFormattedTime());
    }
}
