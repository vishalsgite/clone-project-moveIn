package com.vishal.project.moveInSync.moveInSyncApp.repositories;

import com.vishal.project.moveInSync.moveInSyncApp.entities.Driver;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Ride;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RideRepository extends JpaRepository<Ride,Long> {
    Page<Ride> findByRider(Rider rider, Pageable pageRequest);

    Page<Ride> findByDriver(Driver driver, Pageable pageRequest);

}