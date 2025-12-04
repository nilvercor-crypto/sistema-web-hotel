package com.acuario.hotel.controller;

import com.acuario.hotel.dto.HabitacionRequestDTO;
import com.acuario.hotel.dto.HabitacionResponseDTO;
import com.acuario.hotel.model.EstadoHabitacion;
import com.acuario.hotel.service.HabitacionService;
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

@Tag(name = "habitacion-controller", description = "Gestión de habitaciones")
@RestController
@RequestMapping("/api/habitaciones")
public class HabitacionController {

	private final HabitacionService service;

	public HabitacionController(HabitacionService service) {
		this.service = service;
	}

	@Operation(
		summary = "Listar habitaciones",
		description = "Retorna todas las habitaciones del sistema. Permite filtrar opcionalmente por estado (DISPONIBLE, OCUPADA, MANTENIMIENTO) o tipo (Simple, Doble, Suite, etc.). Si no se envían filtros, retorna todas las habitaciones. Requiere autenticación."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Lista de habitaciones obtenida correctamente",
		content = @Content(schema = @Schema(implementation = HabitacionResponseDTO.class))
	)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@GetMapping
	public List<HabitacionResponseDTO> listar(
			@Parameter(description = "Filtrar por estado: DISPONIBLE, OCUPADA o MANTENIMIENTO", required = false, example = "DISPONIBLE")
			@RequestParam(required = false) EstadoHabitacion estado,
			@Parameter(description = "Filtrar por tipo de habitación (Simple, Doble, Suite, etc.)", required = false, example = "Simple")
			@RequestParam(required = false) String tipo) {
		if (estado != null)
			return service.listarPorEstado(estado);
		if (tipo != null)
			return service.listarPorTipo(tipo);
		return service.listar();
	}

	@Operation(
		summary = "Obtener habitación por ID",
		description = "Retorna los detalles de una habitación específica por su identificador único."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Habitación encontrada",
		content = @Content(schema = @Schema(implementation = HabitacionResponseDTO.class))
	)
	@ApiResponse(responseCode = "404", description = "Habitación no encontrada", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@GetMapping("/{id}")
	public HabitacionResponseDTO obtener(
			@Parameter(description = "ID de la habitación", required = true, example = "1")
			@PathVariable Long id) {
		return service.obtener(id);
	}

	@Operation(
		summary = "Crear habitación",
		description = "Crea una nueva habitación en el sistema. Valida que el número de habitación no esté duplicado. Requiere rol de ADMINISTRADOR."
	)
	@ApiResponse(
		responseCode = "201",
		description = "Habitación creada exitosamente",
		content = @Content(schema = @Schema(implementation = HabitacionResponseDTO.class))
	)
	@ApiResponse(responseCode = "400", description = "Datos inválidos o número de habitación duplicado", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@ApiResponse(responseCode = "403", description = "No tiene permisos (requiere ADMINISTRADOR)", content = @Content)
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@PostMapping
	public ResponseEntity<HabitacionResponseDTO> crear(@Valid @RequestBody HabitacionRequestDTO dto) {
		HabitacionResponseDTO creada = service.crear(dto);
		return ResponseEntity.created(URI.create("/api/habitaciones/" + creada.getId())).body(creada);
	}

	@Operation(
		summary = "Actualizar habitación",
		description = "Actualiza la información de una habitación existente. Requiere rol de ADMINISTRADOR."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Habitación actualizada exitosamente",
		content = @Content(schema = @Schema(implementation = HabitacionResponseDTO.class))
	)
	@ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
	@ApiResponse(responseCode = "404", description = "Habitación no encontrada", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@ApiResponse(responseCode = "403", description = "No tiene permisos (requiere ADMINISTRADOR)", content = @Content)
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@PutMapping("/{id}")
	public HabitacionResponseDTO actualizar(
			@Parameter(description = "ID de la habitación a actualizar", required = true, example = "1")
			@PathVariable Long id,
			@Valid @RequestBody HabitacionRequestDTO dto) {
		return service.actualizar(id, dto);
	}

	@Operation(
		summary = "Eliminar habitación",
		description = "Elimina permanentemente una habitación del sistema. Esta acción no se puede deshacer. Requiere rol de ADMINISTRADOR."
	)
	@ApiResponse(responseCode = "204", description = "Habitación eliminada exitosamente", content = @Content)
	@ApiResponse(responseCode = "404", description = "Habitación no encontrada", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@ApiResponse(responseCode = "403", description = "No tiene permisos (requiere ADMINISTRADOR)", content = @Content)
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(
			@Parameter(description = "ID de la habitación a eliminar", required = true, example = "1")
			@PathVariable Long id) {
		service.eliminar(id);
		return ResponseEntity.noContent().build();
	}

	@Operation(
		summary = "Buscar habitaciones disponibles",
		description = "Busca habitaciones disponibles para un rango de fechas específico. Solo retorna habitaciones en estado DISPONIBLE que no tengan reservas solapadas en el rango de fechas indicado. Opcionalmente puede filtrar por tipo de habitación. Requiere autenticación."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Lista de habitaciones disponibles obtenida correctamente",
		content = @Content(schema = @Schema(implementation = HabitacionResponseDTO.class))
	)
	@ApiResponse(responseCode = "400", description = "Fechas inválidas (fecha de salida debe ser posterior a fecha de ingreso)", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@GetMapping("/disponibles")
	public List<HabitacionResponseDTO> buscarDisponibles(
			@Parameter(description = "Fecha de ingreso en formato YYYY-MM-DD", required = true, example = "2025-12-20")
			@RequestParam LocalDate fechaIngreso,
			@Parameter(description = "Fecha de salida en formato YYYY-MM-DD (debe ser posterior a fecha de ingreso)", required = true, example = "2025-12-25")
			@RequestParam LocalDate fechaSalida,
			@Parameter(description = "Tipo de habitación opcional (Simple, Doble, Suite, etc.)", required = false, example = "Simple")
			@RequestParam(required = false) String tipo) {
		return service.buscarDisponibles(fechaIngreso, fechaSalida, tipo);
	}
}
