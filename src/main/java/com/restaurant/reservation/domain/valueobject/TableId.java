package com.restaurant.reservation.domain.valueobject;

import lombok.Value;

import java.util.Objects;

/**
 * Value Object que representa o identificador único de uma mesa.
 * Garante imutabilidade e validação do ID.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Value
public class TableId {
    
    String value;
    
    private TableId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Table ID cannot be null or empty");
        }
        this.value = value;
    }
    
    /**
     * Cria um TableId a partir de uma string.
     */
    public static TableId of(String value) {
        return new TableId(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableId tableId = (TableId) o;
        return Objects.equals(value, tableId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
