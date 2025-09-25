package com.restaurant.reservation.domain.valueobject;

import lombok.Value;

import java.util.Objects;

/**
 * Value Object que representa a capacidade de uma mesa.
 * Garante que a capacidade seja sempre um valor positivo válido.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Value
public class Capacity {
    
    int value;
    
    private Capacity(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        if (value > 20) {
            throw new IllegalArgumentException("Capacity cannot exceed 20 people");
        }
        this.value = value;
    }
    
    /**
     * Cria um Capacity a partir de um valor inteiro.
     */
    public static Capacity of(int value) {
        return new Capacity(value);
    }
    
    /**
     * Verifica se esta capacidade pode acomodar o número de pessoas especificado.
     */
    public boolean canAccommodate(int numberOfPeople) {
        return value >= numberOfPeople;
    }
    
    /**
     * Retorna a diferença entre esta capacidade e o número de pessoas.
     * Útil para verificar quantas pessoas a mais cabem na mesa.
     */
    public int getRemainingSpace(int numberOfPeople) {
        return Math.max(0, value - numberOfPeople);
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Capacity capacity = (Capacity) o;
        return value == capacity.value;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
