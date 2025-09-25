package com.restaurant.reservation.infrastructure.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;

/**
 * Entidade JPA para persistência de mesas.
 * Representa a tabela de mesas no banco de dados.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Entity
@Table(name = "tables")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableEntity {
    
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "capacity", nullable = false)
    private Integer capacity;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    @Column(name = "location", length = 100)
    private String location;
}
