package com.example.taskmanagementsystem.controllers;

import com.example.taskmanagementsystem.dtos.RegistrationUserDTO;
import com.example.taskmanagementsystem.dtos.UserDTO;
import com.example.taskmanagementsystem.dtos.jwts.JwtRequest;
import com.example.taskmanagementsystem.dtos.jwts.JwtResponse;
import com.example.taskmanagementsystem.dtos.views.UserViewDTO;
import com.example.taskmanagementsystem.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@Slf4j
@Tag(name = "Authentication", description = "Authorization is required to work with the rest of the application")
public class AuthController {
    private AuthService authService;
    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }
    @Operation(summary = "The user enters an email and password, and after successful login receives a JWT token", tags = "Authentication")
    @ApiResponse()
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        log.info("Handling POST request for /api/auth");
        log.info("Logging in");
        return authService.createAuthToken(authRequest);
    }
    @Operation(summary = "The user enters the nickname, email, password and password again, and after successful entry creates an account", tags = "Registration")
    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDTO registrationUserDto) {
        log.info("Handling POST request for /api/registration");
        log.info("Registering");
        return authService.createNewUser(registrationUserDto);
    }
    @Operation(summary = "Exiting the session", tags = "Logout")
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        log.info("Handling POST request for /api/logout");
        log.info("Clearing current session");
        return ResponseEntity.ok().build();
    }
}

