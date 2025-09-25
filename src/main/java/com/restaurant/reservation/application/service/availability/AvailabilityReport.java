package com.restaurant.reservation.application.service.availability;

import com.restaurant.reservation.domain.entity.Reservation;
import com.restaurant.reservation.domain.valueobject.TableId;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

/**
 * Relatório de disponibilidade de uma mesa em uma data específica.
 * Contém informações sobre reservas existentes e estatísticas.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Getter
@Builder
public class AvailabilityReport {
    
    private final TableId tableId;
    private final LocalDate date;
    private final int totalReservations;
    private final List<Reservation> reservations;
    
    /**
     * Calcula a taxa de ocupação da mesa no dia.
     */
    public double getOccupancyRate() {
        // Assumindo que o restaurante funciona das 11h às 23h (12 horas)
        int totalSlots = 12;
        return (double) totalReservations / totalSlots;
    }
    
    /**
     * Verifica se a mesa está completamente ocupada.
     */
    public boolean isFullyOccupied() {
        return totalReservations >= 12; // Máximo de 12 slots por dia
    }
    
    /**
     * Retorna o número de slots disponíveis.
     */
    public int getAvailableSlots() {
        return Math.max(0, 12 - totalReservations);
    }
}
