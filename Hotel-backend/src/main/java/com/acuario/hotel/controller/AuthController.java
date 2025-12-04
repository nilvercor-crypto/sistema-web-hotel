package com.acuario.hotel.controller;

import com.acuario.hotel.dto.AuthResponseDTO;
import com.acuario.hotel.dto.LoginRequestDTO;
import com.acuario.hotel.dto.RegisterRequestDTO;
import com.acuario.hotel.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "auth-controller", description = "Autenticación y registro de usuarios")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UsuarioService usuarioService;

	public AuthController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@Operation(
		summary = "Iniciar sesión",
		description = "Autentica un usuario con nombre de usuario y contraseña. Retorna un token JWT que debe incluirse en el header 'Authorization: Bearer {token}' para acceder a los demás endpoints. No requiere autenticación previa."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Login exitoso, retorna token JWT y datos del usuario",
		content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))
	)
	@ApiResponse(responseCode = "400", description = "Datos inválidos (nombre de usuario o contraseña vacíos)", content = @Content)
	@ApiResponse(responseCode = "401", description = "Credenciales inválidas o usuario inactivo", content = @Content)
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
		AuthResponseDTO response = usuarioService.login(loginRequest);
		return ResponseEntity.ok(response);
	}

	@Operation(
		summary = "Registrar nuevo usuario",
		description = "Crea un nuevo usuario en el sistema. Por defecto, los usuarios nuevos tienen rol USUARIO. Retorna un token JWT automáticamente. No requiere autenticación previa."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Usuario registrado exitosamente, retorna token JWT y datos del usuario",
		content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))
	)
	@ApiResponse(responseCode = "400", description = "Datos inválidos o nombre de usuario ya existe", content = @Content)
	@PostMapping("/register")
	public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
		AuthResponseDTO response = usuarioService.register(registerRequest);
		return ResponseEntity.ok(response);
	}
}

