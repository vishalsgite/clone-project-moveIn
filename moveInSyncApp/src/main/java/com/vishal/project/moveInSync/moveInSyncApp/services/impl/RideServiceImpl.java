package com.vishal.project.moveInSync.moveInSyncApp.services.impl;

import com.vishal.project.moveInSync.moveInSyncApp.dto.RideRequestDTO;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Driver;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Ride;
import com.vishal.project.moveInSync.moveInSyncApp.entities.RideRequest;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Rider;
import com.vishal.project.moveInSync.moveInSyncApp.entities.enums.RideRequestStatus;
import com.vishal.project.moveInSync.moveInSyncApp.entities.enums.RideStatus;
import com.vishal.project.moveInSync.moveInSyncApp.exceptions.ResourceNotFoundException;
import com.vishal.project.moveInSync.moveInSyncApp.repositories.RideRepository;
import com.vishal.project.moveInSync.moveInSyncApp.services.RideRequestService;
import com.vishal.project.moveInSync.moveInSyncApp.services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final RideRequestService rideRequestService;
    private final ModelMapper modelMapper;


    @Override
    public Ride getRideById(Long rideId) {
        return rideRepository.findById(rideId).orElseThrow(()-> new ResourceNotFoundException(
                "Ride Not Found "+rideId)
        );
    }

    @Override
    public void matchWithDrivers(RideRequestDTO rideRequestDTO) {

    }

    @Override
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED);

        Ride ride = modelMapper.map(rideRequest,Ride.class);


        ride.setRideStatus(RideStatus.CONFIRMED);
        ride.setDriver(driver);

        ride.setOtp(generateRandomOTP());
        ride.setNoShowOtp(generateNoShowOTP());
        ride.setId(null);

        rideRequestService.update(rideRequest);
        return rideRepository.save(ride);

    }

    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        ride.setRideStatus(rideStatus);
        return rideRepository.save(ride);
    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        return rideRepository.findByRider(rider,pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {

        return rideRepository.findByDriver(driver,pageRequest);
    }

    private String generateRandomOTP(){
        Random random = new Random();
       int otpInt =  random.nextInt(10000) ; //0 to 9999
        return String.format("%04d",otpInt);

    }
    private String generateNoShowOTP(){
        Random random = new Random();
        int noShowOtpInt =  random.nextInt(10000) ; //0 to 9999
        return String.format("%04d",noShowOtpInt);

    }

}
