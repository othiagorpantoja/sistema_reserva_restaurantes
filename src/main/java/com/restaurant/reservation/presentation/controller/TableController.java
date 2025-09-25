package com.restaurant.reservation.presentation.controller;

import com.restaurant.reservation.application.dto.TableResponse;
import com.restaurant.reservation.application.service.TableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciar mesas.
 * Expõe endpoints para operações de consulta de mesas.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/tables")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tables", description = "API para gerenciar mesas do restaurante")
public class TableController {
    
    private final TableService tableService;
    
    /**
     * Busca todas as mesas ativas.
     */
    @GetMapping
    @Operation(summary = "Listar mesas", description = "Retorna todas as mesas ativas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mesas encontradas")
    })
    public ResponseEntity<List<TableResponse>> getAllTables() {
        log.info("Getting all active tables");
        List<TableResponse> response = tableService.getAllActiveTables();
        return ResponseEntity.ok(response);
    }
    
    /**
     * Busca uma mesa por ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar mesa por ID", description = "Retorna uma mesa específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mesa encontrada"),
        @ApiResponse(responseCode = "404", description = "Mesa não encontrada")
    })
    public ResponseEntity<TableResponse> getTable(
            @Parameter(description = "ID da mesa") @PathVariable String id) {
        
        log.info("Getting table: {}", id);
        TableResponse response = tableService.getTable(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Busca mesas por capacidade.
     */
    @GetMapping("/capacity/{capacity}")
    @Operation(summary = "Buscar mesas por capacidade", description = "Retorna mesas que podem acomodar um número específico de pessoas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mesas encontradas")
    })
    public ResponseEntity<List<TableResponse>> getTablesByCapacity(
            @Parameter(description = "Capacidade mínima da mesa") @PathVariable int capacity) {
        
        log.info("Getting tables with capacity: {}", capacity);
        List<TableResponse> response = tableService.getTablesByCapacity(capacity);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Busca mesas disponíveis por capacidade.
     */
    @GetMapping("/available/capacity/{capacity}")
    @Operation(summary = "Buscar mesas disponíveis por capacidade", description = "Retorna mesas disponíveis que podem acomodar um número específico de pessoas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mesas encontradas")
    })
    public ResponseEntity<List<TableResponse>> getAvailableTablesByCapacity(
            @Parameter(description = "Capacidade mínima da mesa") @PathVariable int capacity) {
        
        log.info("Getting available tables with capacity: {}", capacity);
        List<TableResponse> response = tableService.getAvailableTablesByCapacity(capacity);
        return ResponseEntity.ok(response);
    }
}
