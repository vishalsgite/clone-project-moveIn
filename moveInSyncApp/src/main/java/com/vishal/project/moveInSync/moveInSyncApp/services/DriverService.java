package com.vishal.project.moveInSync.moveInSyncApp.services;

import com.vishal.project.moveInSync.moveInSyncApp.dto.DriverDTO;
import com.vishal.project.moveInSync.moveInSyncApp.dto.RideDTO;
import com.vishal.project.moveInSync.moveInSyncApp.dto.RiderDTO;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface DriverService {
    RideDTO acceptRide(Long rideRequestId);

    RideDTO cancelRide(Long rideId);

    RideDTO startRide(Long rideId ,String otp);

    RideDTO endRide(Long rideId , String otp);

    RiderDTO rateRider(Long rideId,Integer rating);

    DriverDTO getMyProfile();

    Page<RideDTO> getAllMyRides(PageRequest pageRequest);

    Driver getCurrentDriver();

    Driver updateDriveravailablity(Driver driver,boolean available);

    Driver createNewDriver(Driver driver);

    RideDTO noShowRide(Long rideId ,String otp);

}
