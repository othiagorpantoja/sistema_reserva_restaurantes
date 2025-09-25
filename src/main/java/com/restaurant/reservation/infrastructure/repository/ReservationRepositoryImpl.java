package com.restaurant.reservation.infrastructure.repository;

import com.restaurant.reservation.domain.entity.Reservation;
import com.restaurant.reservation.domain.repository.ReservationRepository;
import com.restaurant.reservation.domain.valueobject.*;
import com.restaurant.reservation.infrastructure.persistence.entity.ReservationEntity;
import com.restaurant.reservation.infrastructure.persistence.mapper.ReservationPersistenceMapper;
import com.restaurant.reservation.infrastructure.persistence.repository.JpaReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do repositório de reservas usando JPA.
 * Adapta a interface de domínio para a camada de persistência.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class ReservationRepositoryImpl implements ReservationRepository {
    
    private final JpaReservationRepository jpaRepository;
    private final ReservationPersistenceMapper mapper;
    
    @Override
    public Reservation save(Reservation reservation) {
        log.debug("Saving reservation: {}", reservation.getId());
        
        ReservationEntity entity = mapper.toEntity(reservation);
        ReservationEntity savedEntity = jpaRepository.save(entity);
        
        log.debug("Reservation saved successfully: {}", savedEntity.getId());
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public Optional<Reservation> findById(ReservationId id) {
        log.debug("Finding reservation by ID: {}", id);
        
        Optional<ReservationEntity> entityOpt = jpaRepository.findById(id.getValue());
        return entityOpt.map(mapper::toDomain);
    }
    
    @Override
    public List<Reservation> findByTableAndDate(TableId tableId, LocalDate date) {
        log.debug("Finding reservations for table {} on date {}", tableId, date);
        
        List<ReservationEntity> entities = jpaRepository.findByTableAndDate(
            tableId.getValue(), date);
        return entities.stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findConflictingReservations(TableId tableId, ReservationTime reservationTime) {
        log.debug("Finding conflicting reservations for table {} at time {}", tableId, reservationTime);
        
        List<ReservationEntity> entities = jpaRepository.findConflictingReservations(
            tableId.getValue(),
            reservationTime.getDateTime(),
            reservationTime.getEndTime()
        );
        return entities.stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findByStatus(String status) {
        log.debug("Finding reservations by status: {}", status);
        
        ReservationEntity.ReservationStatus entityStatus = 
            ReservationEntity.ReservationStatus.valueOf(status.toUpperCase());
        List<ReservationEntity> entities = jpaRepository.findByStatus(entityStatus);
        return entities.stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findByCustomerEmail(String email) {
        log.debug("Finding reservations by customer email: {}", email);
        
        List<ReservationEntity> entities = jpaRepository.findByCustomerEmail(email);
        return entities.stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Reservation> findByDate(LocalDate date) {
        log.debug("Finding reservations by date: {}", date);
        
        List<ReservationEntity> entities = jpaRepository.findByDate(date);
        return entities.stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override
    public void delete(Reservation reservation) {
        log.debug("Deleting reservation: {}", reservation.getId());
        
        jpaRepository.deleteById(reservation.getId().getValue());
        log.debug("Reservation deleted successfully: {}", reservation.getId());
    }
    
    @Override
    public boolean existsById(ReservationId id) {
        return jpaRepository.existsById(id.getValue());
    }
    
    @Override
    public long countByTableAndDate(TableId tableId, LocalDate date) {
        return jpaRepository.countByTableAndDate(tableId.getValue(), date);
    }
}
