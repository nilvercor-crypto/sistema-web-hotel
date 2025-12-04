package com.acuario.hotel.exception;

public enum ErrorCode {
	RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "Recurso no encontrado"),
	VALIDATION_ERROR("VALIDATION_ERROR", "Error de validación"),
	BUSINESS_RULE_VIOLATION("BUSINESS_RULE_VIOLATION", "Violación de regla de negocio"),

	CLIENTE_NOT_FOUND("CLIENTE_NOT_FOUND", "Cliente no encontrado"),
	CLIENTE_DUPLICADO("CLIENTE_DUPLICADO", "Ya existe un cliente con este documento"),
	CLIENTE_CON_RESERVAS("CLIENTE_CON_RESERVAS", "No se puede eliminar un cliente con reservas activas"),

	HABITACION_NOT_FOUND("HABITACION_NOT_FOUND", "Habitación no encontrada"),
	HABITACION_NO_DISPONIBLE("HABITACION_NO_DISPONIBLE", "Habitación no disponible para las fechas seleccionadas"),
	HABITACION_OCUPADA("HABITACION_OCUPADA", "La habitación está ocupada"),
	HABITACION_EN_MANTENIMIENTO("HABITACION_EN_MANTENIMIENTO", "La habitación está en mantenimiento"),

	RESERVA_NOT_FOUND("RESERVA_NOT_FOUND", "Reserva no encontrada"),
	RESERVA_FECHAS_INVALIDAS("RESERVA_FECHAS_INVALIDAS", "Las fechas de la reserva son inválidas"),
	RESERVA_SOLAPADA("RESERVA_SOLAPADA", "Ya existe una reserva para esta habitación en las fechas seleccionadas"),
	RESERVA_ESTADO_INVALIDO("RESERVA_ESTADO_INVALIDO", "El estado de la reserva no permite esta operación"),
	RESERVA_CAPACIDAD_INSUFICIENTE("RESERVA_CAPACIDAD_INSUFICIENTE", "La habitación no tiene capacidad suficiente para el número de huéspedes"),

	SERVICIO_NOT_FOUND("SERVICIO_NOT_FOUND", "Servicio no encontrado"),
	SERVICIO_DUPLICADO("SERVICIO_DUPLICADO", "Ya existe un servicio con este nombre"),

	INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Error interno del servidor"),
	BAD_REQUEST("BAD_REQUEST", "Solicitud inválida");

	private final String code;
	private final String message;

	ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}

