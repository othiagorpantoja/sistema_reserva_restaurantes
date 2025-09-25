package com.restaurant.reservation.application.service.notification;

import com.restaurant.reservation.domain.event.DomainEvent;
import com.restaurant.reservation.domain.event.ReservationCancelledEvent;
import com.restaurant.reservation.domain.event.ReservationCompletedEvent;
import com.restaurant.reservation.domain.event.ReservationConfirmedEvent;
import com.restaurant.reservation.domain.event.ReservationModifiedEvent;
import com.restaurant.reservation.application.service.integration.EmailService;
import com.restaurant.reservation.application.service.integration.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por processar eventos de domínio e enviar notificações.
 * Coordena o envio de emails e SMS baseado nos eventos que ocorrem no sistema.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    
    private final EmailService emailService;
    private final SmsService smsService;
    
    /**
     * Processa um evento de domínio e envia as notificações apropriadas.
     */
    public void handleDomainEvent(DomainEvent event) {
        log.info("Processing domain event: {} - {}", event.getEventType(), event.getEventId());
        
        switch (event.getEventType()) {
            case "ReservationConfirmed" -> handleReservationConfirmed((ReservationConfirmedEvent) event);
            case "ReservationCancelled" -> handleReservationCancelled((ReservationCancelledEvent) event);
            case "ReservationCompleted" -> handleReservationCompleted((ReservationCompletedEvent) event);
            case "ReservationModified" -> handleReservationModified((ReservationModifiedEvent) event);
            default -> log.warn("Unknown event type: {}", event.getEventType());
        }
    }
    
    /**
     * Processa evento de reserva confirmada.
     */
    private void handleReservationConfirmed(ReservationConfirmedEvent event) {
        try {
            // Envia email de confirmação
            String emailSubject = "Reserva Confirmada - " + event.getCustomerInfo().getName();
            String emailBody = buildConfirmationEmailBody(event);
            emailService.sendEmail(event.getCustomerInfo().getEmail(), emailSubject, emailBody);
            
            // Envia SMS de confirmação (opcional)
            String smsMessage = String.format(
                "Sua reserva foi confirmada para %s na mesa %s. Obrigado!",
                event.getReservationTime().getFormattedTime(),
                event.getTableId()
            );
            smsService.sendSms(event.getCustomerInfo().getPhone(), smsMessage);
            
            log.info("Confirmation notifications sent for reservation: {}", event.getReservationId());
            
        } catch (Exception e) {
            log.error("Error sending confirmation notifications for reservation: {}", 
                event.getReservationId(), e);
        }
    }
    
    /**
     * Processa evento de reserva cancelada.
     */
    private void handleReservationCancelled(ReservationCancelledEvent event) {
        try {
            // Envia email de cancelamento
            String emailSubject = "Reserva Cancelada - " + event.getCustomerInfo().getName();
            String emailBody = buildCancellationEmailBody(event);
            emailService.sendEmail(event.getCustomerInfo().getEmail(), emailSubject, emailBody);
            
            log.info("Cancellation notifications sent for reservation: {}", event.getReservationId());
            
        } catch (Exception e) {
            log.error("Error sending cancellation notifications for reservation: {}", 
                event.getReservationId(), e);
        }
    }
    
    /**
     * Processa evento de reserva completada.
     */
    private void handleReservationCompleted(ReservationCompletedEvent event) {
        try {
            // Envia email de agradecimento
            String emailSubject = "Obrigado pela visita - " + event.getCustomerInfo().getName();
            String emailBody = buildCompletionEmailBody(event);
            emailService.sendEmail(event.getCustomerInfo().getEmail(), emailSubject, emailBody);
            
            log.info("Completion notifications sent for reservation: {}", event.getReservationId());
            
        } catch (Exception e) {
            log.error("Error sending completion notifications for reservation: {}", 
                event.getReservationId(), e);
        }
    }
    
    /**
     * Processa evento de reserva modificada.
     */
    private void handleReservationModified(ReservationModifiedEvent event) {
        try {
            // Envia email de modificação
            String emailSubject = "Reserva Modificada - " + event.getCustomerInfo().getName();
            String emailBody = buildModificationEmailBody(event);
            emailService.sendEmail(event.getCustomerInfo().getEmail(), emailSubject, emailBody);
            
            log.info("Modification notifications sent for reservation: {}", event.getReservationId());
            
        } catch (Exception e) {
            log.error("Error sending modification notifications for reservation: {}", 
                event.getReservationId(), e);
        }
    }
    
    /**
     * Constrói o corpo do email de confirmação.
     */
    private String buildConfirmationEmailBody(ReservationConfirmedEvent event) {
        return String.format("""
            Olá %s,
            
            Sua reserva foi confirmada com sucesso!
            
            Detalhes da reserva:
            - Data e hora: %s
            - Mesa: %s
            - Email: %s
            - Telefone: %s
            
            Esperamos vê-lo em breve!
            
            Atenciosamente,
            Equipe do Restaurante
            """, 
            event.getCustomerInfo().getFormattedName(),
            event.getReservationTime().getFormattedTime(),
            event.getTableId(),
            event.getCustomerInfo().getEmail(),
            event.getCustomerInfo().getPhone()
        );
    }
    
    /**
     * Constrói o corpo do email de cancelamento.
     */
    private String buildCancellationEmailBody(ReservationCancelledEvent event) {
        return String.format("""
            Olá %s,
            
            Sua reserva foi cancelada conforme solicitado.
            
            Detalhes da reserva cancelada:
            - Data e hora: %s
            - Mesa: %s
            
            Esperamos poder atendê-lo em uma próxima oportunidade!
            
            Atenciosamente,
            Equipe do Restaurante
            """, 
            event.getCustomerInfo().getFormattedName(),
            event.getReservationTime().getFormattedTime(),
            event.getTableId()
        );
    }
    
    /**
     * Constrói o corpo do email de conclusão.
     */
    private String buildCompletionEmailBody(ReservationCompletedEvent event) {
        return String.format("""
            Olá %s,
            
            Obrigado por escolher nosso restaurante!
            
            Esperamos que tenha tido uma excelente experiência.
            Sua opinião é muito importante para nós.
            
            Detalhes da visita:
            - Data e hora: %s
            - Mesa: %s
            
            Atenciosamente,
            Equipe do Restaurante
            """, 
            event.getCustomerInfo().getFormattedName(),
            event.getReservationTime().getFormattedTime(),
            event.getTableId()
        );
    }
    
    /**
     * Constrói o corpo do email de modificação.
     */
    private String buildModificationEmailBody(ReservationModifiedEvent event) {
        return String.format("""
            Olá %s,
            
            Sua reserva foi modificada com sucesso!
            
            Novos detalhes da reserva:
            - Data e hora: %s
            - Mesa: %s
            
            Caso tenha alguma dúvida, entre em contato conosco.
            
            Atenciosamente,
            Equipe do Restaurante
            """, 
            event.getCustomerInfo().getFormattedName(),
            event.getNewReservationTime().getFormattedTime(),
            event.getNewTableId()
        );
    }
}
