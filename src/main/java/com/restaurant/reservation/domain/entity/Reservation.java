package com.restaurant.reservation.domain.entity;

import com.restaurant.reservation.domain.valueobject.CustomerInfo;
import com.restaurant.reservation.domain.valueobject.ReservationId;
import com.restaurant.reservation.domain.valueobject.ReservationStatus;
import com.restaurant.reservation.domain.valueobject.ReservationTime;
import com.restaurant.reservation.domain.valueobject.TableId;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidade principal que representa uma reserva no sistema.
 * Contém toda a lógica de negócio relacionada a reservas.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Getter
@Setter
public class Reservation {
    
    private ReservationId id;
    private TableId tableId;
    private CustomerInfo customerInfo;
    private ReservationTime reservationTime;
    private ReservationStatus status;
    private List<DomainEvent> domainEvents;
    
    @Builder
    public Reservation(ReservationId id, TableId tableId, CustomerInfo customerInfo, 
                      ReservationTime reservationTime, ReservationStatus status) {
        this.id = id;
        this.tableId = tableId;
        this.customerInfo = customerInfo;
        this.reservationTime = reservationTime;
        this.status = status != null ? status : ReservationStatus.PENDING;
        this.domainEvents = new ArrayList<>();
        
        validateReservation();
    }
    
    /**
     * Confirma uma reserva pendente.
     * Lança exceção se a reserva não estiver em estado válido para confirmação.
     */
    public void confirm() {
        if (!this.status.canTransitionTo(ReservationStatus.CONFIRMED)) {
            throw new IllegalStateException(
                "Cannot confirm reservation in status: " + this.status);
        }
        
        this.status = ReservationStatus.CONFIRMED;
        addDomainEvent(new ReservationConfirmedEvent(this.id, this.tableId, 
            this.customerInfo, this.reservationTime));
    }
    
    /**
     * Cancela uma reserva.
     * Permite cancelamento de reservas pendentes ou confirmadas.
     */
    public void cancel() {
        if (!this.status.canTransitionTo(ReservationStatus.CANCELLED)) {
            throw new IllegalStateException(
                "Cannot cancel reservation in status: " + this.status);
        }
        
        this.status = ReservationStatus.CANCELLED;
        addDomainEvent(new ReservationCancelledEvent(this.id, this.tableId, 
            this.customerInfo, this.reservationTime));
    }
    
    /**
     * Completa uma reserva confirmada.
     * Indica que o cliente compareceu e a reserva foi utilizada.
     */
    public void complete() {
        if (!this.status.canTransitionTo(ReservationStatus.COMPLETED)) {
            throw new IllegalStateException(
                "Cannot complete reservation in status: " + this.status);
        }
        
        this.status = ReservationStatus.COMPLETED;
        addDomainEvent(new ReservationCompletedEvent(this.id, this.tableId, 
            this.customerInfo, this.reservationTime));
    }
    
    /**
     * Verifica se a reserva é válida para modificação.
     */
    public boolean canBeModified() {
        return this.status == ReservationStatus.PENDING || 
               this.status == ReservationStatus.CONFIRMED;
    }
    
    /**
     * Modifica os dados da reserva se permitido.
     */
    public void modifyReservation(TableId newTableId, ReservationTime newTime) {
        if (!canBeModified()) {
            throw new IllegalStateException(
                "Cannot modify reservation in status: " + this.status);
        }
        
        this.tableId = newTableId;
        this.reservationTime = newTime;
        addDomainEvent(new ReservationModifiedEvent(this.id, newTableId, 
            this.customerInfo, newTime));
    }
    
    /**
     * Adiciona um evento de domínio à lista.
     */
    private void addDomainEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }
    
    /**
     * Remove todos os eventos de domínio após serem processados.
     */
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
    
    /**
     * Valida os dados da reserva.
     */
    private void validateReservation() {
        if (id == null) {
            throw new IllegalArgumentException("Reservation ID cannot be null");
        }
        if (tableId == null) {
            throw new IllegalArgumentException("Table ID cannot be null");
        }
        if (customerInfo == null) {
            throw new IllegalArgumentException("Customer info cannot be null");
        }
        if (reservationTime == null) {
            throw new IllegalArgumentException("Reservation time cannot be null");
        }
        if (reservationTime.getDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Reservation time cannot be in the past");
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Reservation{id=%s, tableId=%s, status=%s, time=%s}", 
            id, tableId, status, reservationTime);
    }
}
