package com.restaurant.reservation.domain.repository;

import com.restaurant.reservation.domain.entity.Reservation;
import com.restaurant.reservation.domain.valueobject.ReservationId;
import com.restaurant.reservation.domain.valueobject.TableId;
import com.restaurant.reservation.domain.valueobject.ReservationTime;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface do repositório para a entidade Reservation.
 * Define os contratos de persistência sem dependências de infraestrutura.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
public interface ReservationRepository {
    
    /**
     * Salva uma reserva.
     */
    Reservation save(Reservation reservation);
    
    /**
     * Busca uma reserva por ID.
     */
    Optional<Reservation> findById(ReservationId id);
    
    /**
     * Busca todas as reservas de uma mesa em uma data específica.
     */
    List<Reservation> findByTableAndDate(TableId tableId, LocalDate date);
    
    /**
     * Busca reservas que conflitam com o horário especificado.
     */
    List<Reservation> findConflictingReservations(TableId tableId, ReservationTime reservationTime);
    
    /**
     * Busca reservas por status.
     */
    List<Reservation> findByStatus(String status);
    
    /**
     * Busca reservas de um cliente por email.
     */
    List<Reservation> findByCustomerEmail(String email);
    
    /**
     * Busca reservas para uma data específica.
     */
    List<Reservation> findByDate(LocalDate date);
    
    /**
     * Remove uma reserva.
     */
    void delete(Reservation reservation);
    
    /**
     * Verifica se existe uma reserva com o ID especificado.
     */
    boolean existsById(ReservationId id);
    
    /**
     * Conta o número de reservas de uma mesa em uma data.
     */
    long countByTableAndDate(TableId tableId, LocalDate date);
}
