package com.vishal.project.moveInSync.moveInSyncApp.dto;

import com.vishal.project.moveInSync.moveInSyncApp.entities.enums.PaymentMethod;
import com.vishal.project.moveInSync.moveInSyncApp.entities.enums.RideStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RideDTO {

    private long id;
    private PointDTO pickUpLocation;
    private PointDTO dropOffLocation;
    private LocalDateTime createdTime;
    private RiderDTO rider;
    private DriverDTO driver;
    private PaymentMethod paymentMethod;
    private RideStatus rideStatus;
    private String otp;
    private String noShowOtp;
    private Double fare;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
