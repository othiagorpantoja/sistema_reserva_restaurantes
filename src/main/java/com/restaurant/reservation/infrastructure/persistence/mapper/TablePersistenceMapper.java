package com.restaurant.reservation.infrastructure.persistence.mapper;

import com.restaurant.reservation.domain.entity.Table;
import com.restaurant.reservation.domain.valueobject.Capacity;
import com.restaurant.reservation.domain.valueobject.TableId;
import com.restaurant.reservation.infrastructure.persistence.entity.TableEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper para conversão entre entidades de domínio e entidades de persistência de mesas.
 * Utiliza MapStruct para geração automática de código de mapeamento.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@Mapper
public interface TablePersistenceMapper {
    
    TablePersistenceMapper INSTANCE = Mappers.getMapper(TablePersistenceMapper.class);
    
    /**
     * Converte uma entidade de domínio para entidade de persistência.
     */
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "capacity.value", target = "capacity")
    TableEntity toEntity(Table table);
    
    /**
     * Converte uma entidade de persistência para entidade de domínio.
     */
    @Mapping(source = "id", target = "id.value")
    @Mapping(target = "capacity", expression = "java(Capacity.of(entity.getCapacity()))")
    Table toDomain(TableEntity entity);
}
