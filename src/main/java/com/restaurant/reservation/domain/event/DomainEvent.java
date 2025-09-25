package com.restaurant.reservation.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Interface base para todos os eventos de domínio.
 * Define o contrato comum para eventos que ocorrem no sistema.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
public interface DomainEvent {
    
    /**
     * Retorna o ID único do evento.
     */
    String getEventId();
    
    /**
     * Retorna o timestamp de quando o evento ocorreu.
     */
    LocalDateTime getOccurredOn();
    
    /**
     * Retorna o tipo do evento.
     */
    String getEventType();
    
    /**
     * Retorna a versão do evento para controle de compatibilidade.
     */
    default int getVersion() {
        return 1;
    }
}
