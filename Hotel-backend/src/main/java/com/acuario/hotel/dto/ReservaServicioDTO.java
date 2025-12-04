package com.acuario.hotel.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ReservaServicioDTO {

	private Long id;

	@NotNull(message = "El servicio es obligatorio")
	private Long servicioId;

	private String servicioNombre;

	private BigDecimal servicioPrecio;

	@NotNull(message = "La cantidad es obligatoria")
	@Min(value = 1, message = "La cantidad debe ser al menos 1")
	private Integer cantidad;

	private BigDecimal subtotal;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getServicioId() {
		return servicioId;
	}

	public void setServicioId(Long servicioId) {
		this.servicioId = servicioId;
	}

	public String getServicioNombre() {
		return servicioNombre;
	}

	public void setServicioNombre(String servicioNombre) {
		this.servicioNombre = servicioNombre;
	}

	public BigDecimal getServicioPrecio() {
		return servicioPrecio;
	}

	public void setServicioPrecio(BigDecimal servicioPrecio) {
		this.servicioPrecio = servicioPrecio;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
}

