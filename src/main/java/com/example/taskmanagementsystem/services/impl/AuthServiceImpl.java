package com.example.taskmanagementsystem.services.impl;

import com.example.taskmanagementsystem.dtos.RegistrationUserDTO;
import com.example.taskmanagementsystem.dtos.jwts.JwtRequest;
import com.example.taskmanagementsystem.dtos.jwts.JwtResponse;
import com.example.taskmanagementsystem.dtos.views.UserViewDTO;
import com.example.taskmanagementsystem.exceptions.AppError;
import com.example.taskmanagementsystem.exceptions.IncorrectLoginOrPasswordException;
import com.example.taskmanagementsystem.models.User;
import com.example.taskmanagementsystem.services.AuthService;
import com.example.taskmanagementsystem.services.CommentService;
import com.example.taskmanagementsystem.services.UserService;
import com.example.taskmanagementsystem.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class AuthServiceImpl implements AuthService {
    private UserDetailsService userDetailsService;
    private UserService userService;
    private JwtTokenUtils jwtTokenUtils;
    private AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setJwtTokenUtils(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            logger.error("Incorrect login or password");
            throw new IncorrectLoginOrPasswordException("Incorrect login or password");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        logger.info("Created token " + token);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDTO registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            logger.error("Passwords don't match");
            throw new IncorrectLoginOrPasswordException("Passwords don't match");
        }
        if (userService.findByEmail(registrationUserDto.getEmail()).isPresent()) {
            logger.error("User with email " + registrationUserDto.getEmail() + " is exist");
            throw new IncorrectLoginOrPasswordException("User with this email is exist");
        }
        UserViewDTO user = userService.create(registrationUserDto);
        logger.info("Registered user with id " + user.getId());
        return ResponseEntity.ok(user);
    }
}