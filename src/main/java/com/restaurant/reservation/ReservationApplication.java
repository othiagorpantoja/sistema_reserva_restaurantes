package com.restaurant.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicação principal do sistema de reservas de restaurante.
 * Implementa Clean Architecture com boundaries claros entre as camadas.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@SpringBootApplication
public class ReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationApplication.class, args);
    }
}
