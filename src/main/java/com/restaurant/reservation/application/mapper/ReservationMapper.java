package com.restaurant.reservation.application.mapper;

import com.restaurant.reservation.application.dto.ReservationResponse;
import com.restaurant.reservation.domain.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para conversão entre entidades de domínio e DTOs.
 * Utiliza MapStruct para geração automática de código de mapeamento.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Mapper
public interface ReservationMapper {
    
    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);
    
    /**
     * Converte uma entidade Reservation para ReservationResponse.
     */
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "tableId.value", target = "tableId")
    @Mapping(source = "customerInfo.name", target = "customerName")
    @Mapping(source = "customerInfo.email", target = "customerEmail")
    @Mapping(source = "customerInfo.phone", target = "customerPhone")
    @Mapping(source = "customerInfo.specialRequests", target = "specialRequests")
    @Mapping(source = "reservationTime.dateTime", target = "reservationDateTime")
    @Mapping(source = "reservationTime.endTime", target = "endTime")
    @Mapping(source = "reservationTime.durationInMinutes", target = "durationInMinutes")
    ReservationResponse toResponse(Reservation reservation);
}
