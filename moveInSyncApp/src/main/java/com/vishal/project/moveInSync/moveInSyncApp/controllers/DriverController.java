package com.vishal.project.moveInSync.moveInSyncApp.controllers;


import com.vishal.project.moveInSync.moveInSyncApp.dto.*;
import com.vishal.project.moveInSync.moveInSyncApp.services.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/drivers")
@Secured("ROLE_DRIVER")
public class DriverController {

    private final DriverService driverService;

    @PostMapping("/acceptRide/{rideRequestId}")
    public ResponseEntity<RideDTO> acceptRide(@PathVariable Long rideRequestId){

        return ResponseEntity.ok(driverService.acceptRide(rideRequestId));
    }

    @PostMapping("/startRide/{rideRequestId}")
    public ResponseEntity<RideDTO> acceptRide(@PathVariable Long rideRequestId ,
                                              @RequestBody RideStartDTO rideStartDTO){

        return ResponseEntity.ok(driverService.startRide(rideRequestId, rideStartDTO.getOtp()));
    }
    @PostMapping("/NoShow/{rideRequestId}")
    public ResponseEntity<RideDTO> NoShowRide(@PathVariable Long rideRequestId ,
                                              @RequestBody RideStartDTO rideStartDTO){

        return ResponseEntity.ok(driverService.noShowRide(rideRequestId, rideStartDTO.getNoShowOtp()));
    }
    @PostMapping("/endRide/{rideId}")
    public ResponseEntity<RideDTO> endRide(@PathVariable Long rideId ,
                                              @RequestBody RideStartDTO rideStartDTO){

        return ResponseEntity.ok(driverService.endRide(rideId, rideStartDTO.getOtp()));
    }

    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RideDTO> cancelRide(@PathVariable Long rideId){
        return ResponseEntity.ok(driverService.cancelRide(rideId));
    }

    @PostMapping("/rateRider/{rideId}")
    public ResponseEntity<RiderDTO> rateRider(@RequestBody RatingDTO ratingDTO){
        return ResponseEntity.ok(driverService.rateRider(ratingDTO.getRideId(), ratingDTO
                .getRating()));
    }

    @GetMapping("/getMyProfile")
    public ResponseEntity<DriverDTO> getMyProfile(){
        return ResponseEntity.ok(driverService.getMyProfile());
    }
    @GetMapping("/getAllMyRides")
    public ResponseEntity<Page<RideDTO>> getAllMyRides(@RequestParam(defaultValue = "0") Integer pageOffSet,
                                                       @RequestParam(defaultValue = "10",required = false) Integer pageSize)

    {
        PageRequest pageRequest = PageRequest.of(pageOffSet,pageSize, Sort.by(Sort.Direction.DESC,"createdTime","id"));

        return ResponseEntity.ok(driverService.getAllMyRides(pageRequest));
    }
   /* @PostMapping("/rateRider/{riderId}/{rating}")
    public ResponseEntity<RiderDTO> rateRider(@PathVariable Long riderId,
                                              @PathVariable Integer rating){
        return ResponseEntity.ok(driverService.rateRider(riderId,rating));
    }
*/
}

/*
cancelRide

rateRider
getMyProfile
getAllMyRides
getCurrentDriver
updateDriveravailablity
 */