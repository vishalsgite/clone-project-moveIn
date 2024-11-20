package com.vishal.project.moveInSync.moveInSyncApp.controllers;


import com.vishal.project.moveInSync.moveInSyncApp.dto.*;
import com.vishal.project.moveInSync.moveInSyncApp.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;


    @PostMapping("/signup")
    ResponseEntity<UserDTO> signUp(@RequestBody SignupDTO signupDTO) {
        return new ResponseEntity<>(authService.signup(signupDTO), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/onBoardNewDriver/{userId}")
    ResponseEntity<DriverDTO> onboardNewDriver(@PathVariable Long userId,
                                               @RequestBody OnboardDriverDTO onboardDriverDTO){

        return new ResponseEntity<>(authService.onboardNewDriver(userId,onboardDriverDTO.getVehicleId()),
                HttpStatus.CREATED);

    }
    @PostMapping("/login")
    ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO,
                                           HttpServletRequest httpServletRequest,
                                           HttpServletResponse httpServletResponse) {
        String tokens[] = authService.login(loginRequestDTO.getEmail(),loginRequestDTO.getPassword());
        Cookie cookie = new Cookie("token", tokens[1]);
        cookie.setHttpOnly(true);

        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok(new LoginResponseDTO(tokens[0]));
    }

    @GetMapping("/login/WithGoogle")
    public String loginWithGoogle() {
        // Redirect to Google OAuth2 Login URL
        return "redirect:/oauth2/authorization/google";
    }
    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<String> googleLoginCallback() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok("Google login successful: " + authentication.getName());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Google login failed");
    }


    @PostMapping("/refresh")
    ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request){
        String refreshToken = Arrays.stream(request.getCookies()).
                filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies"));

        String accessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new LoginResponseDTO(accessToken));

    }

}