package com.projects.petize.services;

import com.projects.petize.dtos.LoginRequestDTO;
import com.projects.petize.dtos.RegisterRequestDTO;
import com.projects.petize.entities.User;
import com.projects.petize.exceptions.EmailAlreadyRegisteredException;
import com.projects.petize.exceptions.InvalidCredentialsException;
import com.projects.petize.exceptions.UserNotFoundException;
import com.projects.petize.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_shouldReturnResponseDTO_whenCredentialsValid() {
        String email = "user@example.com";
        String password = "pass";

        User user = new User();
        user.setEmail(email);
        user.setPassword("encodedPass");
        user.setName("User");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "encodedPass")).thenReturn(true);
        when(tokenService.generateToken(user)).thenReturn("token");

        var response = authService.login(new LoginRequestDTO(email, password));

        assertEquals("User", response.name());
        assertEquals("token", response.token());
    }

    @Test
    void login_shouldThrowUserNotFoundException_whenUserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.login(new LoginRequestDTO("x", "y")));
    }

    @Test
    void login_shouldThrowInvalidCredentialsException_whenPasswordMismatch() {
        User user = new User();
        user.setPassword("encodedPass");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), eq("encodedPass"))).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(new LoginRequestDTO("x", "y")));
    }

    @Test
    void register_shouldThrowEmailAlreadyRegisteredException_whenEmailExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        var dto = new RegisterRequestDTO("User", "user@example.com", "password");

        assertThrows(EmailAlreadyRegisteredException.class, () -> authService.register(dto));
    }

    @Test
    void register_shouldReturnResponseDTO_whenSuccess() {
        RegisterRequestDTO dto = new RegisterRequestDTO("User", "user@example.com", "password");

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(tokenService.generateToken(any(User.class))).thenReturn("token");

        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        }).when(userRepository).save(any());

        when(passwordEncoder.encode(dto.password())).thenReturn("encodedPass");

        var response = authService.register(dto);

        assertEquals("User", response.name());
        assertEquals("token", response.token());
    }
}
