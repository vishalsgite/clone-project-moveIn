package com.vishal.project.moveInSync.moveInSyncApp.services.impl;

import com.vishal.project.moveInSync.moveInSyncApp.dto.DriverDTO;
import com.vishal.project.moveInSync.moveInSyncApp.dto.RiderDTO;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Driver;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Rating;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Ride;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Rider;
import com.vishal.project.moveInSync.moveInSyncApp.exceptions.ResourceNotFoundException;
import com.vishal.project.moveInSync.moveInSyncApp.repositories.DriverRepository;
import com.vishal.project.moveInSync.moveInSyncApp.repositories.RatingRepository;
import com.vishal.project.moveInSync.moveInSyncApp.repositories.RideRepository;
import com.vishal.project.moveInSync.moveInSyncApp.repositories.RiderRepository;
import com.vishal.project.moveInSync.moveInSyncApp.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor

public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;

    @Override
    public DriverDTO rateDriver(Ride ride, Integer rating) {

        Driver driver = ride.getDriver();
        Rating ratingObj = ratingRepository.findByRide(ride).orElseThrow(()->
                new ResourceNotFoundException("Rating not found for ride with ID : "+ride.getId()));

        if(ratingObj.getDriverRating() != null)
            throw new RuntimeException("Driver Already Rated , Cannot Rate Again  ");

        ratingObj.setDriverRating(rating);

        ratingRepository.save(ratingObj);
       Double newRating =  ratingRepository.findByDriver(driver)
                .stream().mapToDouble(Rating::getDriverRating)
                .average().orElse(0.0);

       driver.setRating(newRating);
        Driver savedDriver = driverRepository.save(driver);
        return modelMapper.map(savedDriver,DriverDTO.class);



    }

    @Override
    public RiderDTO rateRider(Ride ride, Integer rating) {

        Rider rider = ride.getRider();
        Rating ratingObj = ratingRepository.findByRide(ride).orElseThrow(()->
                new ResourceNotFoundException("Rating not found for ride with ID : "+ride.getId()));

        if(ratingObj.getRiderRating() != null)
            throw new RuntimeException("Rider Already Rated , Cannot Rate Again  ");

        ratingObj.setRiderRating(rating);

        ratingRepository.save(ratingObj);
        Double newRating =  ratingRepository.findByRider(rider)
                .stream().mapToDouble(Rating::getRiderRating)
                .average().orElse(0.0);

        rider.setRating(newRating);
        Rider savedRider = riderRepository.save(rider);

        return modelMapper.map(savedRider, RiderDTO.class);
    }

    @Override
    public void createNewRating(Ride ride) {
        Rating rating = Rating.builder()
                .rider(
                        ride.getRider())
                .driver(ride.getDriver())
                .ride(ride)
                .build();
        ratingRepository.save(rating);
    }
}
