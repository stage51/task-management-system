package com.example.taskmanagementsystem.utils;

import com.example.taskmanagementsystem.dtos.UserDTO;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ValidationUtilsImplTest {

    @Autowired
    private ValidationUtils validationUtils;

    private static UserDTO dto = new UserDTO();


    @Test
    public void testValidationWithValidData() {
        assertTrue(validationUtils.isValid(dto));
    }

    @Test
    public void                     testValidationWithInvalidEmail() {
        dto.setEmail("invalid-email");
        assertFalse(validationUtils.isValid(dto));
    }

    @Test
    public void testValidationWithInvalidUsername() {
        dto.setUsername("u");
        assertFalse(validationUtils.isValid(dto));
    }

    @Test
    public void testValidationWithInvalidPassword() {
        dto.setPassword("pass");
        assertFalse(validationUtils.isValid(dto));
    }
}
