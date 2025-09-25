package com.restaurant.reservation.application.service.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Serviço para integração com APIs externas de email.
 * Simula o envio de emails através de uma API externa.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Service
@Slf4j
public class EmailService {
    
    private final WebClient webClient;
    
    public EmailService() {
        this.webClient = WebClient.builder()
            .baseUrl("https://api.emailservice.com") // URL fictícia
            .build();
    }
    
    /**
     * Envia um email através de API externa.
     */
    public void sendEmail(String to, String subject, String body) {
        log.info("Sending email to: {} with subject: {}", to, subject);
        
        try {
            EmailRequest request = EmailRequest.builder()
                .to(to)
                .subject(subject)
                .body(body)
                .build();
            
            // Simula chamada para API externa
            Mono<EmailResponse> response = webClient.post()
                .uri("/send")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(EmailResponse.class);
            
            response.subscribe(
                emailResponse -> log.info("Email sent successfully. ID: {}", emailResponse.getMessageId()),
                error -> log.error("Error sending email to {}: {}", to, error.getMessage())
            );
            
        } catch (Exception e) {
            log.error("Exception occurred while sending email to {}: {}", to, e.getMessage());
            // Em um cenário real, poderia implementar retry ou fallback
        }
    }
    
    /**
     * DTO para requisição de email.
     */
    @lombok.Data
    @lombok.Builder
    public static class EmailRequest {
        private String to;
        private String subject;
        private String body;
    }
    
    /**
     * DTO para resposta de email.
     */
    @lombok.Data
    public static class EmailResponse {
        private String messageId;
        private String status;
        private String error;
    }
}
