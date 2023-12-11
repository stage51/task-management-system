package com.example.taskmanagementsystem.controllers;

import com.example.taskmanagementsystem.dtos.RegistrationUserDTO;
import com.example.taskmanagementsystem.dtos.UserDTO;
import com.example.taskmanagementsystem.dtos.jwts.JwtRequest;
import com.example.taskmanagementsystem.dtos.jwts.JwtResponse;
import com.example.taskmanagementsystem.dtos.views.UserViewDTO;
import com.example.taskmanagementsystem.services.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@Slf4j
public class AuthController {
    private AuthService authService;
    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        log.info("Handling POST request for /api/auth");
        log.info("Logging in");
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDTO registrationUserDto) {
        log.info("Handling POST request for /api/registration");
        log.info("Registering");
        return authService.createNewUser(registrationUserDto);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        log.info("Handling POST request for /api/logout");
        log.info("Clearing current session");
        return ResponseEntity.ok().build();
    }
}

