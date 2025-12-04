package com.acuario.hotel.controller;

import com.acuario.hotel.dto.HotelRequestDTO;
import com.acuario.hotel.dto.HotelResponseDTO;
import com.acuario.hotel.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "hotel-controller", description = "Gestión de información del hotel")
@RestController
@RequestMapping("/api/hotel")
public class HotelController {

	private final HotelService service;

	public HotelController(HotelService service) {
		this.service = service;
	}

	@Operation(
		summary = "Obtener información del hotel",
		description = "Retorna la información general del hotel incluyendo nombre, RUC, dirección, celular y email. Requiere autenticación."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Información del hotel obtenida correctamente",
		content = @Content(schema = @Schema(implementation = HotelResponseDTO.class))
	)
	@ApiResponse(responseCode = "404", description = "No existe información del hotel registrada", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@GetMapping
	public HotelResponseDTO obtener() {
		return service.obtener();
	}

	@Operation(
		summary = "Actualizar información del hotel",
		description = "Actualiza la información general del hotel (nombre, RUC, dirección, celular, email). Si no existe información previa, la crea. Requiere rol de ADMINISTRADOR."
	)
	@ApiResponse(
		responseCode = "200",
		description = "Información del hotel actualizada exitosamente",
		content = @Content(schema = @Schema(implementation = HotelResponseDTO.class))
	)
	@ApiResponse(responseCode = "400", description = "Datos inválidos (RUC debe tener 11 dígitos, celular 9 dígitos, email formato válido)", content = @Content)
	@ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
	@ApiResponse(responseCode = "403", description = "No tiene permisos (requiere ADMINISTRADOR)", content = @Content)
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@PutMapping
	public ResponseEntity<HotelResponseDTO> actualizar(@Valid @RequestBody HotelRequestDTO dto) {
		HotelResponseDTO actualizado = service.actualizar(dto);
		return ResponseEntity.ok(actualizado);
	}
}
