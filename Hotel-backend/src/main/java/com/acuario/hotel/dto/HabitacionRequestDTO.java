package com.acuario.hotel.dto;

import com.acuario.hotel.model.EstadoHabitacion;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class HabitacionRequestDTO {

	@NotBlank(message = "El número de habitación es obligatorio")
	@Size(max = 10, message = "El número no puede exceder 10 caracteres")
	private String numero;

	@NotBlank(message = "El tipo de habitación es obligatorio")
	@Size(max = 50, message = "El tipo no puede exceder 50 caracteres")
	private String tipo;

	@Min(value = 1, message = "La capacidad debe ser al menos 1")
	private int capacidad;

	@NotNull(message = "El precio por noche es obligatorio")
	@DecimalMin(value = "0.00", message = "El precio debe ser mayor o igual a 0")
	@Digits(integer = 8, fraction = 2, message = "El precio debe tener máximo 8 dígitos enteros y 2 decimales")
	private BigDecimal precioPorNoche;

	@NotNull(message = "El estado es obligatorio")
	private EstadoHabitacion estado;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public BigDecimal getPrecioPorNoche() {
		return precioPorNoche;
	}

	public void setPrecioPorNoche(BigDecimal precioPorNoche) {
		this.precioPorNoche = precioPorNoche;
	}

	public EstadoHabitacion getEstado() {
		return estado;
	}

	public void setEstado(EstadoHabitacion estado) {
		this.estado = estado;
	}
}



