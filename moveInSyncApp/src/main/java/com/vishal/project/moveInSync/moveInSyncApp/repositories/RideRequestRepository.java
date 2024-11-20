package com.vishal.project.moveInSync.moveInSyncApp.repositories;

import com.vishal.project.moveInSync.moveInSyncApp.entities.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RideRequestRepository extends JpaRepository<RideRequest,Long> {
}
