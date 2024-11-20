package com.vishal.project.moveInSync.moveInSyncApp.services.impl;

import com.vishal.project.moveInSync.moveInSyncApp.Strategies.RideFareCalculationStrategy;
import com.vishal.project.moveInSync.moveInSyncApp.Strategies.RideStrategyManager;
import com.vishal.project.moveInSync.moveInSyncApp.dto.DriverDTO;
import com.vishal.project.moveInSync.moveInSyncApp.dto.RideDTO;
import com.vishal.project.moveInSync.moveInSyncApp.dto.RideRequestDTO;
import com.vishal.project.moveInSync.moveInSyncApp.dto.RiderDTO;
import com.vishal.project.moveInSync.moveInSyncApp.entities.*;
import com.vishal.project.moveInSync.moveInSyncApp.entities.enums.RideRequestStatus;
import com.vishal.project.moveInSync.moveInSyncApp.entities.enums.RideStatus;
import com.vishal.project.moveInSync.moveInSyncApp.exceptions.ResourceNotFoundException;
import com.vishal.project.moveInSync.moveInSyncApp.repositories.RideRequestRepository;
import com.vishal.project.moveInSync.moveInSyncApp.repositories.RiderRepository;
import com.vishal.project.moveInSync.moveInSyncApp.services.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiderServiceImpl implements RiderService {

    private final ModelMapper modelMapper;
    private final RideFareCalculationStrategy rideFareCalculationStrategy;
    private final RideStrategyManager rideStrategyManager;
    private final RiderRepository riderRepository;
    private final RideRequestRepository rideRequestRepository;
    private final RideService rideService;
    private final DriverService driverService;
    private final RatingService ratingService;
    private final EmailSenderService emailSenderService;


    @Override
    @Transactional
    public RideRequestDTO requestRide(RideRequestDTO rideRequestDTO) {
        Rider rider = getCurrentRider();
        User user = getCurrentRider().getUser();


        RideRequest rideRequest = modelMapper.map(rideRequestDTO,RideRequest.class);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        rideRequest.setRider(rider);

        Double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(fare);


      RideRequest savedRideRequest =  rideRequestRepository.save(rideRequest);

        String emailContent = String.format(
                """
                <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                    <h2 style="color: #4CAF50;">Hello, %s</h2>
                    <p>Your ride request has been successfully placed. Below are the details of your request:</p>
                    <table style="border-collapse: collapse; width: 100%%; margin: 20px 0;">
                        <tr style="background-color: #f2f2f2;">
                            <th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Detail</th>
                            <th style="padding: 10px; text-align: left; border: 1px solid #ddd;">Value</th>
                        </tr>
                        <tr>
                            <td style="padding: 10px; border: 1px solid #ddd;">Pickup Location</td>
                            <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                        </tr>
                        <tr>
                            <td style="padding: 10px; border: 1px solid #ddd;">Drop-Off Location</td>
                            <td style="padding: 10px; border: 1px solid #ddd;">%s</td>
                        </tr>
                        <tr>
                            <td style="padding: 10px; border: 1px solid #ddd;">Total Fare</td>
                            <td style="padding: 10px; border: 1px solid #ddd;">%.2f</td>
                        </tr>
                    </table>
                    <p style="margin-top: 20px;">Thank you for choosing our service!</p>
                    <p style="color: #777;">If you have any questions, feel free to <a href="vshlsgite.98.vg@gmail.com" style="color: #4CAF50; text-decoration: none;">contact us</a>.</p>
                </div>
                """,
                user.getName(),  // User's name
                rideRequestDTO.getPickUpLocation(),  // Pickup location
                rideRequestDTO.getDropOffLocation(), // Drop-off location
                fare                                // Fare
        );



        emailSenderService.sendEmail(
                user.getEmail(),
                "Ride Request Confirmation",
                emailContent
        );

        log.info("Ride request email sent to {}", user.getEmail());
      List<Driver> drivers = rideStrategyManager.driverMatchingStrategy(rider.getRating()).findMatchingDriver(rideRequest);


//        TODO : Send notification to all the drivers about this ride request
        return modelMapper.map(savedRideRequest,RideRequestDTO.class);
    }

    @Override
    public RideDTO cancelRide(Long rideId) {

        Rider rider = getCurrentRider();
        Ride ride= rideService.getRideById(rideId);

        if(!rider.equals(ride.getRider())){
            throw new RuntimeException("Can't Cancelled ,Rider is not associated with this ride "+rideId);
        }

        if (!(ride.getRideStatus() == RideStatus.CONFIRMED || ride.getRideStatus() == RideStatus.PENDING)) {
            throw new RuntimeException("Ride Cannot Be Cancelled, Invalid status");
        }

        Ride SavedRide = rideService.updateRideStatus(ride,RideStatus.CANCELLED);
       driverService.updateDriveravailablity(ride.getDriver(),true);

        return modelMapper.map(SavedRide,RideDTO.class);
    }

    @Override
    public DriverDTO rateDriver(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Rider rider = getCurrentRider();
        if(!rider.equals(ride.getRider())){
            throw new RuntimeException("Rider Cannot rate Driver , because " +
                    "Rider is not associated with this ride ");
        }

        if(!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("Ride is not ENDED yet , " +
                    "So ride Cannot be start rating  "+ride.getRideStatus());
        }

        return ratingService.rateDriver(ride,rating);
    }

    @Override
    public RiderDTO getMyProfile() {
        Rider currentRider = getCurrentRider();

        return modelMapper.map(currentRider,RiderDTO.class);
    }

    @Override
    public Page<RideDTO> getAllMyRides(PageRequest pageRequest) {
        Rider currentRider = getCurrentRider();

        return rideService.getAllRidesOfRider(currentRider, pageRequest).map(
                ride -> modelMapper.map(ride,RideDTO.class)
        );
    }

    @Override
    public Rider createNewRider(User user) {
        Rider rider = Rider
                .builder()
                .user(user)
                .rating(0.0)
                .build();

        return riderRepository.save(rider);

    }

    @Override
    public Rider getCurrentRider() {

         User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return riderRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException(

                "Rider Not asscociated With id : "+user.getId()
        ));
    }


}
