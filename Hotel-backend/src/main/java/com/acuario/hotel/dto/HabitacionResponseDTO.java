package com.acuario.hotel.dto;

import com.acuario.hotel.model.EstadoHabitacion;

import java.math.BigDecimal;

public class HabitacionResponseDTO {

	private Long id;
	private String numero;
	private String tipo;
	private int capacidad;
	private BigDecimal precioPorNoche;
	private EstadoHabitacion estado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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



