package com.restaurant.reservation.application.dto;

import com.restaurant.reservation.domain.valueobject.ReservationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO para resposta de uma reserva.
 * Contém todas as informações de uma reserva para exibição.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Data
@Builder
public class ReservationResponse {
    
    private String id;
    private String tableId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String specialRequests;
    private LocalDateTime reservationDateTime;
    private LocalDateTime endTime;
    private int durationInMinutes;
    private int numberOfPeople;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * Retorna o status formatado para exibição.
     */
    public String getStatusDisplay() {
        return status != null ? status.getDisplayName() : "Desconhecido";
    }
    
    /**
     * Verifica se a reserva está ativa.
     */
    public boolean isActive() {
        return status != null && status.isActive();
    }
    
    /**
     * Verifica se a reserva pode ser modificada.
     */
    public boolean canBeModified() {
        return status != null && status.isActive();
    }
}
