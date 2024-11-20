package com.vishal.project.moveInSync.moveInSyncApp.Strategies;

import com.vishal.project.moveInSync.moveInSyncApp.entities.RideRequest;


public interface RideFareCalculationStrategy {


    Double RIDE_FARE_MULTIPLIER = 10.0;
    double calculateFare(RideRequest rideRequest);

}
