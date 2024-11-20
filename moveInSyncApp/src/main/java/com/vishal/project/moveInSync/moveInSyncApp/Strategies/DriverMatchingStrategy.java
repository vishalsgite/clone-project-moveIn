package com.vishal.project.moveInSync.moveInSyncApp.Strategies;

import com.vishal.project.moveInSync.moveInSyncApp.entities.Driver;
import com.vishal.project.moveInSync.moveInSyncApp.entities.RideRequest;

import java.util.List;

public interface  DriverMatchingStrategy {
    List<Driver> findMatchingDriver(RideRequest rideRequest);




}
