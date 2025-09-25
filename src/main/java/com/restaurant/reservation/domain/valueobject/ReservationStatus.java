package com.restaurant.reservation.domain.valueobject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

/**
 * Enum que representa os possíveis status de uma reserva.
 * Contém lógica para transições válidas entre estados.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum ReservationStatus {
    
    PENDING("Pendente", Set.of(CONFIRMED, CANCELLED)),
    CONFIRMED("Confirmada", Set.of(COMPLETED, CANCELLED, NO_SHOW)),
    COMPLETED("Concluída", Set.of()),
    CANCELLED("Cancelada", Set.of()),
    NO_SHOW("Não compareceu", Set.of());
    
    private final String description;
    private final Set<ReservationStatus> validTransitions;
    
    /**
     * Verifica se é possível fazer transição para o status especificado.
     */
    public boolean canTransitionTo(ReservationStatus targetStatus) {
        return validTransitions.contains(targetStatus);
    }
    
    /**
     * Verifica se a reserva está ativa (pode ser modificada).
     */
    public boolean isActive() {
        return this == PENDING || this == CONFIRMED;
    }
    
    /**
     * Verifica se a reserva está finalizada.
     */
    public boolean isFinalized() {
        return this == COMPLETED || this == CANCELLED || this == NO_SHOW;
    }
    
    /**
     * Verifica se a reserva foi confirmada.
     */
    public boolean isConfirmed() {
        return this == CONFIRMED;
    }
    
    /**
     * Verifica se a reserva foi cancelada.
     */
    public boolean isCancelled() {
        return this == CANCELLED;
    }
    
    /**
     * Retorna o status em português para exibição.
     */
    public String getDisplayName() {
        return description;
    }
    
    @Override
    public String toString() {
        return description;
    }
}
