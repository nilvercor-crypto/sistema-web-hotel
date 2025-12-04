package com.acuario.hotel.controller;

import com.acuario.hotel.dto.ServicioRequestDTO;
import com.acuario.hotel.dto.ServicioResponseDTO;
import com.acuario.hotel.service.ServicioService;
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
import java.util.List;

@Tag(name = "servicio-controller", description = "Gestión de servicios adicionales del hotel")
@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

	private final ServicioService service;

	public ServicioController(ServicioService service) {
		this.service = service;
	}

	@Operation(
		summary = "Listar servicios",
		description = "Retorna todos los servicios adicionales registrados en el sistema (SPA, restaurante, lavandería, etc.). Requiere autenticación."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Lista de servicios obtenida correctamente",
		content = @Content(schema = @Schema(implementation = ServicioResponseDTO.class))
	)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@GetMapping
	public List<ServicioResponseDTO> listar() {
		return service.listar();
	}

	@Operation(
		summary = "Obtener servicio por ID",
		description = "Retorna los detalles de un servicio específico por su identificador único."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Servicio encontrado",
		content = @Content(schema = @Schema(implementation = ServicioResponseDTO.class))
	)
	@ApiResponse(responseCode = "404", description = "Servicio no encontrado", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@GetMapping("/{id}")
	public ServicioResponseDTO obtener(
			@Parameter(description = "ID del servicio", required = true, example = "1")
			@PathVariable Long id) {
		return service.obtener(id);
	}

	@Operation(
		summary = "Crear servicio",
		description = "Crea un nuevo servicio adicional del hotel (SPA, restaurante, lavandería, room service, etc.). Valida que el nombre no esté duplicado. Requiere rol de ADMINISTRADOR."
	)
	@ApiResponse(
		responseCode = "201",
		description = "Servicio creado exitosamente",
		content = @Content(schema = @Schema(implementation = ServicioResponseDTO.class))
	)
	@ApiResponse(responseCode = "400", description = "Datos inválidos o nombre de servicio duplicado", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@ApiResponse(responseCode = "403", description = "No tiene permisos (requiere ADMINISTRADOR)", content = @Content)
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@PostMapping
	public ResponseEntity<ServicioResponseDTO> crear(@Valid @RequestBody ServicioRequestDTO dto) {
		ServicioResponseDTO creado = service.crear(dto);
		return ResponseEntity.created(URI.create("/api/servicios/" + creado.getId())).body(creado);
	}

	@Operation(
		summary = "Actualizar servicio",
		description = "Actualiza la información de un servicio existente. Valida que el nuevo nombre no esté duplicado. Requiere rol de ADMINISTRADOR."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Servicio actualizado exitosamente",
		content = @Content(schema = @Schema(implementation = ServicioResponseDTO.class))
	)
	@ApiResponse(responseCode = "400", description = "Datos inválidos o nombre duplicado", content = @Content)
	@ApiResponse(responseCode = "404", description = "Servicio no encontrado", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@ApiResponse(responseCode = "403", description = "No tiene permisos (requiere ADMINISTRADOR)", content = @Content)
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@PutMapping("/{id}")
	public ServicioResponseDTO actualizar(
			@Parameter(description = "ID del servicio a actualizar", required = true, example = "1")
			@PathVariable Long id,
			@Valid @RequestBody ServicioRequestDTO dto) {
		return service.actualizar(id, dto);
	}

	@Operation(
		summary = "Eliminar servicio",
		description = "Elimina permanentemente un servicio del sistema. Esta acción no se puede deshacer. Requiere rol de ADMINISTRADOR."
	)
	@ApiResponse(responseCode = "204", description = "Servicio eliminado exitosamente", content = @Content)
	@ApiResponse(responseCode = "404", description = "Servicio no encontrado", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@ApiResponse(responseCode = "403", description = "No tiene permisos (requiere ADMINISTRADOR)", content = @Content)
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(
			@Parameter(description = "ID del servicio a eliminar", required = true, example = "1")
			@PathVariable Long id) {
		service.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}


