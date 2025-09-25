package com.restaurant.reservation.infrastructure.repository;

import com.restaurant.reservation.domain.entity.Table;
import com.restaurant.reservation.domain.repository.TableRepository;
import com.restaurant.reservation.domain.valueobject.TableId;
import com.restaurant.reservation.infrastructure.persistence.entity.TableEntity;
import com.restaurant.reservation.infrastructure.persistence.mapper.TablePersistenceMapper;
import com.restaurant.reservation.infrastructure.persistence.repository.JpaTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do repositório de mesas usando JPA.
 * Adapta a interface de domínio para a camada de persistência.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class TableRepositoryImpl implements TableRepository {
    
    private final JpaTableRepository jpaRepository;
    private final TablePersistenceMapper mapper;
    
    @Override
    public Table save(Table table) {
        log.debug("Saving table: {}", table.getId());
        
        TableEntity entity = mapper.toEntity(table);
        TableEntity savedEntity = jpaRepository.save(entity);
        
        log.debug("Table saved successfully: {}", savedEntity.getId());
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Table> findById(TableId id) {
        log.debug("Finding table by ID: {}", id);
        
        Optional<TableEntity> entityOpt = jpaRepository.findById(id.getValue());
        return entityOpt.map(mapper::toDomain);
    }
    
    @Override
    public List<Table> findAllActive() {
        log.debug("Finding all active tables");
        
        List<TableEntity> entities = jpaRepository.findByIsActiveTrue();
        return entities.stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Table> findTablesByCapacity(int capacity) {
        log.debug("Finding tables with capacity: {}", capacity);
        
        List<TableEntity> entities = jpaRepository.findByCapacityGreaterThanEqual(capacity);
        return entities.stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Table> findAvailableTablesByCapacity(int capacity) {
        log.debug("Finding available tables with capacity: {}", capacity);
        
        List<TableEntity> entities = jpaRepository.findByCapacityGreaterThanEqual(capacity);
        return entities.stream()
            .map(mapper::toDomain)
            .filter(table -> table.isActive()) // Filtra apenas mesas ativas
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Table> findAll() {
        log.debug("Finding all tables");
        
        List<TableEntity> entities = jpaRepository.findAll();
        return entities.stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public void delete(Table table) {
        log.debug("Deleting table: {}", table.getId());
        
        jpaRepository.deleteById(table.getId().getValue());
        log.debug("Table deleted successfully: {}", table.getId());
    }
    
    @Override
    public boolean existsById(TableId id) {
        return jpaRepository.existsById(id.getValue());
    }
}
