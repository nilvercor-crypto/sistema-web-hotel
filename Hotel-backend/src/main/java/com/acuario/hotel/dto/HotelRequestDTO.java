package com.acuario.hotel.dto;

import jakarta.validation.constraints.*;

public class HotelRequestDTO {

	@NotBlank(message = "El nombre es obligatorio")
	@Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
	private String nombre;

	@Size(min = 11, max = 11, message = "El RUC debe tener exactamente 11 dígitos")
	@Pattern(regexp = "^[0-9]{11}$", message = "El RUC debe contener solo dígitos numéricos")
	private String ruc;

	@Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
	private String direccion;

	@Size(min = 9, max = 9, message = "El celular debe tener exactamente 9 dígitos")
	@Pattern(regexp = "^[0-9]{9}$", message = "El celular debe contener solo dígitos numéricos")
	private String celular;

	@Email(message = "El email debe tener un formato válido")
	@Size(max = 150, message = "El email no puede exceder 150 caracteres")
	private String email;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
