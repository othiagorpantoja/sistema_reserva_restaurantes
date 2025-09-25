package com.restaurant.reservation.presentation.controller;

import com.restaurant.reservation.application.dto.CreateReservationRequest;
import com.restaurant.reservation.application.dto.ReservationResponse;
import com.restaurant.reservation.application.dto.UpdateReservationRequest;
import com.restaurant.reservation.application.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller REST para gerenciar reservas.
 * Expõe endpoints para operações CRUD de reservas.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Reservations", description = "API para gerenciar reservas de restaurante")
public class ReservationController {
    
    private final ReservationService reservationService;
    
    /**
     * Cria uma nova reserva.
     */
    @PostMapping
    @Operation(summary = "Criar reserva", description = "Cria uma nova reserva no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reserva criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "409", description = "Conflito de horário")
    })
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody CreateReservationRequest request) {
        
        log.info("Creating reservation for customer: {}", request.getCustomerEmail());
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Busca uma reserva por ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar reserva por ID", description = "Retorna uma reserva específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
        @ApiResponse(responseCode = "404", description = "Reserva não encontrada")
    })
    public ResponseEntity<ReservationResponse> getReservation(
            @Parameter(description = "ID da reserva") @PathVariable String id) {
        
        log.info("Getting reservation: {}", id);
        ReservationResponse response = reservationService.getReservation(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Confirma uma reserva pendente.
     */
    @PutMapping("/{id}/confirm")
    @Operation(summary = "Confirmar reserva", description = "Confirma uma reserva pendente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva confirmada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Reserva não pode ser confirmada"),
        @ApiResponse(responseCode = "404", description = "Reserva não encontrada")
    })
    public ResponseEntity<ReservationResponse> confirmReservation(
            @Parameter(description = "ID da reserva") @PathVariable String id) {
        
        log.info("Confirming reservation: {}", id);
        ReservationResponse response = reservationService.confirmReservation(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Cancela uma reserva.
     */
    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancelar reserva", description = "Cancela uma reserva")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva cancelada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Reserva não pode ser cancelada"),
        @ApiResponse(responseCode = "404", description = "Reserva não encontrada")
    })
    public ResponseEntity<ReservationResponse> cancelReservation(
            @Parameter(description = "ID da reserva") @PathVariable String id) {
        
        log.info("Cancelling reservation: {}", id);
        ReservationResponse response = reservationService.cancelReservation(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Completa uma reserva confirmada.
     */
    @PutMapping("/{id}/complete")
    @Operation(summary = "Completar reserva", description = "Marca uma reserva como completada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva completada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Reserva não pode ser completada"),
        @ApiResponse(responseCode = "404", description = "Reserva não encontrada")
    })
    public ResponseEntity<ReservationResponse> completeReservation(
            @Parameter(description = "ID da reserva") @PathVariable String id) {
        
        log.info("Completing reservation: {}", id);
        ReservationResponse response = reservationService.completeReservation(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Atualiza uma reserva existente.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar reserva", description = "Atualiza uma reserva existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou reserva não pode ser modificada"),
        @ApiResponse(responseCode = "404", description = "Reserva não encontrada"),
        @ApiResponse(responseCode = "409", description = "Conflito de horário")
    })
    public ResponseEntity<ReservationResponse> updateReservation(
            @Parameter(description = "ID da reserva") @PathVariable String id,
            @Valid @RequestBody UpdateReservationRequest request) {
        
        log.info("Updating reservation: {}", id);
        ReservationResponse response = reservationService.updateReservation(id, request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Busca reservas por email do cliente.
     */
    @GetMapping("/customer/{email}")
    @Operation(summary = "Buscar reservas por cliente", description = "Retorna todas as reservas de um cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservas encontradas")
    })
    public ResponseEntity<List<ReservationResponse>> getReservationsByCustomer(
            @Parameter(description = "Email do cliente") @PathVariable String email) {
        
        log.info("Getting reservations for customer: {}", email);
        List<ReservationResponse> response = reservationService.getReservationsByCustomer(email);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Busca reservas por data.
     */
    @GetMapping("/date/{date}")
    @Operation(summary = "Buscar reservas por data", description = "Retorna todas as reservas de uma data específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservas encontradas")
    })
    public ResponseEntity<List<ReservationResponse>> getReservationsByDate(
            @Parameter(description = "Data das reservas (formato: yyyy-MM-dd)")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        log.info("Getting reservations for date: {}", date);
        List<ReservationResponse> response = reservationService.getReservationsByDate(date);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Busca reservas por status.
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar reservas por status", description = "Retorna todas as reservas com um status específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservas encontradas")
    })
    public ResponseEntity<List<ReservationResponse>> getReservationsByStatus(
            @Parameter(description = "Status da reserva") @PathVariable String status) {
        
        log.info("Getting reservations with status: {}", status);
        List<ReservationResponse> response = reservationService.getReservationsByStatus(status);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Busca reservas de uma mesa em uma data específica.
     */
    @GetMapping("/table/{tableId}/date/{date}")
    @Operation(summary = "Buscar reservas por mesa e data", description = "Retorna todas as reservas de uma mesa em uma data específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservas encontradas")
    })
    public ResponseEntity<List<ReservationResponse>> getTableReservations(
            @Parameter(description = "ID da mesa") @PathVariable String tableId,
            @Parameter(description = "Data das reservas (formato: yyyy-MM-dd)")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        log.info("Getting reservations for table {} on date {}", tableId, date);
        List<ReservationResponse> response = reservationService.getTableReservations(tableId, date);
        return ResponseEntity.ok(response);
    }
}
