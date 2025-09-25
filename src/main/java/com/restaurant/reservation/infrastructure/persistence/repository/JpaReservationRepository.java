package com.restaurant.reservation.infrastructure.persistence.repository;

import com.restaurant.reservation.infrastructure.persistence.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório JPA para a entidade ReservationEntity.
 * Extende JpaRepository para operações CRUD básicas e define consultas customizadas.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Repository
public interface JpaReservationRepository extends JpaRepository<ReservationEntity, String> {
    
    /**
     * Busca reservas por mesa e data.
     */
    @Query("SELECT r FROM ReservationEntity r WHERE r.tableId = :tableId AND " +
           "DATE(r.reservationDateTime) = :date")
    List<ReservationEntity> findByTableAndDate(@Param("tableId") String tableId, 
                                             @Param("date") LocalDate date);
    
    /**
     * Busca reservas que conflitam com o horário especificado.
     */
    @Query("SELECT r FROM ReservationEntity r WHERE r.tableId = :tableId AND " +
           "r.status IN ('PENDING', 'CONFIRMED') AND " +
           "((r.reservationDateTime < :endTime AND " +
           "r.reservationDateTime + INTERVAL r.durationInMinutes MINUTE > :startTime))")
    List<ReservationEntity> findConflictingReservations(@Param("tableId") String tableId,
                                                       @Param("startTime") LocalDateTime startTime,
                                                       @Param("endTime") LocalDateTime endTime);
    
    /**
     * Busca reservas por status.
     */
    List<ReservationEntity> findByStatus(ReservationEntity.ReservationStatus status);
    
    /**
     * Busca reservas por email do cliente.
     */
    List<ReservationEntity> findByCustomerEmail(String email);
    
    /**
     * Busca reservas por data.
     */
    @Query("SELECT r FROM ReservationEntity r WHERE DATE(r.reservationDateTime) = :date")
    List<ReservationEntity> findByDate(@Param("date") LocalDate date);
    
    /**
     * Conta reservas por mesa e data.
     */
    @Query("SELECT COUNT(r) FROM ReservationEntity r WHERE r.tableId = :tableId AND " +
           "DATE(r.reservationDateTime) = :date AND r.status IN ('PENDING', 'CONFIRMED')")
    long countByTableAndDate(@Param("tableId") String tableId, @Param("date") LocalDate date);
    
    /**
     * Busca reservas ativas (não canceladas ou completadas).
     */
    @Query("SELECT r FROM ReservationEntity r WHERE r.status IN ('PENDING', 'CONFIRMED')")
    List<ReservationEntity> findActiveReservations();
    
    /**
     * Busca reservas por período.
     */
    @Query("SELECT r FROM ReservationEntity r WHERE r.reservationDateTime BETWEEN :startDate AND :endDate")
    List<ReservationEntity> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);
}
