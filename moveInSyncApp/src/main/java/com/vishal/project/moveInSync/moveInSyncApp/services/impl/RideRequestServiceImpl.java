package com.vishal.project.moveInSync.moveInSyncApp.services.impl;

import com.vishal.project.moveInSync.moveInSyncApp.entities.RideRequest;
import com.vishal.project.moveInSync.moveInSyncApp.exceptions.ResourceNotFoundException;
import com.vishal.project.moveInSync.moveInSyncApp.repositories.RideRequestRepository;
import com.vishal.project.moveInSync.moveInSyncApp.services.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RideRequestServiceImpl implements RideRequestService {

    private final RideRequestRepository rideRequestRepository;

    @Override
    public RideRequest findRideRequestById(long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Ride Request Not Found With ID : "+rideRequestId));
    }

    @Override
    public void update(RideRequest rideRequest) {
                rideRequestRepository.findById(rideRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Rider Request Not Found With ID : "+rideRequest.getId()));

                rideRequestRepository.save(rideRequest);

    }
}
