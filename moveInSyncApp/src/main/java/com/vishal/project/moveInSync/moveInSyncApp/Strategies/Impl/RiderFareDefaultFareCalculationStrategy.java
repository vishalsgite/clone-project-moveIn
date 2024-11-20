package com.vishal.project.moveInSync.moveInSyncApp.Strategies.Impl;

import com.vishal.project.moveInSync.moveInSyncApp.Strategies.RideFareCalculationStrategy;
import com.vishal.project.moveInSync.moveInSyncApp.entities.RideRequest;
import com.vishal.project.moveInSync.moveInSyncApp.services.DistanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Primary
public class RiderFareDefaultFareCalculationStrategy implements RideFareCalculationStrategy {

    private final DistanceService distanceService;

    @Override
    public double calculateFare(RideRequest rideRequest) {
        Double fare ;
        double distance = distanceService.calculateDistance(
                rideRequest.getPickUpLocation(),rideRequest.getDropOffLocation());
        return fare = distance*RIDE_FARE_MULTIPLIER;
    }
}
