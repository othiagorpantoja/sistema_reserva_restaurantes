package com.restaurant.reservation.domain.valueobject;

import lombok.Value;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Value Object que representa o horário de uma reserva.
 * Contém validações de horário de funcionamento e regras de negócio.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Value
public class ReservationTime {
    
    LocalDateTime dateTime;
    int durationInMinutes;
    
    private ReservationTime(LocalDateTime dateTime, int durationInMinutes) {
        validateReservationTime(dateTime, durationInMinutes);
        this.dateTime = dateTime;
        this.durationInMinutes = durationInMinutes;
    }
    
    /**
     * Cria ReservationTime com duração padrão de 2 horas.
     */
    public static ReservationTime of(LocalDateTime dateTime) {
        return new ReservationTime(dateTime, 120);
    }
    
    /**
     * Cria ReservationTime com duração específica.
     */
    public static ReservationTime of(LocalDateTime dateTime, int durationInMinutes) {
        return new ReservationTime(dateTime, durationInMinutes);
    }
    
    /**
     * Valida o horário da reserva.
     */
    private void validateReservationTime(LocalDateTime dateTime, int durationInMinutes) {
        if (dateTime == null) {
            throw new IllegalArgumentException("Reservation date and time cannot be null");
        }
        
        if (durationInMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0 minutes");
        }
        
        if (durationInMinutes > 480) { // 8 hours
            throw new IllegalArgumentException("Duration cannot exceed 8 hours");
        }
        
        // Verifica se está dentro do horário de funcionamento
        LocalTime time = dateTime.toLocalTime();
        LocalTime openTime = LocalTime.of(11, 0); // 11:00
        LocalTime closeTime = LocalTime.of(23, 0); // 23:00
        
        if (time.isBefore(openTime) || time.isAfter(closeTime)) {
            throw new IllegalArgumentException(
                "Reservation time must be between " + openTime + " and " + closeTime);
        }
        
        // Verifica se não é no passado
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reservation time cannot be in the past");
        }
        
        // Verifica se não é muito no futuro (máximo 3 meses)
        if (dateTime.isAfter(LocalDateTime.now().plusMonths(3))) {
            throw new IllegalArgumentException("Reservation time cannot be more than 3 months in the future");
        }
    }
    
    /**
     * Retorna o horário de término da reserva.
     */
    public LocalDateTime getEndTime() {
        return dateTime.plusMinutes(durationInMinutes);
    }
    
    /**
     * Verifica se o horário está dentro do horário de funcionamento.
     */
    public boolean isWithinOperatingHours() {
        LocalTime time = dateTime.toLocalTime();
        LocalTime endTime = getEndTime().toLocalTime();
        LocalTime openTime = LocalTime.of(11, 0);
        LocalTime closeTime = LocalTime.of(23, 0);
        
        return !time.isBefore(openTime) && !endTime.isAfter(closeTime);
    }
    
    /**
     * Verifica se a reserva é para hoje.
     */
    public boolean isToday() {
        return dateTime.toLocalDate().equals(LocalDateTime.now().toLocalDate());
    }
    
    /**
     * Verifica se a reserva é para amanhã.
     */
    public boolean isTomorrow() {
        return dateTime.toLocalDate().equals(LocalDateTime.now().plusDays(1).toLocalDate());
    }
    
    /**
     * Retorna o horário formatado para exibição.
     */
    public String getFormattedTime() {
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
    
    /**
     * Verifica se há conflito de horário com outra reserva.
     */
    public boolean hasTimeConflict(ReservationTime other) {
        if (other == null) return false;
        
        LocalDateTime thisStart = this.dateTime;
        LocalDateTime thisEnd = this.getEndTime();
        LocalDateTime otherStart = other.dateTime;
        LocalDateTime otherEnd = other.getEndTime();
        
        // Verifica se há sobreposição
        return thisStart.isBefore(otherEnd) && thisEnd.isAfter(otherStart);
    }
    
    @Override
    public String toString() {
        return String.format("ReservationTime{dateTime=%s, duration=%d minutes}", 
            dateTime, durationInMinutes);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationTime that = (ReservationTime) o;
        return durationInMinutes == that.durationInMinutes && 
               Objects.equals(dateTime, that.dateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(dateTime, durationInMinutes);
    }
}
