package com.acuario.hotel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "servicios")
public class Servicio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "El nombre del servicio es obligatorio")
	@Size(max = 100, message = "El nombre del servicio no puede exceder 100 caracteres")
	@Column(name = "nombre_servicio", nullable = false, length = 100)
	private String nombreServicio;

	@Size(max = 250, message = "La descripción no puede exceder 250 caracteres")
	@Column(name = "descripcion", length = 250)
	private String descripcion;

	@NotNull(message = "El precio es obligatorio")
	@DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
	@Digits(integer = 8, fraction = 2, message = "El precio debe tener máximo 8 dígitos enteros y 2 decimales")
	@Column(name = "precio", nullable = false, precision = 10, scale = 2)
	private BigDecimal precio;

	@JsonIgnore
	@OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReservaServicio> reservas = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombreServicio() {
		return nombreServicio;
	}

	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public List<ReservaServicio> getReservas() {
		return reservas;
	}

	public void setReservas(List<ReservaServicio> reservas) {
		this.reservas = reservas;
	}
}

