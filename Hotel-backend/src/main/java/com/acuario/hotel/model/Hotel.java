package com.acuario.hotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hotel", uniqueConstraints = @UniqueConstraint(name = "uk_hotel_ruc", columnNames = "ruc"))
public class Hotel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 150)
	private String nombre;

	@Size(min = 11, max = 11)
	@Pattern(regexp = "^[0-9]{11}$", message = "El RUC debe tener exactamente 11 dígitos numéricos")
	private String ruc;

	@Size(max = 200)
	private String direccion;

	@Size(min = 9, max = 9)
	@Pattern(regexp = "^[0-9]{9}$", message = "El celular debe tener exactamente 9 dígitos numéricos")
	private String celular;

	@Email
	@Size(max = 150)
	private String email;

	@Column(nullable = false, updatable = false)
	private LocalDateTime fechaRegistro = LocalDateTime.now();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
}
