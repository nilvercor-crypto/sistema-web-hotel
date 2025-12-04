package com.acuario.hotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "habitaciones", uniqueConstraints = @UniqueConstraint(name = "uk_habitacion_numero", columnNames = "numero"))
public class Habitacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 10)
	private String numero;

	@NotBlank
	@Size(max = 50)
	private String tipo;

	@Min(1)
	private int capacidad;

	@NotNull
	@DecimalMin(value = "0.00")
	@Digits(integer = 8, fraction = 2)
	private BigDecimal precioPorNoche;

	@NotNull
	@Enumerated(EnumType.STRING)
	private EstadoHabitacion estado = EstadoHabitacion.DISPONIBLE;

	public Long getId() {
		return id;
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
