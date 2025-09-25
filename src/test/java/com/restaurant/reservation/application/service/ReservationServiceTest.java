package com.restaurant.reservation.application.service;

import com.restaurant.reservation.application.dto.CreateReservationRequest;
import com.restaurant.reservation.application.dto.ReservationResponse;
import com.restaurant.reservation.application.dto.UpdateReservationRequest;
import com.restaurant.reservation.application.mapper.ReservationMapper;
import com.restaurant.reservation.application.service.availability.AvailabilityService;
import com.restaurant.reservation.application.service.notification.NotificationService;
import com.restaurant.reservation.domain.entity.Reservation;
import com.restaurant.reservation.domain.entity.Table;
import com.restaurant.reservation.domain.repository.ReservationRepository;
import com.restaurant.reservation.domain.repository.TableRepository;
import com.restaurant.reservation.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o ReservationService.
 * 
 * @author Sistema de Reservas
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Reservation Service Tests")
class ReservationServiceTest {
    
    @Mock
    private ReservationRepository reservationRepository;
    
    @Mock
    private TableRepository tableRepository;
    
    @Mock
    private AvailabilityService availabilityService;
    
    @Mock
    private NotificationService notificationService;
    
    @Mock
    private ReservationMapper reservationMapper;
    
    @InjectMocks
    private ReservationService reservationService;
    
    private CreateReservationRequest createRequest;
    private Table table;
    private Reservation reservation;
    private ReservationResponse reservationResponse;
    
    @BeforeEach
    void setUp() {
        createRequest = CreateReservationRequest.builder()
            .tableId("T001")
            .customerName("João Silva")
            .customerEmail("joao@email.com")
            .customerPhone("(11) 99999-9999")
            .reservationDateTime(LocalDateTime.now().plusHours(1))
            .numberOfPeople(2)
            .durationInMinutes(120)
            .specialRequests("Mesa próxima à janela")
            .build();
        
        table = Table.builder()
            .id(TableId.of("T001"))
            .capacity(Capacity.of(4))
            .isActive(true)
            .location("Área interna")
            .build();
        
        reservation = Reservation.builder()
            .id(ReservationId.generate())
            .tableId(TableId.of("T001"))
            .customerInfo(CustomerInfo.of("João Silva", "joao@email.com", "(11) 99999-9999"))
            .reservationTime(ReservationTime.of(LocalDateTime.now().plusHours(1)))
            .status(ReservationStatus.PENDING)
            .build();
        
        reservationResponse = ReservationResponse.builder()
            .id(reservation.getId().toString())
            .tableId("T001")
            .customerName("João Silva")
            .customerEmail("joao@email.com")
            .status(ReservationStatus.PENDING)
            .build();
    }
    
    @Test
    @DisplayName("Should create reservation successfully")
    void shouldCreateReservationSuccessfully() {
        // Given
        when(tableRepository.findById(any(TableId.class))).thenReturn(Optional.of(table));
        doNothing().when(availabilityService).checkAvailability(any(TableId.class), any(ReservationTime.class));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(reservationMapper.toResponse(any(Reservation.class))).thenReturn(reservationResponse);
        
        // When
        ReservationResponse response = reservationService.createReservation(createRequest);
        
        // Then
        assertNotNull(response);
        assertEquals("T001", response.getTableId());
        assertEquals("João Silva", response.getCustomerName());
        assertEquals("joao@email.com", response.getCustomerEmail());
        
        verify(tableRepository).findById(TableId.of("T001"));
        verify(availabilityService).checkAvailability(any(TableId.class), any(ReservationTime.class));
        verify(reservationRepository).save(any(Reservation.class));
        verify(notificationService).handleDomainEvent(any());
    }
    
    @Test
    @DisplayName("Should throw exception when table not found")
    void shouldThrowExceptionWhenTableNotFound() {
        // Given
        when(tableRepository.findById(any(TableId.class))).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> reservationService.createReservation(createRequest));
        
        verify(tableRepository).findById(TableId.of("T001"));
        verify(availabilityService, never()).checkAvailability(any(), any());
        verify(reservationRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Should throw exception when table cannot accommodate people")
    void shouldThrowExceptionWhenTableCannotAccommodatePeople() {
        // Given
        Table smallTable = Table.builder()
            .id(TableId.of("T001"))
            .capacity(Capacity.of(1)) // Capacidade menor que o necessário
            .isActive(true)
            .location("Área interna")
            .build();
        
        when(tableRepository.findById(any(TableId.class))).thenReturn(Optional.of(smallTable));
        
        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> reservationService.createReservation(createRequest));
        
        verify(tableRepository).findById(TableId.of("T001"));
        verify(availabilityService, never()).checkAvailability(any(), any());
        verify(reservationRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Should confirm reservation successfully")
    void shouldConfirmReservationSuccessfully() {
        // Given
        String reservationId = "R001";
        Reservation confirmedReservation = Reservation.builder()
            .id(ReservationId.of(reservationId))
            .tableId(TableId.of("T001"))
            .customerInfo(CustomerInfo.of("João Silva", "joao@email.com", "(11) 99999-9999"))
            .reservationTime(ReservationTime.of(LocalDateTime.now().plusHours(1)))
            .status(ReservationStatus.CONFIRMED)
            .build();
        
        ReservationResponse confirmedResponse = ReservationResponse.builder()
            .id(reservationId)
            .status(ReservationStatus.CONFIRMED)
            .build();
        
        when(reservationRepository.findById(any(ReservationId.class)))
            .thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class)))
            .thenReturn(confirmedReservation);
        when(reservationMapper.toResponse(any(Reservation.class)))
            .thenReturn(confirmedResponse);
        
        // When
        ReservationResponse response = reservationService.confirmReservation(reservationId);
        
        // Then
        assertNotNull(response);
        assertEquals(ReservationStatus.CONFIRMED, response.getStatus());
        
        verify(reservationRepository).findById(ReservationId.of(reservationId));
        verify(reservationRepository).save(any(Reservation.class));
        verify(notificationService).handleDomainEvent(any());
    }
    
    @Test
    @DisplayName("Should cancel reservation successfully")
    void shouldCancelReservationSuccessfully() {
        // Given
        String reservationId = "R001";
        Reservation cancelledReservation = Reservation.builder()
            .id(ReservationId.of(reservationId))
            .tableId(TableId.of("T001"))
            .customerInfo(CustomerInfo.of("João Silva", "joao@email.com", "(11) 99999-9999"))
            .reservationTime(ReservationTime.of(LocalDateTime.now().plusHours(1)))
            .status(ReservationStatus.CANCELLED)
            .build();
        
        ReservationResponse cancelledResponse = ReservationResponse.builder()
            .id(reservationId)
            .status(ReservationStatus.CANCELLED)
            .build();
        
        when(reservationRepository.findById(any(ReservationId.class)))
            .thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class)))
            .thenReturn(cancelledReservation);
        when(reservationMapper.toResponse(any(Reservation.class)))
            .thenReturn(cancelledResponse);
        
        // When
        ReservationResponse response = reservationService.cancelReservation(reservationId);
        
        // Then
        assertNotNull(response);
        assertEquals(ReservationStatus.CANCELLED, response.getStatus());
        
        verify(reservationRepository).findById(ReservationId.of(reservationId));
        verify(reservationRepository).save(any(Reservation.class));
        verify(notificationService).handleDomainEvent(any());
    }
    
    @Test
    @DisplayName("Should throw exception when reservation not found")
    void shouldThrowExceptionWhenReservationNotFound() {
        // Given
        String reservationId = "R999";
        when(reservationRepository.findById(any(ReservationId.class)))
            .thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(IllegalArgumentException.class, 
            () -> reservationService.confirmReservation(reservationId));
        
        verify(reservationRepository).findById(ReservationId.of(reservationId));
        verify(reservationRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Should update reservation successfully")
    void shouldUpdateReservationSuccessfully() {
        // Given
        String reservationId = "R001";
        UpdateReservationRequest updateRequest = UpdateReservationRequest.builder()
            .tableId("T002")
            .reservationDateTime(LocalDateTime.now().plusHours(2))
            .numberOfPeople(4)
            .durationInMinutes(180)
            .build();
        
        Table newTable = Table.builder()
            .id(TableId.of("T002"))
            .capacity(Capacity.of(6))
            .isActive(true)
            .location("Área externa")
            .build();
        
        when(reservationRepository.findById(any(ReservationId.class)))
            .thenReturn(Optional.of(reservation));
        when(tableRepository.findById(any(TableId.class)))
            .thenReturn(Optional.of(newTable));
        doNothing().when(availabilityService).checkAvailability(any(TableId.class), any(ReservationTime.class));
        when(reservationRepository.save(any(Reservation.class)))
            .thenReturn(reservation);
        when(reservationMapper.toResponse(any(Reservation.class)))
            .thenReturn(reservationResponse);
        
        // When
        ReservationResponse response = reservationService.updateReservation(reservationId, updateRequest);
        
        // Then
        assertNotNull(response);
        
        verify(reservationRepository).findById(ReservationId.of(reservationId));
        verify(tableRepository).findById(TableId.of("T002"));
        verify(availabilityService).checkAvailability(any(TableId.class), any(ReservationTime.class));
        verify(reservationRepository).save(any(Reservation.class));
        verify(notificationService).handleDomainEvent(any());
    }
}
