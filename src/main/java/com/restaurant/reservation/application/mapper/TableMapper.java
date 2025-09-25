package com.restaurant.reservation.application.mapper;

import com.restaurant.reservation.application.dto.TableResponse;
import com.restaurant.reservation.domain.entity.Table;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para conversão entre entidades de domínio de mesas e DTOs.
 * Utiliza MapStruct para geração automática de código de mapeamento.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Mapper
public interface TableMapper {
    
    TableMapper INSTANCE = Mappers.getMapper(TableMapper.class);
    
    /**
     * Converte uma entidade Table para TableResponse.
     */
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "capacity.value", target = "capacity")
    @Mapping(source = "isActive", target = "active")
    TableResponse toResponse(Table table);
}
