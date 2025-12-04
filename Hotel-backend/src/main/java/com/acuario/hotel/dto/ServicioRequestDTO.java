package com.acuario.hotel.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ServicioRequestDTO {

	@NotBlank(message = "El nombre del servicio es obligatorio")
	@Size(max = 100, message = "El nombre del servicio no puede exceder 100 caracteres")
	private String nombreServicio;

	@Size(max = 250, message = "La descripción no puede exceder 250 caracteres")
	private String descripcion;

	@NotNull(message = "El precio es obligatorio")
	@DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
	@Digits(integer = 8, fraction = 2, message = "El precio debe tener máximo 8 dígitos enteros y 2 decimales")
	private BigDecimal precio;

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
}



