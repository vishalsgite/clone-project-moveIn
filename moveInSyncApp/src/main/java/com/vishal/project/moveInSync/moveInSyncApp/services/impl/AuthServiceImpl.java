package com.vishal.project.moveInSync.moveInSyncApp.services.impl;

import com.vishal.project.moveInSync.moveInSyncApp.Security.JWTService;
import com.vishal.project.moveInSync.moveInSyncApp.dto.DriverDTO;
import com.vishal.project.moveInSync.moveInSyncApp.dto.SignupDTO;
import com.vishal.project.moveInSync.moveInSyncApp.dto.UserDTO;
import com.vishal.project.moveInSync.moveInSyncApp.entities.Driver;
import com.vishal.project.moveInSync.moveInSyncApp.entities.User;
import com.vishal.project.moveInSync.moveInSyncApp.entities.enums.Role;
import static com.vishal.project.moveInSync.moveInSyncApp.entities.enums.Role.DRIVER;
import com.vishal.project.moveInSync.moveInSyncApp.exceptions.ResourceNotFoundException;
import com.vishal.project.moveInSync.moveInSyncApp.exceptions.RuntimeConflictsException;
import com.vishal.project.moveInSync.moveInSyncApp.repositories.UserRepository;
import com.vishal.project.moveInSync.moveInSyncApp.services.AuthService;
import com.vishal.project.moveInSync.moveInSyncApp.services.DriverService;
import com.vishal.project.moveInSync.moveInSyncApp.services.EmailSenderService;
import com.vishal.project.moveInSync.moveInSyncApp.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RiderService riderService;
    private final DriverService driverService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final EmailSenderService emailSenderService;




    @Override
    public String[] login(String email, String password) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email,password));

        User user = (User)authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateAccessToken(user);

        return new String[]{accessToken, refreshToken};
    }

   /* @Override
    @Transactional
    public UserDTO signup(SignupDTO signupDTO) {

     User user =  userRepository.findByEmail(signupDTO.getEmail()).orElse(null);

      if(user !=null)

      {

              throw new RuntimeConflictsException("Can't Signup , User already exists with email "+signupDTO.getEmail());

}
        User mappedUser =modelMapper.map(signupDTO , User.class);
        mappedUser.setRoles(Set.of(Role.RIDER));
        User savedUser = userRepository.save(mappedUser);

        // create user related entities
          riderService.createNewRider(savedUser);


          //TODO ; Wallet Related Service

        return modelMapper.map(savedUser,UserDTO.class);

    }  */

   /* @Override
    @Transactional
    public UserDTO signup(SignupDTO signupDTO) {
        // Define the logger for this class
        Logger log = LoggerFactory.getLogger(getClass());

        // Check if email is null or empty
        if (signupDTO.getEmail() == null || signupDTO.getEmail().isEmpty()) {
            log.error("Email is required for signup");
            throw new RuntimeConflictsException("Email is required");
        }

        // Log and check if user already exists
        log.debug("Checking if user exists with email: {}", signupDTO.getEmail());
        User user = userRepository.findByEmail(signupDTO.getEmail()).orElse(null);

        if (user != null) {
            log.error("User already exists with email: {}", signupDTO.getEmail());
            throw new RuntimeConflictsException("Can't Signup, User already exists with email " + signupDTO.getEmail());
        }

        // Map and save the new user
        User mappedUser = modelMapper.map(signupDTO, User.class);
        mappedUser.setRoles(Set.of(Role.RIDER));
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
        User savedUser = userRepository.save(mappedUser);

        // Create related rider entity
        riderService.createNewRider(savedUser);
emailSenderService.sendEmail(signupDTO.getEmail(),"SignUp Successfull","Your Account Has been Created Successfully");

        // Return the user DTO
        return modelMapper.map(savedUser, UserDTO.class);
    }
*/
   @Override
   @Transactional
   public UserDTO signup(SignupDTO signupDTO) {

       String htmlContent = "<html>" +
               "<body style='font-family: Arial, sans-serif; line-height: 1.6; color: #333;'>" +
               "<div style='background-color: #f4f4f4; padding: 20px; border-radius: 8px; max-width: 600px; margin: 0 auto;'>" +
               "<h1 style='color: #4CAF50; text-align: center;'>Hello " + signupDTO.getName() + ",</h1>" +
               "<p style='font-size: 16px; text-align: center;'>Your account has been created successfully.</p>" +
               "<p style='font-size: 16px; text-align: center;'>Thank you for signing up!</p>" +
               "</div>" +
               "</body>" +
               "</html>";

       // Define the logger for this class
       Logger log = LoggerFactory.getLogger(getClass());

       // Check if email is null or empty
       if (signupDTO.getEmail() == null || signupDTO.getEmail().isEmpty()) {
           log.error("Email is required for signup");
           throw new RuntimeConflictsException("Email is required");
       }

       // Log and check if user already exists
       log.debug("Checking if user exists with email: {}", signupDTO.getEmail());
       User user = userRepository.findByEmail(signupDTO.getEmail()).orElse(null);

       if (user != null) {
           log.error("User already exists with email: {}", signupDTO.getEmail());
           throw new RuntimeConflictsException("Can't Signup, User already exists with email " + signupDTO.getEmail());
       }

       // Map and save the new user
       User mappedUser = modelMapper.map(signupDTO, User.class);

       // Set roles based on email condition
       if ("vishalgite@gmail.com".equalsIgnoreCase(signupDTO.getEmail())) {
           mappedUser.setRoles(Set.of(Role.ADMIN));
       } else {
           mappedUser.setRoles(Set.of(Role.RIDER));
       }

       mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
       User savedUser = userRepository.save(mappedUser);

       // Create related rider entity
       riderService.createNewRider(savedUser);
       emailSenderService.sendEmail(signupDTO.getEmail(), "SignUp Successfully", htmlContent);
       // Return the user DTO
       return modelMapper.map(savedUser, UserDTO.class);
   }


    @Override
    public DriverDTO onboardNewDriver(Long userId, String vehicleId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(
                "User Not Found to Onboard as a Driver  "));

        if (user.getRoles().contains(DRIVER)) throw new RuntimeException(
                "User Already Assigned As Driver "
        );

        Driver createDriver = Driver.builder()
                .user(user)
                .rating(0.0)
                .vehicleId(vehicleId)
                .available(true)
                .build();

        user.getRoles().add(DRIVER);
        userRepository.save(user);

        Driver savedDriver = driverService.createNewDriver(createDriver);
        return modelMapper.map(savedDriver, DriverDTO.class);
    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found " +
                "with id: "+userId));

        return jwtService.generateAccessToken(user);
    }
}
