package com.restaurant.reservation.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Controller para verificação de saúde da aplicação.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/health")
@Slf4j
@Tag(name = "Health", description = "Endpoints de verificação de saúde da aplicação")
public class HealthController {
    
    @GetMapping
    @Operation(summary = "Verificar saúde da aplicação", description = "Retorna informações sobre o status da aplicação")
    @ApiResponse(responseCode = "200", description = "Aplicação funcionando normalmente")
    public ResponseEntity<Map<String, Object>> health() {
        log.debug("Health check requested");
        
        Map<String, Object> health = Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now(),
            "application", "Restaurant Reservation System",
            "version", "1.0.0",
            "architecture", "Clean Architecture + DDD"
        );
        
        return ResponseEntity.ok(health);
    }
    
    @GetMapping("/ready")
    @Operation(summary = "Verificar prontidão da aplicação", description = "Verifica se a aplicação está pronta para receber requisições")
    @ApiResponse(responseCode = "200", description = "Aplicação pronta")
    public ResponseEntity<Map<String, String>> ready() {
        log.debug("Readiness check requested");
        
        Map<String, String> ready = Map.of(
            "status", "READY",
            "message", "Application is ready to handle requests"
        );
        
        return ResponseEntity.ok(ready);
    }
}
