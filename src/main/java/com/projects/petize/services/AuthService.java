package com.projects.petize.services;

import com.projects.petize.dtos.LoginRequestDTO;
import com.projects.petize.dtos.RegisterRequestDTO;
import com.projects.petize.dtos.ResponseDTO;

public interface AuthService {

    ResponseDTO login(LoginRequestDTO login);

    ResponseDTO register(RegisterRequestDTO register);
}
