package com.vishal.project.moveInSync.moveInSyncApp.Strategies.Impl;

import com.vishal.project.moveInSync.moveInSyncApp.Strategies.RideFareCalculationStrategy;
import com.vishal.project.moveInSync.moveInSyncApp.entities.RideRequest;
import com.vishal.project.moveInSync.moveInSyncApp.services.DistanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RideFareSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy {
    private final DistanceService distanceService;
    private static final double SURGE_FACTOR = 1.45;

        @Override
        public double calculateFare (RideRequest rideRequest){

            double distance = distanceService.calculateDistance(
                    rideRequest.getPickUpLocation(), rideRequest.getDropOffLocation());
            return distance * RIDE_FARE_MULTIPLIER * SURGE_FACTOR ;
        }

}

