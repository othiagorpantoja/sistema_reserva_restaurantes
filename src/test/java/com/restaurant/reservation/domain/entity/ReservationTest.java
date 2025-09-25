package com.restaurant.reservation.domain.entity;

import com.restaurant.reservation.domain.event.ReservationConfirmedEvent;
import com.restaurant.reservation.domain.event.ReservationCancelledEvent;
import com.restaurant.reservation.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a entidade Reservation.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@DisplayName("Reservation Entity Tests")
class ReservationTest {
    
    private Reservation reservation;
    private ReservationId reservationId;
    private TableId tableId;
    private CustomerInfo customerInfo;
    private ReservationTime reservationTime;
    
    @BeforeEach
    void setUp() {
        reservationId = ReservationId.generate();
        tableId = TableId.of("T001");
        customerInfo = CustomerInfo.of("João Silva", "joao@email.com", "(11) 99999-9999");
        reservationTime = ReservationTime.of(LocalDateTime.now().plusHours(1));
        
        reservation = Reservation.builder()
            .id(reservationId)
            .tableId(tableId)
            .customerInfo(customerInfo)
            .reservationTime(reservationTime)
            .status(ReservationStatus.PENDING)
            .build();
    }
    
    @Test
    @DisplayName("Should create reservation with valid data")
    void shouldCreateReservationWithValidData() {
        // Given & When
        Reservation newReservation = Reservation.builder()
            .id(reservationId)
            .tableId(tableId)
            .customerInfo(customerInfo)
            .reservationTime(reservationTime)
            .status(ReservationStatus.PENDING)
            .build();
        
        // Then
        assertNotNull(newReservation);
        assertEquals(reservationId, newReservation.getId());
        assertEquals(tableId, newReservation.getTableId());
        assertEquals(customerInfo, newReservation.getCustomerInfo());
        assertEquals(reservationTime, newReservation.getReservationTime());
        assertEquals(ReservationStatus.PENDING, newReservation.getStatus());
        assertTrue(newReservation.getDomainEvents().isEmpty());
    }
    
    @Test
    @DisplayName("Should confirm pending reservation")
    void shouldConfirmPendingReservation() {
        // Given
        assertEquals(ReservationStatus.PENDING, reservation.getStatus());
        
        // When
        reservation.confirm();
        
        // Then
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
        assertEquals(1, reservation.getDomainEvents().size());
        assertTrue(reservation.getDomainEvents().get(0) instanceof ReservationConfirmedEvent);
    }
    
    @Test
    @DisplayName("Should not confirm already confirmed reservation")
    void shouldNotConfirmAlreadyConfirmedReservation() {
        // Given
        reservation.confirm();
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
        
        // When & Then
        assertThrows(IllegalStateException.class, () -> reservation.confirm());
    }
    
    @Test
    @DisplayName("Should cancel pending reservation")
    void shouldCancelPendingReservation() {
        // Given
        assertEquals(ReservationStatus.PENDING, reservation.getStatus());
        
        // When
        reservation.cancel();
        
        // Then
        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
        assertEquals(1, reservation.getDomainEvents().size());
        assertTrue(reservation.getDomainEvents().get(0) instanceof ReservationCancelledEvent);
    }
    
    @Test
    @DisplayName("Should cancel confirmed reservation")
    void shouldCancelConfirmedReservation() {
        // Given
        reservation.confirm();
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
        
        // When
        reservation.cancel();
        
        // Then
        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
        assertEquals(2, reservation.getDomainEvents().size());
    }
    
    @Test
    @DisplayName("Should complete confirmed reservation")
    void shouldCompleteConfirmedReservation() {
        // Given
        reservation.confirm();
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
        
        // When
        reservation.complete();
        
        // Then
        assertEquals(ReservationStatus.COMPLETED, reservation.getStatus());
        assertEquals(2, reservation.getDomainEvents().size());
    }
    
    @Test
    @DisplayName("Should not complete pending reservation")
    void shouldNotCompletePendingReservation() {
        // Given
        assertEquals(ReservationStatus.PENDING, reservation.getStatus());
        
        // When & Then
        assertThrows(IllegalStateException.class, () -> reservation.complete());
    }
    
    @Test
    @DisplayName("Should check if reservation can be modified")
    void shouldCheckIfReservationCanBeModified() {
        // Given & When & Then
        assertTrue(reservation.canBeModified());
        
        reservation.confirm();
        assertTrue(reservation.canBeModified());
        
        reservation.cancel();
        assertFalse(reservation.canBeModified());
    }
    
    @Test
    @DisplayName("Should modify reservation when allowed")
    void shouldModifyReservationWhenAllowed() {
        // Given
        TableId newTableId = TableId.of("T002");
        ReservationTime newTime = ReservationTime.of(LocalDateTime.now().plusHours(2));
        
        // When
        reservation.modifyReservation(newTableId, newTime);
        
        // Then
        assertEquals(newTableId, reservation.getTableId());
        assertEquals(newTime, reservation.getReservationTime());
        assertEquals(1, reservation.getDomainEvents().size());
    }
    
    @Test
    @DisplayName("Should not modify cancelled reservation")
    void shouldNotModifyCancelledReservation() {
        // Given
        reservation.cancel();
        TableId newTableId = TableId.of("T002");
        ReservationTime newTime = ReservationTime.of(LocalDateTime.now().plusHours(2));
        
        // When & Then
        assertThrows(IllegalStateException.class, 
            () -> reservation.modifyReservation(newTableId, newTime));
    }
    
    @Test
    @DisplayName("Should clear domain events")
    void shouldClearDomainEvents() {
        // Given
        reservation.confirm();
        assertEquals(1, reservation.getDomainEvents().size());
        
        // When
        reservation.clearDomainEvents();
        
        // Then
        assertTrue(reservation.getDomainEvents().isEmpty());
    }
    
    @Test
    @DisplayName("Should validate reservation with null ID")
    void shouldValidateReservationWithNullId() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            Reservation.builder()
                .id(null)
                .tableId(tableId)
                .customerInfo(customerInfo)
                .reservationTime(reservationTime)
                .build());
    }
    
    @Test
    @DisplayName("Should validate reservation with past time")
    void shouldValidateReservationWithPastTime() {
        // Given
        ReservationTime pastTime = ReservationTime.of(LocalDateTime.now().minusHours(1));
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            Reservation.builder()
                .id(reservationId)
                .tableId(tableId)
                .customerInfo(customerInfo)
                .reservationTime(pastTime)
                .build());
    }
}
