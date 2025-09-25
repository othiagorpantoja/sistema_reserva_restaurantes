package com.restaurant.reservation.application.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * DTO para atualização de uma reserva existente.
 * Todos os campos são opcionais para permitir atualizações parciais.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Data
@Builder
public class UpdateReservationRequest {
    
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    private String customerName;
    
    @Email(message = "Invalid email format")
    private String customerEmail;
    
    @Pattern(regexp = "^\\(?([0-9]{2})\\)?[-. ]?([0-9]{4,5})[-. ]?([0-9]{4})$", 
             message = "Invalid phone format")
    private String customerPhone;
    
    private String tableId;
    
    @Future(message = "Reservation date must be in the future")
    private LocalDateTime reservationDateTime;
    
    @Min(value = 1, message = "Number of people must be at least 1")
    @Max(value = 20, message = "Number of people cannot exceed 20")
    private Integer numberOfPeople;
    
    @Min(value = 30, message = "Duration must be at least 30 minutes")
    @Max(value = 480, message = "Duration cannot exceed 480 minutes (8 hours)")
    private Integer durationInMinutes;
    
    @Size(max = 500, message = "Special requests cannot exceed 500 characters")
    private String specialRequests;
}
