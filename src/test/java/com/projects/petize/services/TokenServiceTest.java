package com.projects.petize.services;

import com.projects.petize.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenServiceImpl tokenService;

    @BeforeEach
    void setup() {
        tokenService = new TokenServiceImpl();
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(tokenService, "secret", "mysecretkey");
    }

    @Test
    void generateToken_shouldReturnToken() {
        User user = new User();
        user.setEmail("test@example.com");

        String token = tokenService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void validateToken_shouldReturnSubject_whenValid() {
        User user = new User();
        user.setEmail("test@example.com");
        String token = tokenService.generateToken(user);

        String subject = tokenService.validateToken(token);

        assertEquals("test@example.com", subject);
    }

    @Test
    void validateToken_shouldReturnNull_whenInvalid() {
        String invalidToken = "token.invalid.here";

        String subject = tokenService.validateToken(invalidToken);

        assertNull(subject);
    }
}
