package com.restaurant.reservation.application.service.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Serviço para integração com APIs externas de SMS.
 * Simula o envio de SMS através de uma API externa.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Service
@Slf4j
public class SmsService {
    
    private final WebClient webClient;
    
    public SmsService() {
        this.webClient = WebClient.builder()
            .baseUrl("https://api.smsservice.com") // URL fictícia
            .build();
    }
    
    /**
     * Envia um SMS através de API externa.
     */
    public void sendSms(String phoneNumber, String message) {
        log.info("Sending SMS to: {} with message: {}", phoneNumber, message);
        
        try {
            SmsRequest request = SmsRequest.builder()
                .to(phoneNumber)
                .message(message)
                .build();
            
            // Simula chamada para API externa
            Mono<SmsResponse> response = webClient.post()
                .uri("/send")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(SmsResponse.class);
            
            response.subscribe(
                smsResponse -> log.info("SMS sent successfully. ID: {}", smsResponse.getMessageId()),
                error -> log.error("Error sending SMS to {}: {}", phoneNumber, error.getMessage())
            );
            
        } catch (Exception e) {
            log.error("Exception occurred while sending SMS to {}: {}", phoneNumber, e.getMessage());
            // Em um cenário real, poderia implementar retry ou fallback
        }
    }
    
    /**
     * DTO para requisição de SMS.
     */
    @lombok.Data
    @lombok.Builder
    public static class SmsRequest {
        private String to;
        private String message;
    }
    
    /**
     * DTO para resposta de SMS.
     */
    @lombok.Data
    public static class SmsResponse {
        private String messageId;
        private String status;
        private String error;
    }
}
