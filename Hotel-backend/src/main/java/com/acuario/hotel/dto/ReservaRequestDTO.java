package com.acuario.hotel.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaRequestDTO {

	@NotNull(message = "El cliente es obligatorio")
	private Long clienteId;

	@NotNull(message = "La habitación es obligatoria")
	private Long habitacionId;

	@NotNull(message = "La fecha de ingreso es obligatoria")
	private LocalDate fechaIngreso;

	@NotNull(message = "La fecha de salida es obligatoria")
	private LocalDate fechaSalida;

	@Valid
	private List<ReservaServicioDTO> servicios = new ArrayList<>();

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public Long getHabitacionId() {
		return habitacionId;
	}

	public void setHabitacionId(Long habitacionId) {
		this.habitacionId = habitacionId;
	}

	public LocalDate getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(LocalDate fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public LocalDate getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(LocalDate fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public List<ReservaServicioDTO> getServicios() {
		return servicios;
	}

	public void setServicios(List<ReservaServicioDTO> servicios) {
		this.servicios = servicios;
	}
}




