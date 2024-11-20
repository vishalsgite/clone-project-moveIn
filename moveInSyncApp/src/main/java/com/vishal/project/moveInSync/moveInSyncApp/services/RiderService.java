package com.vishal.project.moveInSync.moveInSyncApp.services;

import com.vishal.project.moveInSync.moveInSyncApp.dto.DriverDTO;
import com.vishal.project.moveInSync.moveInSyncApp.dto.RideDTO;
import com.vishal.project.moveInSync.moveInSyncApp.dto.RideRequestDTO;
import com.vishal.project.moveInSync.moveInSyncApp.dto.RiderDTO;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Rider;
import com.vishal.project.moveInSync.moveInSyncApp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RiderService {

    RideRequestDTO requestRide(RideRequestDTO rideRequestDTO);

    RideDTO cancelRide(Long rideId);

    DriverDTO rateDriver(Long rideId,Integer rating);

    RiderDTO getMyProfile();

    Page<RideDTO> getAllMyRides(PageRequest pageRequest);

    Rider createNewRider(User user);

    Rider getCurrentRider();
}
