package com.restaurant.reservation.infrastructure.config;

import com.restaurant.reservation.domain.entity.Table;
import com.restaurant.reservation.domain.repository.TableRepository;
import com.restaurant.reservation.domain.valueobject.Capacity;
import com.restaurant.reservation.domain.valueobject.TableId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Configuração para inicialização de dados de exemplo.
 * Executa apenas em ambiente de desenvolvimento.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test") // Não executa durante os testes
public class DataInitializationConfig implements CommandLineRunner {
    
    private final TableRepository tableRepository;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing sample data...");
        
        // Verifica se já existem mesas cadastradas
        if (tableRepository.findAll().isEmpty()) {
            initializeTables();
        }
        
        log.info("Sample data initialization completed");
    }
    
    private void initializeTables() {
        log.info("Creating sample tables...");
        
        // Mesas para 2 pessoas
        createTable("T001", 2, "Área interna");
        createTable("T002", 2, "Área interna");
        
        // Mesas para 4 pessoas
        createTable("T003", 4, "Área interna");
        createTable("T004", 4, "Área interna");
        createTable("T005", 4, "Área interna");
        
        // Mesas para 6 pessoas
        createTable("T006", 6, "Área interna");
        createTable("T007", 6, "Área interna");
        
        // Mesas para 8 pessoas
        createTable("T008", 8, "Área externa");
        createTable("T009", 8, "Área externa");
        
        // Mesas para 10 pessoas
        createTable("T010", 10, "Área externa");
        
        // Mesas VIP
        createTable("T011", 12, "Área VIP");
        
        // Mesa em manutenção
        Table maintenanceTable = Table.builder()
            .id(TableId.of("T012"))
            .capacity(Capacity.of(2))
            .isActive(false)
            .location("Área interna - Manutenção")
            .build();
        tableRepository.save(maintenanceTable);
        
        log.info("Created {} sample tables", 12);
    }
    
    private void createTable(String id, int capacity, String location) {
        Table table = Table.builder()
            .id(TableId.of(id))
            .capacity(Capacity.of(capacity))
            .isActive(true)
            .location(location)
            .build();
        
        tableRepository.save(table);
        log.debug("Created table: {} with capacity: {} at location: {}", id, capacity, location);
    }
}
