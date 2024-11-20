package com.vishal.project.moveInSync.moveInSyncApp.Strategies.Impl;

import com.vishal.project.moveInSync.moveInSyncApp.Strategies.DriverMatchingStrategy;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Driver;
import com.vishal.project.moveInSync.moveInSyncApp.entities.RideRequest;
import com.vishal.project.moveInSync.moveInSyncApp.repositories.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor


public class DriverMatchingHighestRatedDriverStrategy implements DriverMatchingStrategy {

    private final DriverRepository driverRepository;

    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {
        return driverRepository.findTenNearbyTopRatedDrivers(rideRequest.getPickUpLocation());
    }
}
