package com.vishal.project.moveInSync.moveInSyncApp.services;

import com.vishal.project.moveInSync.moveInSyncApp.entities.RideRequest;

public interface RideRequestService {

    RideRequest findRideRequestById(long rideRequestId);

    void update(RideRequest rideRequest);
}
