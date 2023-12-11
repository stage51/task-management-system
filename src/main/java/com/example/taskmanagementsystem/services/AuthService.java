package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.dtos.RegistrationUserDTO;
import com.example.taskmanagementsystem.dtos.jwts.JwtRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest);
    ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDTO registrationUserDto);
}
