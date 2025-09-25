package com.restaurant.reservation.application.mapper;

import com.restaurant.reservation.application.dto.ReservationResponse;
import com.restaurant.reservation.domain.entity.Reservation;
import com.restaurant.reservation.domain.valueobject.CustomerInfo;
import com.restaurant.reservation.domain.valueobject.ReservationId;
import com.restaurant.reservation.domain.valueobject.ReservationTime;
import com.restaurant.reservation.domain.valueobject.TableId;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-25T01:02:00-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.43.0.v20250819-1513, environment: Java 21.0.8 (Eclipse Adoptium)"
)
public class ReservationMapperImpl implements ReservationMapper {

    @Override
    public ReservationResponse toResponse(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }

        ReservationResponse.ReservationResponseBuilder reservationResponse = ReservationResponse.builder();

        reservationResponse.id( reservationIdValue( reservation ) );
        reservationResponse.tableId( reservationTableIdValue( reservation ) );
        reservationResponse.customerName( reservationCustomerInfoName( reservation ) );
        reservationResponse.customerEmail( reservationCustomerInfoEmail( reservation ) );
        reservationResponse.customerPhone( reservationCustomerInfoPhone( reservation ) );
        reservationResponse.specialRequests( reservationCustomerInfoSpecialRequests( reservation ) );
        reservationResponse.reservationDateTime( reservationReservationTimeDateTime( reservation ) );
        reservationResponse.endTime( reservationReservationTimeEndTime( reservation ) );
        reservationResponse.durationInMinutes( reservationReservationTimeDurationInMinutes( reservation ) );
        reservationResponse.status( reservation.getStatus() );

        return reservationResponse.build();
    }

    private String reservationIdValue(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }
        ReservationId id = reservation.getId();
        if ( id == null ) {
            return null;
        }
        String value = id.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }

    private String reservationTableIdValue(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }
        TableId tableId = reservation.getTableId();
        if ( tableId == null ) {
            return null;
        }
        String value = tableId.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }

    private String reservationCustomerInfoName(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }
        CustomerInfo customerInfo = reservation.getCustomerInfo();
        if ( customerInfo == null ) {
            return null;
        }
        String name = customerInfo.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String reservationCustomerInfoEmail(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }
        CustomerInfo customerInfo = reservation.getCustomerInfo();
        if ( customerInfo == null ) {
            return null;
        }
        String email = customerInfo.getEmail();
        if ( email == null ) {
            return null;
        }
        return email;
    }

    private String reservationCustomerInfoPhone(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }
        CustomerInfo customerInfo = reservation.getCustomerInfo();
        if ( customerInfo == null ) {
            return null;
        }
        String phone = customerInfo.getPhone();
        if ( phone == null ) {
            return null;
        }
        return phone;
    }

    private String reservationCustomerInfoSpecialRequests(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }
        CustomerInfo customerInfo = reservation.getCustomerInfo();
        if ( customerInfo == null ) {
            return null;
        }
        if ( customerInfo == null || !customerInfo.hasSpecialRequests() ) {
            return null;
        }
        String specialRequests = customerInfo.getSpecialRequests();
        return specialRequests;
    }

    private LocalDateTime reservationReservationTimeDateTime(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }
        ReservationTime reservationTime = reservation.getReservationTime();
        if ( reservationTime == null ) {
            return null;
        }
        LocalDateTime dateTime = reservationTime.getDateTime();
        if ( dateTime == null ) {
            return null;
        }
        return dateTime;
    }

    private LocalDateTime reservationReservationTimeEndTime(Reservation reservation) {
        if ( reservation == null ) {
            return null;
        }
        ReservationTime reservationTime = reservation.getReservationTime();
        if ( reservationTime == null ) {
            return null;
        }
        LocalDateTime endTime = reservationTime.getEndTime();
        if ( endTime == null ) {
            return null;
        }
        return endTime;
    }

    private int reservationReservationTimeDurationInMinutes(Reservation reservation) {
        if ( reservation == null ) {
            return 0;
        }
        ReservationTime reservationTime = reservation.getReservationTime();
        if ( reservationTime == null ) {
            return 0;
        }
        int durationInMinutes = reservationTime.getDurationInMinutes();
        return durationInMinutes;
    }
}
