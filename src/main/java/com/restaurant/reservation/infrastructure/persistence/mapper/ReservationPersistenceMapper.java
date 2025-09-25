package com.restaurant.reservation.infrastructure.persistence.mapper;

import com.restaurant.reservation.domain.entity.Reservation;
import com.restaurant.reservation.domain.valueobject.*;
import com.restaurant.reservation.infrastructure.persistence.entity.ReservationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para conversão entre entidades de domínio e entidades de persistência.
 * Utiliza MapStruct para geração automática de código de mapeamento.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Mapper
public interface ReservationPersistenceMapper {
    
    ReservationPersistenceMapper INSTANCE = Mappers.getMapper(ReservationPersistenceMapper.class);
    
    /**
     * Converte uma entidade de domínio para entidade de persistência.
     */
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "tableId.value", target = "tableId")
    @Mapping(source = "customerInfo.name", target = "customerName")
    @Mapping(source = "customerInfo.email", target = "customerEmail")
    @Mapping(source = "customerInfo.phone", target = "customerPhone")
    @Mapping(source = "customerInfo.specialRequests", target = "specialRequests")
    @Mapping(source = "reservationTime.dateTime", target = "reservationDateTime")
    @Mapping(source = "reservationTime.durationInMinutes", target = "durationInMinutes")
    @Mapping(target = "numberOfPeople", ignore = true) // Será calculado baseado na mesa
    @Mapping(target = "status", expression = "java(mapStatusToEntity(reservation.getStatus()))")
    @Mapping(target = "createdAt", ignore = true) // Será definido pelo JPA
    @Mapping(target = "updatedAt", ignore = true) // Será definido pelo JPA
    ReservationEntity toEntity(Reservation reservation);
    
    /**
     * Converte uma entidade de persistência para entidade de domínio.
     */
    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "tableId", target = "tableId.value")
    @Mapping(target = "customerInfo", expression = "java(createCustomerInfo(entity))")
    @Mapping(target = "reservationTime", expression = "java(createReservationTime(entity))")
    @Mapping(target = "status", expression = "java(mapStatusFromEntity(entity.getStatus()))")
    @Mapping(target = "domainEvents", ignore = true) // Não é persistido
    Reservation toDomain(ReservationEntity entity);
    
    /**
     * Mapeia o status do domínio para a entidade.
     */
    default ReservationEntity.ReservationStatus mapStatusToEntity(ReservationStatus status) {
        return switch (status) {
            case PENDING -> ReservationEntity.ReservationStatus.PENDING;
            case CONFIRMED -> ReservationEntity.ReservationStatus.CONFIRMED;
            case COMPLETED -> ReservationEntity.ReservationStatus.COMPLETED;
            case CANCELLED -> ReservationEntity.ReservationStatus.CANCELLED;
            case NO_SHOW -> ReservationEntity.ReservationStatus.NO_SHOW;
        };
    }
    
    /**
     * Mapeia o status da entidade para o domínio.
     */
    default ReservationStatus mapStatusFromEntity(ReservationEntity.ReservationStatus status) {
        return switch (status) {
            case PENDING -> ReservationStatus.PENDING;
            case CONFIRMED -> ReservationStatus.CONFIRMED;
            case COMPLETED -> ReservationStatus.COMPLETED;
            case CANCELLED -> ReservationStatus.CANCELLED;
            case NO_SHOW -> ReservationStatus.NO_SHOW;
        };
    }
    
    /**
     * Cria CustomerInfo a partir da entidade.
     */
    default CustomerInfo createCustomerInfo(ReservationEntity entity) {
        return CustomerInfo.of(
            entity.getCustomerName(),
            entity.getCustomerEmail(),
            entity.getCustomerPhone(),
            entity.getSpecialRequests()
        );
    }
    
    /**
     * Cria ReservationTime a partir da entidade.
     */
    default ReservationTime createReservationTime(ReservationEntity entity) {
        return ReservationTime.of(
            entity.getReservationDateTime(),
            entity.getDurationInMinutes()
        );
    }
}
