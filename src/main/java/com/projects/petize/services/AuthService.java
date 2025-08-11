package com.projects.petize.services;

import com.projects.petize.dtos.LoginRequestDTO;
import com.projects.petize.dtos.RegisterRequestDTO;
import com.projects.petize.dtos.ResponseDTO;
import com.projects.petize.entities.User;
import com.projects.petize.exceptions.EmailAlreadyRegisteredException;
import com.projects.petize.exceptions.InvalidCredentialsException;
import com.projects.petize.exceptions.UserNotFoundException;
import com.projects.petize.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public ResponseDTO login(LoginRequestDTO login) {
        User user = this.userRepository.findByEmail(login.email())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(login.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = this.tokenService.generateToken(user);
        return new ResponseDTO(user.getName(), token);
    }

    public ResponseDTO register(RegisterRequestDTO register) {
        Optional<User> existingUser = this.userRepository.findByEmail(register.email());

        if (existingUser.isPresent()) {
            throw new EmailAlreadyRegisteredException();
        }

        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(register.password()));
        newUser.setEmail(register.email());
        newUser.setName(register.name());
        this.userRepository.save(newUser);

        String token = this.tokenService.generateToken(newUser);
        return new ResponseDTO(newUser.getName(), token);
    }
}
