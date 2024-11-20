package com.vishal.project.moveInSync.moveInSyncApp.services.impl;

import com.vishal.project.moveInSync.moveInSyncApp.dto.DriverDTO;
import com.vishal.project.moveInSync.moveInSyncApp.dto.RideDTO;
import com.vishal.project.moveInSync.moveInSyncApp.dto.RiderDTO;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Driver;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Ride;
import com.vishal.project.moveInSync.moveInSyncApp.entities.RideRequest;
import com.vishal.project.moveInSync.moveInSyncApp.entities.User;
import com.vishal.project.moveInSync.moveInSyncApp.entities.enums.RideRequestStatus;
import com.vishal.project.moveInSync.moveInSyncApp.entities.enums.RideStatus;
import com.vishal.project.moveInSync.moveInSyncApp.exceptions.ResourceNotFoundException;
import com.vishal.project.moveInSync.moveInSyncApp.repositories.DriverRepository;
import com.vishal.project.moveInSync.moveInSyncApp.services.DriverService;
import com.vishal.project.moveInSync.moveInSyncApp.services.EmailSenderService;
import com.vishal.project.moveInSync.moveInSyncApp.services.RatingService;
import com.vishal.project.moveInSync.moveInSyncApp.services.RideService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final RideRequestServiceImpl rideRequestService;

    private final DriverRepository driverRepository;

    private final ModelMapper modelMapper;

    private final RideService rideService;
    private final EmailSenderService emailSenderService;

    private final RatingService ratingService;
    @Override
    @Transactional
    public RideDTO acceptRide(Long rideRequestId) {

        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);

        if(!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)){

            throw
                    new RuntimeException("ride Request cannot be accepted ,status : "+rideRequest.getRideRequestStatus());
        }

        Driver currentDriver = getCurrentDriver();
        if(!currentDriver.getAvailable()){
            throw
                    new RuntimeException("Cannot Accept Ride , Driver Is Not Available : ");

        }
        Driver savedDriver = updateDriveravailablity(currentDriver,false);
            Ride ride = rideService.createNewRide(rideRequest,savedDriver);
        RideDTO rideDTO = modelMapper.map(ride, RideDTO.class);

        // Create email content with ride details
        String emailContent = String.format(
                "<!DOCTYPE html>" +
                        "<html lang='en'>" +
                        "<head>" +
                        "    <meta charset='UTF-8'>" +
                        "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                        "    <style>" +
                        "        body {" +
                        "            font-family: Arial, sans-serif;" +
                        "            margin: 0;" +
                        "            padding: 0;" +
                        "            background-color: #f4f4f4;" +
                        "        }" +
                        "        .email-container {" +
                        "            max-width: 600px;" +
                        "            margin: 20px auto;" +
                        "            background: #ffffff;" +
                        "            border-radius: 8px;" +
                        "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);" +
                        "            overflow: hidden;" +
                        "        }" +
                        "        .header {" +
                        "            background-color: #4CAF50;" +
                        "            color: #ffffff;" +
                        "            padding: 20px;" +
                        "            text-align: center;" +
                        "        }" +
                        "        .otp-box {" +
                        "            text-align: center;" +
                        "            margin: 20px;" +
                        "            padding: 20px;" +
                        "            background-color: #f9f9f9;" +
                        "            border: 2px dashed #4CAF50;" +
                        "            border-radius: 8px;" +
                        "            font-size: 24px;" +
                        "            font-weight: bold;" +
                        "            color: #4CAF50;" +
                        "        }" +
                        "        .content {" +
                        "            padding: 20px;" +
                        "            color: #333333;" +
                        "            line-height: 1.6;" +
                        "        }" +
                        "        .content p {" +
                        "            margin: 10px 0;" +
                        "        }" +
                        "        .details {" +
                        "            background-color: #f9f9f9;" +
                        "            border: 1px solid #ddd;" +
                        "            padding: 15px;" +
                        "            border-radius: 5px;" +
                        "            margin: 20px 0;" +
                        "        }" +
                        "        .footer {" +
                        "            background-color: #f4f4f4;" +
                        "            padding: 10px;" +
                        "            text-align: center;" +
                        "            color: #666;" +
                        "            font-size: 0.9em;" +
                        "        }" +
                        "        .footer a {" +
                        "            color: #4CAF50;" +
                        "            text-decoration: none;" +
                        "        }" +
                        "    </style>" +
                        "</head>" +
                        "<body>" +
                        "    <div class='email-container'>" +
                        "        <div class='header'>" +
                        "            <h1>Ride Confirmation</h1>" +
                        "        </div>" +
                        "        <div class='otp-box'>" +
                        "            OTP: %s" +
                        "        </div>" +
                        "        <div class='content'>" +
                        "            <p>Dear %s,</p>" +
                        "            <p>Your ride has been confirmed with the following details:</p>" +
                        "            <div class='details'>" +
                        "                <p><strong>Pickup Location:</strong> %s</p>" +
                        "                <p><strong>Dropoff Location:</strong> %s</p>" +
                        "                <p><strong>Driver Name:</strong> %s</p>" +
                        "                <p><strong>Ride Fare:</strong> ₹%.2f</p>" +
                        "                <p><strong>Estimated Start Time:</strong> %s</p>" +
                        "            </div>" +
                        "            <p>Thank you for choosing our service. We wish you a safe and pleasant journey!</p>" +
                        "        </div>" +
                        "        <div class='footer'>" +
                        "            <p>Best Regards,<br>VISHAL GITE</p>" +
                        "            <p><a href='vshlsgite.98.vg@gmail.com'>Mail to us </a></p>" +
                        "        </div>" +
                        "    </div>" +
                        "</body>" +
                        "</html>",
                rideDTO.getOtp(), // For OTP
                rideDTO.getRider().getUser().getName(), // Rider's name
                rideDTO.getPickUpLocation(), // Pickup location
                rideDTO.getDropOffLocation(), // Dropoff location
                rideDTO.getDriver().getUser().getName(), // Driver's name
                rideDTO.getFare(), // Fare
                rideDTO.getStartedAt() // Estimated start time
        );



        // Send email
        emailSenderService.sendEmail(
                rideDTO.getRider().getUser().getEmail(),
                "Ride Confirmation Details",
                emailContent
        );

        return rideDTO;

    }

    @Override
    public RideDTO cancelRide(Long rideId) {

        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Can't Cancelled ,Driver is not associated with this ride ");
        }

        if (!(ride.getRideStatus() == RideStatus.CONFIRMED || ride.getRideStatus() == RideStatus.PENDING)) {
            throw new RuntimeException("Ride Cannot Be Cancelled, Invalid status"+ride.getRideStatus());
        }

        rideService.updateRideStatus(ride,RideStatus.CANCELLED);
        updateDriveravailablity(driver,true);


        return modelMapper.map(ride,RideDTO.class);

    }

    @Override
    public RideDTO noShowRide(Long rideId, String otp) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver is not associated with this ride ");
        }
        if (!(ride.getRideStatus() == RideStatus.CONFIRMED || ride.getRideStatus() == RideStatus.PENDING)) {
            throw new RuntimeException("NO SHOW is not Possible , Invalid  Ride status"+ride.getRideStatus());
        }
        if(!otp.equals(ride.getNoShowOtp())){
            throw new RuntimeException("Invalid OTP ");
        }
        rideService.updateRideStatus(ride,RideStatus.ABSENT);
        updateDriveravailablity(driver,true);


        return modelMapper.map(ride,RideDTO.class);
    }

    @Override
    public RideDTO startRide(Long rideId , String otp) {


        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        RideDTO rideDTO = modelMapper.map(ride, RideDTO.class);
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver is not associated with this ride ");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride is not CONFIRMED yet , " +
                    "So ride Cannot be start  "+ride.getRideStatus());
        }

        if(!otp.equals(ride.getOtp())){
            throw new RuntimeException("Invalid OTP ");
        }
        ride.setStartedAt(LocalDateTime.now());
               Ride savedRide = rideService.updateRideStatus(ride,RideStatus.ONGOING);

        String emailContent = String.format(
                "<!DOCTYPE html>" +
                        "<html lang='en'>" +
                        "<head>" +
                        "    <meta charset='UTF-8'>" +
                        "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                        "    <style>" +
                        "        body {" +
                        "            font-family: Arial, sans-serif;" +
                        "            margin: 0;" +
                        "            padding: 0;" +
                        "            background-color: #f4f4f4;" +
                        "        }" +
                        "        .email-container {" +
                        "            max-width: 600px;" +
                        "            margin: 20px auto;" +
                        "            background: #ffffff;" +
                        "            border-radius: 8px;" +
                        "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);" +
                        "            overflow: hidden;" +
                        "        }" +
                        "        .header {" +
                        "            background-color: #007BFF;" +
                        "            color: #ffffff;" +
                        "            padding: 20px;" +
                        "            text-align: center;" +
                        "        }" +
                        "        .content {" +
                        "            padding: 20px;" +
                        "            color: #333333;" +
                        "            line-height: 1.6;" +
                        "        }" +
                        "        .content p {" +
                        "            margin: 10px 0;" +
                        "        }" +
                        "        .ride-details {" +
                        "            background-color: #f9f9f9;" +
                        "            border: 1px solid #ddd;" +
                        "            padding: 15px;" +
                        "            border-radius: 5px;" +
                        "            margin: 20px 0;" +
                        "        }" +
                        "        .ride-details p {" +
                        "            margin: 8px 0;" +
                        "        }" +
                        "        .otp-box {" +
                        "            text-align: center;" +
                        "            margin: 20px;" +
                        "            padding: 20px;" +
                        "            background-color: #fff4e5;" +
                        "            border: 2px dashed #FF9800;" +
                        "            border-radius: 8px;" +
                        "            font-size: 24px;" +
                        "            font-weight: bold;" +
                        "            color: #FF9800;" +
                        "        }" +
                        "        .footer {" +
                        "            background-color: #f4f4f4;" +
                        "            padding: 10px;" +
                        "            text-align: center;" +
                        "            color: #666;" +
                        "            font-size: 0.9em;" +
                        "        }" +
                        "        .footer a {" +
                        "            color: #007BFF;" +
                        "            text-decoration: none;" +
                        "        }" +
                        "    </style>" +
                        "</head>" +
                        "<body>" +
                        "    <div class='email-container'>" +
                        "        <div class='header'>" +
                        "            <h1>Your Ride Has Started!</h1>" +
                        "        </div>" +
                        "        <div class='otp-box'>" +
                        "            OTP for Your Ride: %s" +
                        "        </div>" +
                        "        <div class='content'>" +
                        "            <p><h2>Dear %s,</h2></p>" +
                        "            <p>Your ride has started. Below are the details of your ride:</p>" +
                        "            <div class='ride-details'>" +
                        "                <p><strong>Pickup Location:</strong> %s</p>" +
                        "                <p><strong>Dropoff Location:</strong> %s</p>" +
                        "                <p><strong>Driver Name:</strong> %s</p>" +
                        "                <p><strong>Vehicle Number:</strong> %s</p>" +
                        "                <p><strong>Fare:</strong> ₹%.2f</p>" +
                        "                <p><strong>Start Time:</strong> %s</p>" +
                        "            </div>" +
                        "            <p>We hope you have a pleasant journey!</p>" +
                        "        </div>" +
                        "        <div class='footer'>" +
                        "            <p>Best Regards,<br>Team MoveInSync</p>" +
                        "            <p><a href='vshlsgite.98.vg@gmail.com'>Mail to us </a></p>" +
                        "        </div>" +
                        "    </div>" +
                        "</body>" +
                        "</html>",
                rideDTO.getOtp(), // OTP
                rideDTO.getRider().getUser().getName(), // Rider's name
                rideDTO.getPickUpLocation(), // Pickup location
                rideDTO.getDropOffLocation(), // Dropoff location
                rideDTO.getDriver().getUser().getName(), // Driver's name
                rideDTO.getDriver().getVehicleId(), // Vehicle number
                rideDTO.getFare(), // Fare
                rideDTO.getStartedAt() // Start time
        );
// Send email
        emailSenderService.sendEmail(
                rideDTO.getRider().getUser().getEmail(),
                "Ride Started ..!!",
                emailContent
        );

        ratingService.createNewRating(savedRide);
               return modelMapper.map(savedRide ,RideDTO.class);
    }

    @Override
    public RideDTO endRide(Long rideId , String otp) {

        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        RideDTO rideDTO = modelMapper.map(ride, RideDTO.class);
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver is not associated with this ride ");
        }

        if(!ride.getRideStatus().equals(RideStatus.ONGOING)){
            throw new RuntimeException("Ride status is not ONGOING  , " +
                    "So ride Cannot be ENDED  "+ride.getRideStatus());
        }

        if(!otp.equals(ride.getOtp())){
            throw new RuntimeException("Invalid OTP ");
        }

        ride.setEndedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride,RideStatus.ENDED);
        updateDriveravailablity(driver,true);

        String emailContent = String.format(
                "<!DOCTYPE html>" +
                        "<html lang='en'>" +
                        "<head>" +
                        "    <meta charset='UTF-8'>" +
                        "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                        "    <style>" +
                        "        body {" +
                        "            font-family: Arial, sans-serif;" +
                        "            margin: 0;" +
                        "            padding: 0;" +
                        "            background-color: #f4f4f4;" +
                        "        }" +
                        "        .email-container {" +
                        "            max-width: 600px;" +
                        "            margin: 20px auto;" +
                        "            background: #ffffff;" +
                        "            border-radius: 8px;" +
                        "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);" +
                        "            overflow: hidden;" +
                        "        }" +
                        "        .header {" +
                        "            background-color: #4CAF50;" +
                        "            color: #ffffff;" +
                        "            padding: 20px;" +
                        "            text-align: center;" +
                        "        }" +
                        "        .content {" +
                        "            padding: 20px;" +
                        "            color: #333333;" +
                        "            line-height: 1.6;" +
                        "        }" +
                        "        .content p {" +
                        "            margin: 10px 0;" +
                        "        }" +
                        "        .ride-details {" +
                        "            background-color: #f9f9f9;" +
                        "            border: 1px solid #ddd;" +
                        "            padding: 15px;" +
                        "            border-radius: 5px;" +
                        "            margin: 20px 0;" +
                        "        }" +
                        "        .ride-details p {" +
                        "            margin: 8px 0;" +
                        "        }" +
                        "        .rate-driver {" +
                        "            text-align: center;" +
                        "            margin: 20px 0;" +
                        "        }" +
                        "        .rate-driver a {" +
                        "            display: inline-block;" +
                        "            background-color: #4CAF50;" +
                        "            color: #ffffff;" +
                        "            padding: 10px 20px;" +
                        "            text-decoration: none;" +
                        "            border-radius: 5px;" +
                        "            font-weight: bold;" +
                        "        }" +
                        "        .rate-driver a:hover {" +
                        "            background-color: #45a049;" +
                        "        }" +
                        "        .footer {" +
                        "            background-color: #f4f4f4;" +
                        "            padding: 10px;" +
                        "            text-align: center;" +
                        "            color: #666;" +
                        "            font-size: 0.9em;" +
                        "        }" +
                        "        .footer a {" +
                        "            color: #4CAF50;" +
                        "            text-decoration: none;" +
                        "        }" +
                        "    </style>" +
                        "</head>" +
                        "<body>" +
                        "    <div class='email-container'>" +
                        "        <div class='header'>" +
                        "            <h1>Your Ride Has Completed!</h1>" +
                        "        </div>" +
                        "        <div class='content'>" +
                        "            <p><h2>Dear %s,</h2></p>" +
                        "            <p>Your ride has been completed. Below are the details of your ride:</p>" +
                        "            <div class='ride-details'>" +
                        "                <p><strong>Pickup Location:</strong> %s</p>" +
                        "                <p><strong>Dropoff Location:</strong> %s</p>" +
                        "                <p><strong>Driver Name:</strong> %s</p>" +
                        "                <p><strong>Vehicle Number:</strong> %s</p>" +
                        "                <p><strong>Fare:</strong> ₹%.2f</p>" +
                        "                <p><strong>Start Time:</strong> %s</p>" +
                        "                <p><strong>End Time:</strong> %s</p>" +
                        "            </div>" +
                        "            <div class='rate-driver'>" +
                        "                <p>We value your feedback. Please rate your driver:</p>" +
                        "                <a href='http://localhost:8080/swagger-ui/index.html#/rider-controller/rateDriver'>Rate Driver</a>" +
                        "            </div>" +
                        "            <p>Thank you for using our service!</p>" +
                        "        </div>" +
                        "        <div class='footer'>" +
                        "            <p>Best Regards,<br>Team MoveInSync</p>" +
                        "            <p><a href='vshlsgite.98.vg@gmail.com'>Mail to us</a></p>" +
                        "        </div>" +
                        "    </div>" +
                        "</body>" +
                        "</html>",
                rideDTO.getRider().getUser().getName(), // Rider's name
                rideDTO.getPickUpLocation(), // Pickup location
                rideDTO.getDropOffLocation(), // Dropoff location
                rideDTO.getDriver().getUser().getName(), // Driver's name
                rideDTO.getDriver().getVehicleId(), // Vehicle number
                rideDTO.getFare(), // Fare
                rideDTO.getStartedAt(), // Start time
                rideDTO.getEndedAt(), // End time
                rideDTO.getId() // Ride ID for rate driver link
        );


// Send email
        emailSenderService.sendEmail(
                rideDTO.getRider().getUser().getEmail(),
                "Ride Ended Now  ..!!",
                emailContent
        );
        return modelMapper.map(savedRide ,RideDTO.class);
    }


    @Override
    public RiderDTO rateRider(Long rideId, Integer rating) {

        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver Cannot rate Rider , because " +
                    "Driver is not associated with this ride ");
        }

        if(!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("Ride is not ENDED yet , " +
                    "So ride Cannot be start rating  "+ride.getRideStatus());
        }

        return ratingService.rateRider(ride,rating);

    }

    @Override
    public DriverDTO getMyProfile() {

        Driver currentDriver= getCurrentDriver();

        return modelMapper.map(currentDriver,DriverDTO.class);

    }

    @Override
    public Page<RideDTO> getAllMyRides(PageRequest pageRequest) {

        Driver currentDriver = getCurrentDriver();

        return rideService.getAllRidesOfDriver(currentDriver, pageRequest).map(
                ride -> modelMapper.map(ride,RideDTO.class)
        );
    }

    @Override
    public Driver getCurrentDriver() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return driverRepository.findByUser(user).orElseThrow(() ->
                new ResourceNotFoundException("Driver Not Associated  With ID : "+user.getId()));
    }

    @Override
    public Driver updateDriveravailablity(Driver driver, boolean available) {


        driver.setAvailable(available);
        return driverRepository.save(driver);

    }

    @Override
    public Driver createNewDriver(Driver driver) {
        return driverRepository.save(driver);
    }


}
