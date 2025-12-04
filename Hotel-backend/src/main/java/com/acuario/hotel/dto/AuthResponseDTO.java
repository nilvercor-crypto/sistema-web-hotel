package com.acuario.hotel.dto;

import com.acuario.hotel.model.Rol;

public class AuthResponseDTO {
	private String token;
	private String tipoToken = "Bearer";
	private Long id;
	private String nombreUsuario;
	private Rol rol;

	public AuthResponseDTO() {
	}

	public AuthResponseDTO(String token, Long id, String nombreUsuario, Rol rol) {
		this.token = token;
		this.id = id;
		this.nombreUsuario = nombreUsuario;
		this.rol = rol;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTipoToken() {
		return tipoToken;
	}

	public void setTipoToken(String tipoToken) {
		this.tipoToken = tipoToken;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}
}

