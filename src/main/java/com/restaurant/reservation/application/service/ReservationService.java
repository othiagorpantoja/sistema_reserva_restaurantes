package com.restaurant.reservation.application.service;

import com.restaurant.reservation.application.dto.CreateReservationRequest;
import com.restaurant.reservation.application.dto.ReservationResponse;
import com.restaurant.reservation.application.dto.UpdateReservationRequest;
import com.restaurant.reservation.domain.entity.Reservation;
import com.restaurant.reservation.domain.entity.Table;
import com.restaurant.reservation.domain.event.DomainEvent;
import com.restaurant.reservation.domain.repository.ReservationRepository;
import com.restaurant.reservation.domain.repository.TableRepository;
import com.restaurant.reservation.domain.valueobject.*;
import com.restaurant.reservation.application.mapper.ReservationMapper;
import com.restaurant.reservation.application.service.availability.AvailabilityService;
import com.restaurant.reservation.application.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço de aplicação para gerenciar reservas.
 * Orquestra as operações de negócio e coordena entre diferentes componentes.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final AvailabilityService availabilityService;
    private final NotificationService notificationService;
    private final ReservationMapper reservationMapper;
    
    /**
     * Cria uma nova reserva.
     */
    public ReservationResponse createReservation(CreateReservationRequest request) {
        log.info("Creating reservation for customer: {}", request.getCustomerEmail());
        
        // Valida se a mesa existe e pode acomodar o número de pessoas
        Table table = validateAndGetTable(request.getTableId(), request.getNumberOfPeople());
        
        // Cria os value objects
        CustomerInfo customerInfo = CustomerInfo.of(
            request.getCustomerName(),
            request.getCustomerEmail(),
            request.getCustomerPhone(),
            request.getSpecialRequests()
        );
        
        ReservationTime reservationTime = ReservationTime.of(
            request.getReservationDateTime(),
            request.getDurationInMinutes()
        );
        
        // Verifica disponibilidade
        availabilityService.checkAvailability(table.getId(), reservationTime);
        
        // Cria a reserva
        Reservation reservation = Reservation.builder()
            .id(ReservationId.generate())
            .tableId(TableId.of(request.getTableId()))
            .customerInfo(customerInfo)
            .reservationTime(reservationTime)
            .status(ReservationStatus.PENDING)
            .build();
        
        // Salva a reserva
        Reservation savedReservation = reservationRepository.save(reservation);
        
        // Processa eventos de domínio
        processDomainEvents(savedReservation);
        
        log.info("Reservation created successfully: {}", savedReservation.getId());
        return reservationMapper.toResponse(savedReservation);
    }
    
    /**
     * Confirma uma reserva pendente.
     */
    public ReservationResponse confirmReservation(String reservationId) {
        log.info("Confirming reservation: {}", reservationId);
        
        Reservation reservation = getReservationById(reservationId);
        reservation.confirm();
        
        Reservation updatedReservation = reservationRepository.save(reservation);
        processDomainEvents(updatedReservation);
        
        log.info("Reservation confirmed: {}", reservationId);
        return reservationMapper.toResponse(updatedReservation);
    }
    
    /**
     * Cancela uma reserva.
     */
    public ReservationResponse cancelReservation(String reservationId) {
        log.info("Cancelling reservation: {}", reservationId);
        
        Reservation reservation = getReservationById(reservationId);
        reservation.cancel();
        
        Reservation updatedReservation = reservationRepository.save(reservation);
        processDomainEvents(updatedReservation);
        
        log.info("Reservation cancelled: {}", reservationId);
        return reservationMapper.toResponse(updatedReservation);
    }
    
    /**
     * Completa uma reserva confirmada.
     */
    public ReservationResponse completeReservation(String reservationId) {
        log.info("Completing reservation: {}", reservationId);
        
        Reservation reservation = getReservationById(reservationId);
        reservation.complete();
        
        Reservation updatedReservation = reservationRepository.save(reservation);
        processDomainEvents(updatedReservation);
        
        log.info("Reservation completed: {}", reservationId);
        return reservationMapper.toResponse(updatedReservation);
    }
    
    /**
     * Modifica uma reserva existente.
     */
    public ReservationResponse updateReservation(String reservationId, UpdateReservationRequest request) {
        log.info("Updating reservation: {}", reservationId);
        
        Reservation reservation = getReservationById(reservationId);
        
        if (!reservation.canBeModified()) {
            throw new IllegalStateException("Reservation cannot be modified in current status");
        }
        
        // Valida nova mesa se fornecida
        TableId newTableId = request.getTableId() != null ? 
            TableId.of(request.getTableId()) : reservation.getTableId();
        
        Table newTable = validateAndGetTable(newTableId.toString(), request.getNumberOfPeople());
        
        // Cria novo horário se fornecido
        ReservationTime newReservationTime = request.getReservationDateTime() != null ?
            ReservationTime.of(request.getReservationDateTime(), request.getDurationInMinutes()) :
            reservation.getReservationTime();
        
        // Verifica disponibilidade para as mudanças
        if (!newTableId.equals(reservation.getTableId()) || 
            !newReservationTime.equals(reservation.getReservationTime())) {
            availabilityService.checkAvailability(newTableId, newReservationTime);
        }
        
        // Modifica a reserva
        reservation.modifyReservation(newTableId, newReservationTime);
        
        Reservation updatedReservation = reservationRepository.save(reservation);
        processDomainEvents(updatedReservation);
        
        log.info("Reservation updated: {}", reservationId);
        return reservationMapper.toResponse(updatedReservation);
    }
    
    /**
     * Busca uma reserva por ID.
     */
    @Transactional(readOnly = true)
    public ReservationResponse getReservation(String reservationId) {
        Reservation reservation = getReservationById(reservationId);
        return reservationMapper.toResponse(reservation);
    }
    
    /**
     * Busca reservas por email do cliente.
     */
    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByCustomer(String email) {
        List<Reservation> reservations = reservationRepository.findByCustomerEmail(email);
        return reservations.stream()
            .map(reservationMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Busca reservas por data.
     */
    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByDate(LocalDate date) {
        List<Reservation> reservations = reservationRepository.findByDate(date);
        return reservations.stream()
            .map(reservationMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Busca reservas por status.
     */
    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByStatus(String status) {
        List<Reservation> reservations = reservationRepository.findByStatus(status);
        return reservations.stream()
            .map(reservationMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Busca reservas de uma mesa em uma data específica.
     */
    @Transactional(readOnly = true)
    public List<ReservationResponse> getTableReservations(String tableId, LocalDate date) {
        List<Reservation> reservations = reservationRepository.findByTableAndDate(
            TableId.of(tableId), date);
        return reservations.stream()
            .map(reservationMapper::toResponse)
            .collect(Collectors.toList());
    }
    
    /**
     * Valida e retorna uma mesa.
     */
    private Table validateAndGetTable(String tableId, int numberOfPeople) {
        Optional<Table> tableOpt = tableRepository.findById(TableId.of(tableId));
        if (tableOpt.isEmpty()) {
            throw new IllegalArgumentException("Table not found: " + tableId);
        }
        
        Table table = tableOpt.get();
        if (!table.canAccommodate(numberOfPeople)) {
            throw new IllegalArgumentException(
                "Table cannot accommodate " + numberOfPeople + " people");
        }
        
        return table;
    }
    
    /**
     * Busca uma reserva por ID ou lança exceção se não encontrada.
     */
    private Reservation getReservationById(String reservationId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(
            ReservationId.of(reservationId));
        if (reservationOpt.isEmpty()) {
            throw new IllegalArgumentException("Reservation not found: " + reservationId);
        }
        return reservationOpt.get();
    }
    
    /**
     * Processa os eventos de domínio da reserva.
     */
    private void processDomainEvents(Reservation reservation) {
        for (DomainEvent event : reservation.getDomainEvents()) {
            log.debug("Processing domain event: {}", event.getEventType());
            notificationService.handleDomainEvent(event);
        }
        reservation.clearDomainEvents();
    }
}
