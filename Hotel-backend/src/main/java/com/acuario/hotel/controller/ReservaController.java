package com.acuario.hotel.controller;

import com.acuario.hotel.dto.ReservaRequestDTO;
import com.acuario.hotel.dto.ReservaResponseDTO;
import com.acuario.hotel.service.ReservaService;
import com.acuario.hotel.model.EstadoReserva;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "reserva-controller", description = "Gestión de reservas")
@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

	private final ReservaService service;

	public ReservaController(ReservaService service) {
		this.service = service;
	}

	@Operation(
		summary = "Listar reservas",
		description = "Retorna todas las reservas del sistema con sus servicios asociados, cliente y habitación. Permite filtrar opcionalmente por cliente, fechas y estado. Requiere autenticación."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Lista de reservas obtenida correctamente",
		content = @Content(schema = @Schema(implementation = ReservaResponseDTO.class))
	)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@GetMapping
	public List<ReservaResponseDTO> listar(
			@Parameter(description = "ID del cliente para filtrar", required = false, example = "1")
			@RequestParam(required = false) Long clienteId,
			@Parameter(description = "Fecha de ingreso mínima (formato: YYYY-MM-DD)", required = false, example = "2025-12-01")
			@RequestParam(required = false) LocalDate fechaIngreso,
			@Parameter(description = "Fecha de salida máxima (formato: YYYY-MM-DD)", required = false, example = "2025-12-31")
			@RequestParam(required = false) LocalDate fechaSalida,
			@Parameter(description = "Estado de la reserva (PENDIENTE, CONFIRMADA, CANCELADA, FINALIZADA)", required = false, example = "CONFIRMADA")
			@RequestParam(required = false) EstadoReserva estado) {
		if (clienteId != null || fechaIngreso != null || fechaSalida != null || estado != null) {
			return service.buscar(clienteId, fechaIngreso, fechaSalida, estado);
		}
		return service.listar();
	}

	@Operation(
		summary = "Obtener reserva por ID",
		description = "Retorna los detalles completos de una reserva específica, incluyendo información del cliente, habitación y servicios asociados."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Reserva encontrada",
		content = @Content(schema = @Schema(implementation = ReservaResponseDTO.class))
	)
	@ApiResponse(responseCode = "404", description = "Reserva no encontrada", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@GetMapping("/{id}")
	public ReservaResponseDTO obtener(
			@Parameter(description = "ID de la reserva", required = true, example = "1")
			@PathVariable Long id) {
		return service.obtener(id);
	}

	@Operation(
		summary = "Crear reserva",
		description = "Crea una nueva reserva. Puede incluir servicios adicionales. Valida disponibilidad de la habitación en el rango de fechas. Requiere rol ADMINISTRADOR o USUARIO."
	)
	@ApiResponse(
		responseCode = "201",
		description = "Reserva creada exitosamente",
		content = @Content(schema = @Schema(implementation = ReservaResponseDTO.class))
	)
	@ApiResponse(responseCode = "400", description = "Datos inválidos, habitación no disponible o fechas incorrectas", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content)
	@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
	@PostMapping
	public ResponseEntity<ReservaResponseDTO> crear(@Valid @RequestBody ReservaRequestDTO dto) {
		ReservaResponseDTO creada = service.crear(dto);
		return ResponseEntity.created(URI.create("/api/reservas/" + creada.getId())).body(creada);
	}

	@Operation(
		summary = "Actualizar reserva",
		description = "Actualiza una reserva existente. Solo se pueden actualizar reservas en estado PENDIENTE. Valida disponibilidad de la habitación. Requiere rol ADMINISTRADOR o USUARIO."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Reserva actualizada exitosamente",
		content = @Content(schema = @Schema(implementation = ReservaResponseDTO.class))
	)
	@ApiResponse(responseCode = "400", description = "Datos inválidos, habitación no disponible o reserva no se puede actualizar", content = @Content)
	@ApiResponse(responseCode = "404", description = "Reserva no encontrada", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content)
	@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
	@PutMapping("/{id}")
	public ReservaResponseDTO actualizar(
			@Parameter(description = "ID de la reserva a actualizar", required = true, example = "1")
			@PathVariable Long id,
			@Valid @RequestBody ReservaRequestDTO dto) {
		return service.actualizar(id, dto);
	}

	@Operation(
		summary = "Confirmar reserva",
		description = "Confirma una reserva en estado PENDIENTE. Si la fecha de ingreso es hoy o anterior, la habitación cambia automáticamente a estado OCUPADA. Requiere rol ADMINISTRADOR o USUARIO."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Reserva confirmada exitosamente",
		content = @Content(schema = @Schema(implementation = ReservaResponseDTO.class))
	)
	@ApiResponse(responseCode = "400", description = "La reserva no se puede confirmar (ya está confirmada o cancelada)", content = @Content)
	@ApiResponse(responseCode = "404", description = "Reserva no encontrada", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content)
	@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
	@PostMapping("/{id}/confirmar")
	public ReservaResponseDTO confirmar(
			@Parameter(description = "ID de la reserva a confirmar", required = true, example = "1")
			@PathVariable Long id) {
		return service.confirmar(id);
	}

	@Operation(
		summary = "Cancelar reserva",
		description = "Cancela una reserva. Si estaba confirmada y la habitación estaba OCUPADA, la libera automáticamente. Requiere rol ADMINISTRADOR o USUARIO."
	)
	@ApiResponse(responseCode = "204", description = "Reserva cancelada exitosamente", content = @Content)
	@ApiResponse(responseCode = "400", description = "La reserva no se puede cancelar (ya está finalizada)", content = @Content)
	@ApiResponse(responseCode = "404", description = "Reserva no encontrada", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content)
	@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
	@PostMapping("/{id}/cancelar")
	public ResponseEntity<Void> cancelar(
			@Parameter(description = "ID de la reserva a cancelar", required = true, example = "1")
			@PathVariable Long id) {
		service.cancelar(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(
		summary = "Finalizar reserva",
		description = "Marca una reserva confirmada como finalizada y libera la habitación automáticamente. Requiere rol ADMINISTRADOR o USUARIO."
	)
	@ApiResponse(responseCode = "204", description = "Reserva finalizada exitosamente", content = @Content)
	@ApiResponse(responseCode = "400", description = "La reserva no se puede finalizar (debe estar confirmada)", content = @Content)
	@ApiResponse(responseCode = "404", description = "Reserva no encontrada", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@ApiResponse(responseCode = "403", description = "No tiene permisos", content = @Content)
	@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
	@PostMapping("/{id}/finalizar")
	public ResponseEntity<Void> finalizar(
			@Parameter(description = "ID de la reserva a finalizar", required = true, example = "1")
			@PathVariable Long id) {
		service.finalizar(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(
		summary = "Eliminar reserva",
		description = "Elimina permanentemente una reserva del sistema. Esta acción no se puede deshacer. Requiere rol ADMINISTRADOR."
	)
	@ApiResponse(responseCode = "204", description = "Reserva eliminada exitosamente", content = @Content)
	@ApiResponse(responseCode = "404", description = "Reserva no encontrada", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@ApiResponse(responseCode = "403", description = "No tiene permisos (requiere ADMINISTRADOR)", content = @Content)
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(
			@Parameter(description = "ID de la reserva a eliminar", required = true, example = "1")
			@PathVariable Long id) {
		service.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
