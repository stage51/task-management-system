package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.models.User;
import com.example.taskmanagementsystem.services.UserService;
import com.example.taskmanagementsystem.services.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserService userService;

    @Test
    void testLoadUserByUsername() {
        MockitoAnnotations.openMocks(this);

        String userEmail = "test@example.com";
        String userPassword = "password";
        User user = new User();
        user.setEmail(userEmail);
        user.setPassword(userPassword);

        when(userService.findByEmail(userEmail)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        assertEquals(userEmail, userDetails.getUsername());
        assertEquals(userPassword, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }
}
