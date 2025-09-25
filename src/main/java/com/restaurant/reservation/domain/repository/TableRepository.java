package com.restaurant.reservation.domain.repository;

import com.restaurant.reservation.domain.entity.Table;
import com.restaurant.reservation.domain.valueobject.TableId;

import java.util.List;
import java.util.Optional;

/**
 * Interface do repositório para a entidade Table.
 * Define os contratos de persistência sem dependências de infraestrutura.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
public interface TableRepository {
    
    /**
     * Salva uma mesa.
     */
    Table save(Table table);
    
    /**
     * Busca uma mesa por ID.
     */
    Optional<Table> findById(TableId id);
    
    /**
     * Busca todas as mesas ativas.
     */
    List<Table> findAllActive();
    
    /**
     * Busca mesas que podem acomodar um número específico de pessoas.
     */
    List<Table> findTablesByCapacity(int capacity);
    
    /**
     * Busca mesas disponíveis que podem acomodar um número específico de pessoas.
     */
    List<Table> findAvailableTablesByCapacity(int capacity);
    
    /**
     * Busca todas as mesas.
     */
    List<Table> findAll();
    
    /**
     * Remove uma mesa.
     */
    void delete(Table table);
    
    /**
     * Verifica se existe uma mesa com o ID especificado.
     */
    boolean existsById(TableId id);
}
