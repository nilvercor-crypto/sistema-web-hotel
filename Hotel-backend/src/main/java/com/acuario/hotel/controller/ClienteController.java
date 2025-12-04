package com.acuario.hotel.controller;

import com.acuario.hotel.dto.ClienteRequestDTO;
import com.acuario.hotel.dto.ClienteResponseDTO;
import com.acuario.hotel.service.ClienteService;
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

@Tag(name = "cliente-controller", description = "Gestión de clientes")
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

	private final ClienteService service;

	public ClienteController(ClienteService service) {
		this.service = service;
	}

	@Operation(
		summary = "Listar clientes",
		description = "Retorna todos los clientes registrados en el sistema. Permite buscar opcionalmente por nombre, apellido, email o documento. Requiere autenticación."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Lista de clientes obtenida correctamente",
		content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))
	)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@GetMapping
	public List<ClienteResponseDTO> listar(
			@Parameter(description = "Texto de búsqueda (nombre, apellido, email o documento)", required = false, example = "Juan")
			@RequestParam(required = false) String buscar) {
		if (buscar != null && !buscar.trim().isEmpty()) {
			return service.buscar(buscar);
		}
		return service.listar();
	}

	@Operation(
		summary = "Obtener cliente por ID",
		description = "Retorna los detalles de un cliente específico por su identificador único."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Cliente encontrado",
		content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))
	)
	@ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@GetMapping("/{id}")
	public ClienteResponseDTO obtener(
			@Parameter(description = "ID del cliente", required = true, example = "1")
			@PathVariable Long id) {
		return service.obtener(id);
	}

	@Operation(
		summary = "Crear cliente",
		description = "Crea un nuevo cliente en el sistema. Requiere rol de ADMINISTRADOR. Valida que el documento de identidad no esté duplicado."
	)
	@ApiResponse(
		responseCode = "201",
		description = "Cliente creado exitosamente",
		content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))
	)
	@ApiResponse(responseCode = "400", description = "Datos inválidos o documento duplicado", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@ApiResponse(responseCode = "403", description = "No tiene permisos (requiere ADMINISTRADOR)", content = @Content)
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@PostMapping
	public ResponseEntity<ClienteResponseDTO> crear(@Valid @RequestBody ClienteRequestDTO dto) {
		ClienteResponseDTO creado = service.crear(dto);
		return ResponseEntity.created(URI.create("/api/clientes/" + creado.getId())).body(creado);
	}

	@Operation(
		summary = "Actualizar cliente",
		description = "Actualiza la información de un cliente existente. Requiere rol de ADMINISTRADOR."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Cliente actualizado exitosamente",
		content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))
	)
	@ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
	@ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@ApiResponse(responseCode = "403", description = "No tiene permisos (requiere ADMINISTRADOR)", content = @Content)
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@PutMapping("/{id}")
	public ClienteResponseDTO actualizar(
			@Parameter(description = "ID del cliente a actualizar", required = true, example = "1")
			@PathVariable Long id,
			@Valid @RequestBody ClienteRequestDTO dto) {
		return service.actualizar(id, dto);
	}

	@Operation(
		summary = "Eliminar cliente",
		description = "Elimina permanentemente un cliente del sistema. Requiere rol de ADMINISTRADOR. Esta acción no se puede deshacer."
	)
	@ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente", content = @Content)
	@ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@ApiResponse(responseCode = "403", description = "No tiene permisos (requiere ADMINISTRADOR)", content = @Content)
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(
			@Parameter(description = "ID del cliente a eliminar", required = true, example = "1")
			@PathVariable Long id) {
		service.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
