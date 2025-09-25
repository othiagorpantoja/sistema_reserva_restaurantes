package com.restaurant.reservation.domain.valueobject;

import lombok.Value;

import java.util.Objects;
import java.util.UUID;

/**
 * Value Object que representa o identificador único de uma reserva.
 * Garante imutabilidade e validação do ID.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Value
public class ReservationId {
    
    String value;
    
    private ReservationId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Reservation ID cannot be null or empty");
        }
        this.value = value;
    }
    
    /**
     * Cria um novo ReservationId com valor gerado automaticamente.
     */
    public static ReservationId generate() {
        return new ReservationId(UUID.randomUUID().toString());
    }
    
    /**
     * Cria um ReservationId a partir de uma string existente.
     */
    public static ReservationId of(String value) {
        return new ReservationId(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationId that = (ReservationId) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
