package com.vishal.project.moveInSync.moveInSyncApp.services;

import com.vishal.project.moveInSync.moveInSyncApp.dto.RideRequestDTO;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Driver;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Ride;
import com.vishal.project.moveInSync.moveInSyncApp.entities.RideRequest;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Rider;
import com.vishal.project.moveInSync.moveInSyncApp.entities.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RideService {

    Ride getRideById(Long rideId);
    void matchWithDrivers(RideRequestDTO rideRequestDTO);
    Ride createNewRide(RideRequest rideRequest, Driver driver);
    Ride updateRideStatus(Ride ride, RideStatus rideStatus);
    Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest);
    Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest);



}
