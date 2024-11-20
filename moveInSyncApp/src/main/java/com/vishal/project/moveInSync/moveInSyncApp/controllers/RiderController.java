package com.vishal.project.moveInSync.moveInSyncApp.controllers;


import com.vishal.project.moveInSync.moveInSyncApp.dto.*;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Driver;
import com.vishal.project.moveInSync.moveInSyncApp.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rider")
@RequiredArgsConstructor
@Secured("ROLE_RIDER")
public class RiderController {


    private final RiderService riderService;

    @PostMapping("/requestRide")
    public ResponseEntity<RideRequestDTO> requestRide(@RequestBody RideRequestDTO rideRequestDTO){
       return ResponseEntity.ok(riderService.requestRide(rideRequestDTO));
    }

    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RideDTO> cancelRide(@PathVariable Long rideId){
        return ResponseEntity.ok(riderService.cancelRide(rideId));
    }

    @PostMapping("/rateDriver/{rideId}")
    public ResponseEntity<DriverDTO> rateDriver(@RequestBody RatingDTO ratingDTO){
        return ResponseEntity.ok(riderService.rateDriver(ratingDTO.getRideId(), ratingDTO
                .getRating()));
    }

    @GetMapping("/getMyProfile")
    public ResponseEntity<RiderDTO> getMyProfile(){
        return ResponseEntity.ok(riderService.getMyProfile());
    }
    @GetMapping("/getAllMyRides")
    public ResponseEntity<Page<RideDTO>> getAllMyRides(@RequestParam(defaultValue = "0") Integer pageOffSet,
                                                       @RequestParam(defaultValue = "10",required = false) Integer pageSize)

    {
        PageRequest pageRequest = PageRequest.of(pageOffSet,pageSize, Sort.by(Sort.Direction.DESC,"createdTime","id"));

        return ResponseEntity.ok(riderService.getAllMyRides(pageRequest));
    }
  /*  @PostMapping("/rateDriver/{driverId}/{rating}")
    public ResponseEntity<DriverDTO> rateDriver(@PathVariable Long driverId,
                                              @PathVariable Integer rating){
        return ResponseEntity.ok(riderService.rateDriver(driverId,rating));
    }
    */

}


/*
createNewRider
getCurrentRider
 */