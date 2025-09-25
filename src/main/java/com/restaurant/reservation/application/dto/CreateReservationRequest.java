package com.restaurant.reservation.application.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * DTO para criação de uma nova reserva.
 * Contém todas as informações necessárias para criar uma reserva.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Data
@Builder
public class CreateReservationRequest {
    
    @NotBlank(message = "Table ID is required")
    private String tableId;
    
    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    private String customerName;
    
    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;
    
    @NotBlank(message = "Customer phone is required")
    @Pattern(regexp = "^\\(?([0-9]{2})\\)?[-. ]?([0-9]{4,5})[-. ]?([0-9]{4})$", 
             message = "Invalid phone format")
    private String customerPhone;
    
    @NotNull(message = "Reservation date and time is required")
    @Future(message = "Reservation date must be in the future")
    private LocalDateTime reservationDateTime;
    
    @Min(value = 1, message = "Number of people must be at least 1")
    @Max(value = 20, message = "Number of people cannot exceed 20")
    private int numberOfPeople;
    
    @Min(value = 30, message = "Duration must be at least 30 minutes")
    @Max(value = 480, message = "Duration cannot exceed 480 minutes (8 hours)")
    private int durationInMinutes;
    
    @Size(max = 500, message = "Special requests cannot exceed 500 characters")
    private String specialRequests;
}
