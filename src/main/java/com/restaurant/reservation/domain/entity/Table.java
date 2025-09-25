package com.restaurant.reservation.domain.entity;

import com.restaurant.reservation.domain.valueobject.Capacity;
import com.restaurant.reservation.domain.valueobject.TableId;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Entidade que representa uma mesa do restaurante.
 * Contém informações sobre capacidade e disponibilidade.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Getter
@Setter
public class Table {
    
    private TableId id;
    private Capacity capacity;
    private boolean isActive;
    private String location;
    
    @Builder
    public Table(TableId id, Capacity capacity, boolean isActive, String location) {
        this.id = id;
        this.capacity = capacity;
        this.isActive = isActive;
        this.location = location;
        
        validateTable();
    }
    
    /**
     * Verifica se a mesa pode acomodar o número de pessoas solicitado.
     */
    public boolean canAccommodate(int numberOfPeople) {
        return isActive && capacity.getValue() >= numberOfPeople;
    }
    
    /**
     * Ativa a mesa.
     */
    public void activate() {
        this.isActive = true;
    }
    
    /**
     * Desativa a mesa.
     */
    public void deactivate() {
        this.isActive = false;
    }
    
    /**
     * Modifica a capacidade da mesa.
     */
    public void updateCapacity(Capacity newCapacity) {
        if (newCapacity.getValue() <= 0) {
            throw new IllegalArgumentException("Table capacity must be greater than 0");
        }
        this.capacity = newCapacity;
    }
    
    /**
     * Valida os dados da mesa.
     */
    private void validateTable() {
        if (id == null) {
            throw new IllegalArgumentException("Table ID cannot be null");
        }
        if (capacity == null) {
            throw new IllegalArgumentException("Table capacity cannot be null");
        }
        if (capacity.getValue() <= 0) {
            throw new IllegalArgumentException("Table capacity must be greater than 0");
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(id, table.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Table{id=%s, capacity=%s, active=%s, location='%s'}", 
            id, capacity, isActive, location);
    }
}
