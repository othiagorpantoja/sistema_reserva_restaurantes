package com.restaurant.reservation.infrastructure.persistence.repository;

import com.restaurant.reservation.infrastructure.persistence.entity.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório JPA para a entidade TableEntity.
 * Extende JpaRepository para operações CRUD básicas e define consultas customizadas.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Repository
public interface JpaTableRepository extends JpaRepository<TableEntity, String> {
    
    /**
     * Busca todas as mesas ativas.
     */
    List<TableEntity> findByIsActiveTrue();
    
    /**
     * Busca mesas por capacidade.
     */
    @Query("SELECT t FROM TableEntity t WHERE t.capacity >= :capacity AND t.isActive = true")
    List<TableEntity> findByCapacityGreaterThanEqual(@Param("capacity") Integer capacity);
    
    /**
     * Busca mesas por capacidade exata.
     */
    @Query("SELECT t FROM TableEntity t WHERE t.capacity = :capacity AND t.isActive = true")
    List<TableEntity> findByCapacity(@Param("capacity") Integer capacity);
    
    /**
     * Busca mesas por localização.
     */
    List<TableEntity> findByLocationAndIsActiveTrue(String location);
    
    /**
     * Busca mesas disponíveis (sem reservas ativas).
     * Esta consulta seria mais complexa em um cenário real.
     */
    @Query("SELECT t FROM TableEntity t WHERE t.isActive = true")
    List<TableEntity> findAvailableTables();
}
