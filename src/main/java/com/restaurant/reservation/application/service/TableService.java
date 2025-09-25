package com.restaurant.reservation.application.service;

import com.restaurant.reservation.application.dto.TableResponse;
import com.restaurant.reservation.application.mapper.TableMapper;
import com.restaurant.reservation.domain.entity.Table;
import com.restaurant.reservation.domain.repository.TableRepository;
import com.restaurant.reservation.domain.valueobject.TableId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço de aplicação para gerenciar mesas.
 * Orquestra as operações de consulta de mesas.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TableService {
    
    private final TableRepository tableRepository;
    private final TableMapper tableMapper;
    
    /**
     * Busca todas as mesas ativas.
     */
    public List<TableResponse> getAllActiveTables() {
        log.info("Getting all active tables");
        List<Table> tables = tableRepository.findAllActive();
        return tables.stream()
            .map(tableMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Busca uma mesa por ID.
     */
    public TableResponse getTable(String tableId) {
        log.info("Getting table: {}", tableId);
        Table table = getTableById(tableId);
        return tableMapper.toResponse(table);
    }
    
    /**
     * Busca mesas por capacidade.
     */
    public List<TableResponse> getTablesByCapacity(int capacity) {
        log.info("Getting tables with capacity: {}", capacity);
        List<Table> tables = tableRepository.findTablesByCapacity(capacity);
        return tables.stream()
            .map(tableMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Busca mesas disponíveis por capacidade.
     */
    public List<TableResponse> getAvailableTablesByCapacity(int capacity) {
        log.info("Getting available tables with capacity: {}", capacity);
        List<Table> tables = tableRepository.findAvailableTablesByCapacity(capacity);
        return tables.stream()
            .map(tableMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Busca uma mesa por ID ou lança exceção se não encontrada.
     */
    private Table getTableById(String tableId) {
        return tableRepository.findById(TableId.of(tableId))
            .orElseThrow(() -> new IllegalArgumentException("Table not found: " + tableId));
    }
}
