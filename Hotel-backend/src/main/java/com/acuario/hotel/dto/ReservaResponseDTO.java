package com.acuario.hotel.dto;

import com.acuario.hotel.model.EstadoReserva;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaResponseDTO {

	private Long id;
	private Long clienteId;
	private String clienteNombre;
	private String clienteApellido;
	private Long habitacionId;
	private String habitacionNumero;
	private String habitacionTipo;
	private LocalDate fechaIngreso;
	private LocalDate fechaSalida;
	private LocalDateTime fechaReserva;
	private EstadoReserva estado;
	private BigDecimal montoHabitacion;
	private BigDecimal montoServicios;
	private BigDecimal montoTotal;
	private List<ReservaServicioDTO> servicios = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public String getClienteNombre() {
		return clienteNombre;
	}

	public void setClienteNombre(String clienteNombre) {
		this.clienteNombre = clienteNombre;
	}

	public String getClienteApellido() {
		return clienteApellido;
	}

	public void setClienteApellido(String clienteApellido) {
		this.clienteApellido = clienteApellido;
	}

	public Long getHabitacionId() {
		return habitacionId;
	}

	public void setHabitacionId(Long habitacionId) {
		this.habitacionId = habitacionId;
	}

	public String getHabitacionNumero() {
		return habitacionNumero;
	}

	public void setHabitacionNumero(String habitacionNumero) {
		this.habitacionNumero = habitacionNumero;
	}

	public String getHabitacionTipo() {
		return habitacionTipo;
	}

	public void setHabitacionTipo(String habitacionTipo) {
		this.habitacionTipo = habitacionTipo;
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

	public LocalDateTime getFechaReserva() {
		return fechaReserva;
	}

	public void setFechaReserva(LocalDateTime fechaReserva) {
		this.fechaReserva = fechaReserva;
	}

	public EstadoReserva getEstado() {
		return estado;
	}

	public void setEstado(EstadoReserva estado) {
		this.estado = estado;
	}

	public BigDecimal getMontoHabitacion() {
		return montoHabitacion;
	}

	public void setMontoHabitacion(BigDecimal montoHabitacion) {
		this.montoHabitacion = montoHabitacion;
	}

	public BigDecimal getMontoServicios() {
		return montoServicios;
	}

	public void setMontoServicios(BigDecimal montoServicios) {
		this.montoServicios = montoServicios;
	}

	public BigDecimal getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}

	public List<ReservaServicioDTO> getServicios() {
		return servicios;
	}

	public void setServicios(List<ReservaServicioDTO> servicios) {
		this.servicios = servicios;
	}
}




