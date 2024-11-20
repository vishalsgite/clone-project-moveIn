package com.vishal.project.moveInSync.moveInSyncApp.repositories;

import com.vishal.project.moveInSync.moveInSyncApp.entities.Driver;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Rating;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Ride;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating,Long> {

    List<Rating> findByRider(Rider rider);
    List<Rating> findByDriver(Driver driver);

    Optional<Rating> findByRide(Ride ride);
}
