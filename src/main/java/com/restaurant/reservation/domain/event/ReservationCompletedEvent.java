package com.restaurant.reservation.domain.event;

import com.restaurant.reservation.domain.valueobject.CustomerInfo;
import com.restaurant.reservation.domain.valueobject.ReservationId;
import com.restaurant.reservation.domain.valueobject.ReservationTime;
import com.restaurant.reservation.domain.valueobject.TableId;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Evento de domínio disparado quando uma reserva é completada.
 * Indica que o cliente compareceu e utilizou a mesa.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Getter
public class ReservationCompletedEvent implements DomainEvent {
    
    private final String eventId;
    private final LocalDateTime occurredOn;
    private final String eventType;
    private final ReservationId reservationId;
    private final TableId tableId;
    private final CustomerInfo customerInfo;
    private final ReservationTime reservationTime;
    
    public ReservationCompletedEvent(ReservationId reservationId, TableId tableId, 
                                   CustomerInfo customerInfo, ReservationTime reservationTime) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.occurredOn = LocalDateTime.now();
        this.eventType = "ReservationCompleted";
        this.reservationId = reservationId;
        this.tableId = tableId;
        this.customerInfo = customerInfo;
        this.reservationTime = reservationTime;
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
        return String.format("ReservationCompletedEvent{reservationId=%s, tableId=%s, " +
                           "customerName='%s', time=%s}", 
            reservationId, tableId, customerInfo.getName(), reservationTime.getFormattedTime());
    }
}
