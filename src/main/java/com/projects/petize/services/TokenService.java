package com.projects.petize.services;

import com.projects.petize.entities.User;

public interface TokenService {
    String generateToken(User user);

    String validateToken(String token);
}
