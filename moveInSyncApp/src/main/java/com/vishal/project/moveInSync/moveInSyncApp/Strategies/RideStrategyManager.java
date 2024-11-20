package com.vishal.project.moveInSync.moveInSyncApp.Strategies;


import com.vishal.project.moveInSync.moveInSyncApp.Strategies.Impl.DriverMatchingHighestRatedDriverStrategy;
import com.vishal.project.moveInSync.moveInSyncApp.Strategies.Impl.DriverMatchingNearestDriverStrategy;
import com.vishal.project.moveInSync.moveInSyncApp.Strategies.Impl.RideFareSurgePricingFareCalculationStrategy;
import com.vishal.project.moveInSync.moveInSyncApp.Strategies.Impl.RiderFareDefaultFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RideStrategyManager {

    private final DriverMatchingHighestRatedDriverStrategy highestRatedDriverStrategy;
    private final DriverMatchingNearestDriverStrategy nearestDriverStrategy;
    private final RideFareSurgePricingFareCalculationStrategy surgePricingFareCalculationStrategy;
    private final RiderFareDefaultFareCalculationStrategy defaultFareCalculationStrategy;



    public DriverMatchingStrategy driverMatchingStrategy(double riderRating){

        if(riderRating >= 4.8 ){
            return highestRatedDriverStrategy;
        }else{
            return nearestDriverStrategy;
        }
    }

    public RideFareCalculationStrategy rideFareCalculationStrategy(){


        LocalTime  surgeStartTime = LocalTime.of(19,00);
        LocalTime  surgeEndTime = LocalTime.of(22,00);
        LocalTime currentTime = LocalTime.now();
        boolean isSurgeTime = currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);

        if(isSurgeTime){
            return surgePricingFareCalculationStrategy;
        }else{
            return defaultFareCalculationStrategy;
        }
    }

}
