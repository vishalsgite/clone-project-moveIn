package com.vishal.project.moveInSync.moveInSyncApp.services;

import com.vishal.project.moveInSync.moveInSyncApp.dto.DriverDTO;
import com.vishal.project.moveInSync.moveInSyncApp.dto.RiderDTO;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Driver;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Ride;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Rider;

public interface RatingService {

    DriverDTO rateDriver(Ride ride, Integer rating);
    RiderDTO rateRider(Ride ride, Integer rating);
    void createNewRating(Ride ride);
}
