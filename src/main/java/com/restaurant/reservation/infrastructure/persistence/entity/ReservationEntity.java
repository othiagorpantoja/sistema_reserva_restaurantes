package com.restaurant.reservation.infrastructure.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade JPA para persistência de reservas.
 * Representa a tabela de reservas no banco de dados.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationEntity {
    
    @Id
    @Column(name = "id", length = 36)
    private String id;
    
    @Column(name = "table_id", nullable = false, length = 50)
    private String tableId;
    
    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;
    
    @Column(name = "customer_email", nullable = false, length = 100)
    private String customerEmail;
    
    @Column(name = "customer_phone", nullable = false, length = 20)
    private String customerPhone;
    
    @Column(name = "special_requests", length = 500)
    private String specialRequests;
    
    @Column(name = "reservation_date_time", nullable = false)
    private LocalDateTime reservationDateTime;
    
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationInMinutes;
    
    @Column(name = "number_of_people", nullable = false)
    private Integer numberOfPeople;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservationStatus status;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Enum para status da reserva na camada de persistência.
     */
    public enum ReservationStatus {
        PENDING, CONFIRMED, COMPLETED, CANCELLED, NO_SHOW
    }
}
