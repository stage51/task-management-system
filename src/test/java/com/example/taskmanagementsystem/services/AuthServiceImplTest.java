package com.example.taskmanagementsystem.services;

import com.example.taskmanagementsystem.dtos.RegistrationUserDTO;
import com.example.taskmanagementsystem.dtos.jwts.JwtRequest;
import com.example.taskmanagementsystem.dtos.jwts.JwtResponse;
import com.example.taskmanagementsystem.dtos.views.UserViewDTO;
import com.example.taskmanagementsystem.exceptions.IncorrectLoginOrPasswordException;
import com.example.taskmanagementsystem.models.User;
import com.example.taskmanagementsystem.services.impl.AuthServiceImpl;
import com.example.taskmanagementsystem.utils.JwtTokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAuthTokenSuccessfulAuthentication() {
        JwtRequest authRequest = new JwtRequest("test@example.com", "password");
        UserDetails userDetails = mock(UserDetails.class);
        String token = "testToken";

        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        when(userDetailsService.loadUserByUsername(authRequest.getEmail())).thenReturn(userDetails);
        when(jwtTokenUtils.generateToken(userDetails)).thenReturn(token);

        ResponseEntity<?> response = authService.createAuthToken(authRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof JwtResponse);

        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals(token, jwtResponse.getToken());

        verify(authenticationManager, times(1)).authenticate(any());
        verify(userDetailsService, times(1)).loadUserByUsername(authRequest.getEmail());
        verify(jwtTokenUtils, times(1)).generateToken(userDetails);
    }

    @Test
    void testCreateAuthTokenBadCredentials() {
        JwtRequest authRequest = new JwtRequest("test@example.com", "invalidPassword");

        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

        assertThrows(IncorrectLoginOrPasswordException.class, () -> authService.createAuthToken(authRequest));

        verify(authenticationManager, times(1)).authenticate(any());
        verifyNoInteractions(userDetailsService, jwtTokenUtils);
    }

    @Test
    void testCreateNewUserSuccessfulRegistration() {
        RegistrationUserDTO registrationUserDTO = new RegistrationUserDTO();
        registrationUserDTO.setEmail("test@example.com");
        registrationUserDTO.setPassword("password");
        registrationUserDTO.setConfirmPassword("password");

        when(userService.findByEmail(registrationUserDTO.getEmail())).thenReturn(Optional.empty());
        when(userService.create(registrationUserDTO)).thenReturn(new UserViewDTO());

        ResponseEntity<?> response = authService.createNewUser(registrationUserDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof UserViewDTO);

        verify(userService, times(1)).findByEmail(registrationUserDTO.getEmail());
        verify(userService, times(1)).create(registrationUserDTO);
    }

    @Test
    void testCreateNewUserPasswordsDoNotMatch() {
        RegistrationUserDTO registrationUserDTO = new RegistrationUserDTO();
        registrationUserDTO.setPassword("password");
        registrationUserDTO.setConfirmPassword("mismatchedPassword");

        assertThrows(IncorrectLoginOrPasswordException.class, () -> authService.createNewUser(registrationUserDTO));

        verifyNoInteractions(userService);
    }

    @Test
    void testCreateNewUserWithEmailExists() {
        RegistrationUserDTO registrationUserDTO = new RegistrationUserDTO();
        registrationUserDTO.setEmail("test@example.com");
        registrationUserDTO.setPassword("password");
        registrationUserDTO.setConfirmPassword("password");

        when(userService.findByEmail(registrationUserDTO.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(IncorrectLoginOrPasswordException.class, () -> authService.createNewUser(registrationUserDTO));

        verify(userService, times(1)).findByEmail(registrationUserDTO.getEmail());
        verifyNoMoreInteractions(userService);
    }
}

