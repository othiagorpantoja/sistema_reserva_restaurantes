package com.restaurant.reservation.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Swagger/OpenAPI para documentação da API.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Sistema de Reservas de Restaurante")
                .description("API RESTful para gerenciamento de reservas de restaurante com Clean Architecture")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Equipe de Desenvolvimento")
                    .email("dev@restaurant.com")
                    .url("https://restaurant.com"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")));
    }
}
