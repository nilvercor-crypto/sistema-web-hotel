package com.acuario.hotel.service;

import com.acuario.hotel.dto.HabitacionRequestDTO;
import com.acuario.hotel.dto.HabitacionResponseDTO;
import com.acuario.hotel.model.EstadoHabitacion;

import java.time.LocalDate;
import java.util.List;

public interface HabitacionService {
	List<HabitacionResponseDTO> listar();

	HabitacionResponseDTO crear(HabitacionRequestDTO dto);

	HabitacionResponseDTO obtener(Long id);

	HabitacionResponseDTO actualizar(Long id, HabitacionRequestDTO dto);

	void eliminar(Long id);

	List<HabitacionResponseDTO> listarPorEstado(EstadoHabitacion estado);

	List<HabitacionResponseDTO> listarPorTipo(String tipo);

	List<HabitacionResponseDTO> buscarDisponibles(LocalDate fechaIngreso, LocalDate fechaSalida, String tipo);
}
