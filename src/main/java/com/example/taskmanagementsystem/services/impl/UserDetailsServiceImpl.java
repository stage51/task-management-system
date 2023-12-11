package com.example.taskmanagementsystem.services.impl;

import com.example.taskmanagementsystem.exceptions.IncorrectLoginOrPasswordException;
import com.example.taskmanagementsystem.models.User;
import com.example.taskmanagementsystem.services.AuthService;
import com.example.taskmanagementsystem.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

    private UserService userService;
    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws IncorrectLoginOrPasswordException{
        Optional<User> user = userService.findByEmail(email);

        if (!user.isPresent()) {
            logger.error("User not found with email " + email);
            throw new IncorrectLoginOrPasswordException("Incorrect login or password");
        }

        return new org.springframework.security.core.userdetails.User(
                user.get().getEmail(), user.get().getPassword(), new ArrayList<>()
        );
    }
}

