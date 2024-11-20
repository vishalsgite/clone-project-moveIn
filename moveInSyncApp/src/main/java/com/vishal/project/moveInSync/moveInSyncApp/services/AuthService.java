package com.vishal.project.moveInSync.moveInSyncApp.services;

import com.vishal.project.moveInSync.moveInSyncApp.dto.DriverDTO;
import com.vishal.project.moveInSync.moveInSyncApp.dto.SignupDTO;
import com.vishal.project.moveInSync.moveInSyncApp.dto.UserDTO;

public interface AuthService {

    String[] login(String email, String password);

    UserDTO signup(SignupDTO signupDTO);

    DriverDTO onboardNewDriver(Long userId,String vehicleId);

    String refreshToken(String refreshToken);

}
