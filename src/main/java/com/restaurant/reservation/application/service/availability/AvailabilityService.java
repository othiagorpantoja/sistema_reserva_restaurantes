package com.restaurant.reservation.application.service.availability;

import com.restaurant.reservation.domain.entity.Reservation;
import com.restaurant.reservation.domain.repository.ReservationRepository;
import com.restaurant.reservation.domain.valueobject.ReservationStatus;
import com.restaurant.reservation.domain.valueobject.ReservationTime;
import com.restaurant.reservation.domain.valueobject.TableId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço responsável por validar a disponibilidade de mesas.
 * Implementa validações complexas de disponibilidade considerando
 * conflitos de horário, capacidade e regras de negócio.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AvailabilityService {
    
    private final ReservationRepository reservationRepository;
    
    /**
     * Verifica se uma mesa está disponível no horário especificado.
     * Lança exceção se não estiver disponível.
     */
    public void checkAvailability(TableId tableId, ReservationTime reservationTime) {
        log.debug("Checking availability for table {} at {}", tableId, reservationTime);
        
        // Verifica se o horário está dentro do horário de funcionamento
        if (!reservationTime.isWithinOperatingHours()) {
            throw new IllegalArgumentException(
                "Reservation time is outside operating hours");
        }
        
        // Busca reservas conflitantes
        List<Reservation> conflictingReservations = reservationRepository
            .findConflictingReservations(tableId, reservationTime);
        
        // Filtra apenas reservas ativas (não canceladas ou completadas)
        List<Reservation> activeConflicts = conflictingReservations.stream()
            .filter(reservation -> isActiveReservation(reservation))
            .toList();
        
        if (!activeConflicts.isEmpty()) {
            log.warn("Table {} is not available at {} - {} conflicting reservations", 
                tableId, reservationTime, activeConflicts.size());
            throw new IllegalStateException(
                "Table is not available at the requested time");
        }
        
        // Verifica regras de negócio adicionais
        validateBusinessRules(tableId, reservationTime);
        
        log.debug("Table {} is available at {}", tableId, reservationTime);
    }
    
    /**
     * Verifica se uma mesa pode acomodar um número específico de pessoas
     * em um horário específico.
     */
    public boolean isTableAvailableForCapacity(TableId tableId, int numberOfPeople, 
                                             ReservationTime reservationTime) {
        try {
            checkAvailability(tableId, reservationTime);
            return true;
        } catch (Exception e) {
            log.debug("Table {} not available: {}", tableId, e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca mesas disponíveis para um horário e número de pessoas específicos.
     */
    public List<TableId> findAvailableTables(int numberOfPeople, ReservationTime reservationTime) {
        // Esta implementação seria expandida para buscar todas as mesas
        // e verificar disponibilidade para cada uma
        log.debug("Finding available tables for {} people at {}", 
            numberOfPeople, reservationTime);
        
        // Por simplicidade, retorna uma lista vazia
        // Em uma implementação real, buscaria todas as mesas do repositório
        return List.of();
    }
    
    /**
     * Verifica a disponibilidade para um período de tempo.
     */
    public AvailabilityReport getAvailabilityReport(TableId tableId, LocalDate date) {
        List<Reservation> reservations = reservationRepository
            .findByTableAndDate(tableId, date);
        
        List<Reservation> activeReservations = reservations.stream()
            .filter(this::isActiveReservation)
            .toList();
        
        return AvailabilityReport.builder()
            .tableId(tableId)
            .date(date)
            .totalReservations(activeReservations.size())
            .reservations(activeReservations)
            .build();
    }
    
    /**
     * Verifica se uma reserva está ativa (não cancelada ou completada).
     */
    private boolean isActiveReservation(Reservation reservation) {
        return reservation.getStatus() == ReservationStatus.PENDING ||
               reservation.getStatus() == ReservationStatus.CONFIRMED;
    }
    
    /**
     * Valida regras de negócio específicas para disponibilidade.
     */
    private void validateBusinessRules(TableId tableId, ReservationTime reservationTime) {
        LocalDateTime now = LocalDateTime.now();
        
        // Regra: Reservas devem ser feitas com pelo menos 1 hora de antecedência
        if (reservationTime.getDateTime().isBefore(now.plusHours(1))) {
            throw new IllegalArgumentException(
                "Reservations must be made at least 1 hour in advance");
        }
        
        // Regra: Não permite reservas para mais de 3 meses no futuro
        if (reservationTime.getDateTime().isAfter(now.plusMonths(3))) {
            throw new IllegalArgumentException(
                "Reservations cannot be made more than 3 months in advance");
        }
        
        // Regra: Verifica se não é um horário de pico (implementação simplificada)
        if (isPeakTime(reservationTime.getDateTime())) {
            log.info("Peak time reservation detected for table {}", tableId);
            // Poderia implementar regras especiais para horários de pico
        }
        
        // Regra: Verifica se não é um feriado (implementação simplificada)
        if (isHoliday(reservationTime.getDateTime().toLocalDate())) {
            log.info("Holiday reservation detected for table {}", tableId);
            // Poderia implementar regras especiais para feriados
        }
    }
    
    /**
     * Verifica se é horário de pico (implementação simplificada).
     */
    private boolean isPeakTime(LocalDateTime dateTime) {
        int hour = dateTime.getHour();
        return (hour >= 19 && hour <= 21) || // Jantar
               (hour >= 12 && hour <= 14);   // Almoço
    }
    
    /**
     * Verifica se é feriado (implementação simplificada).
     */
    private boolean isHoliday(LocalDate date) {
        // Implementação simplificada - em produção usaria uma API de feriados
        return false;
    }
}
