package com.acuario.hotel.dto;

import com.acuario.hotel.model.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequestDTO {
	@NotBlank(message = "El nombre de usuario es obligatorio")
	@Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
	private String nombreUsuario;

	@NotBlank(message = "La contraseña es obligatoria")
	@Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
	private String contrasena;

	private Rol rol = Rol.USUARIO;

	public RegisterRequestDTO() {
	}

	public RegisterRequestDTO(String nombreUsuario, String contrasena, Rol rol) {
		this.nombreUsuario = nombreUsuario;
		this.contrasena = contrasena;
		this.rol = rol;
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

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}
}

