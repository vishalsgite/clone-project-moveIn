package com.vishal.project.moveInSync.moveInSyncApp.repositories;

import com.vishal.project.moveInSync.moveInSyncApp.entities.Rider;
import com.vishal.project.moveInSync.moveInSyncApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RiderRepository extends JpaRepository<Rider,Long> {
    Optional<Rider> findByUser(User user);
}
