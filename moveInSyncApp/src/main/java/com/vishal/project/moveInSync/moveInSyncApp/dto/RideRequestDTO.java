package com.vishal.project.moveInSync.moveInSyncApp.dto;


import com.vishal.project.moveInSync.moveInSyncApp.entities.enums.PaymentMethod;
import com.vishal.project.moveInSync.moveInSyncApp.entities.enums.RideRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDTO {

    private Long id;
    private PointDTO pickUpLocation;
    private PointDTO dropOffLocation;
    private LocalDateTime requestTime;
    private RiderDTO rider;
    private PaymentMethod paymentMethod;
    private RideRequestStatus rideRequestStatus;
    private Double fare;
}
