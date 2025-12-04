package com.acuario.hotel.service;

import com.acuario.hotel.dto.ReservaRequestDTO;
import com.acuario.hotel.dto.ReservaResponseDTO;
import com.acuario.hotel.model.EstadoReserva;

import java.time.LocalDate;
import java.util.List;

public interface ReservaService {
	List<ReservaResponseDTO> listar();

	ReservaResponseDTO crear(ReservaRequestDTO dto);

	ReservaResponseDTO obtener(Long id);

	ReservaResponseDTO actualizar(Long id, ReservaRequestDTO dto);

	ReservaResponseDTO confirmar(Long id);

	void cancelar(Long id);
	
	void finalizar(Long id);
	
	void eliminar(Long id);

	List<ReservaResponseDTO> buscar(Long clienteId, LocalDate fechaIngreso, LocalDate fechaSalida, EstadoReserva estado);
}
