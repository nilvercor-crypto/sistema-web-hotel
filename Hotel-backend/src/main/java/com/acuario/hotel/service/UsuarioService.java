package com.acuario.hotel.service;

import com.acuario.hotel.dto.AuthResponseDTO;
import com.acuario.hotel.dto.LoginRequestDTO;
import com.acuario.hotel.dto.RegisterRequestDTO;

public interface UsuarioService {
	AuthResponseDTO login(LoginRequestDTO loginRequest);
	AuthResponseDTO register(RegisterRequestDTO registerRequest);
}

