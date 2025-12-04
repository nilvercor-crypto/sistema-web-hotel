package com.acuario.hotel.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {
	@NotBlank(message = "El nombre de usuario es obligatorio")
	private String nombreUsuario;

	@NotBlank(message = "La contraseña es obligatoria")
	private String contrasena;

	public LoginRequestDTO() {
	}

	public LoginRequestDTO(String nombreUsuario, String contrasena) {
		this.nombreUsuario = nombreUsuario;
		this.contrasena = contrasena;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
}

