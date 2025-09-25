package com.restaurant.reservation.application.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO para resposta de uma mesa.
 * Contém todas as informações de uma mesa para exibição.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Data
@Builder
public class TableResponse {
    
    private String id;
    private int capacity;
    private boolean active;
    private String location;
    
    /**
     * Retorna o status formatado para exibição.
     */
    public String getStatusDisplay() {
        return active ? "Ativa" : "Inativa";
    }
    
    /**
     * Verifica se a mesa pode acomodar um número específico de pessoas.
     */
    public boolean canAccommodate(int numberOfPeople) {
        return active && capacity >= numberOfPeople;
    }
}
