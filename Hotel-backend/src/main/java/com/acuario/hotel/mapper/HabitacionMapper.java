package com.acuario.hotel.mapper;

import com.acuario.hotel.dto.HabitacionRequestDTO;
import com.acuario.hotel.dto.HabitacionResponseDTO;
import com.acuario.hotel.model.Habitacion;

public class HabitacionMapper {

	public static Habitacion toEntity(HabitacionRequestDTO dto) {
		Habitacion habitacion = new Habitacion();
		habitacion.setNumero(dto.getNumero());
		habitacion.setTipo(dto.getTipo());
		habitacion.setCapacidad(dto.getCapacidad());
		habitacion.setPrecioPorNoche(dto.getPrecioPorNoche());
		habitacion.setEstado(dto.getEstado());
		return habitacion;
	}

	public static HabitacionResponseDTO toDTO(Habitacion habitacion) {
		HabitacionResponseDTO dto = new HabitacionResponseDTO();
		dto.setId(habitacion.getId());
		dto.setNumero(habitacion.getNumero());
		dto.setTipo(habitacion.getTipo());
		dto.setCapacidad(habitacion.getCapacidad());
		dto.setPrecioPorNoche(habitacion.getPrecioPorNoche());
		dto.setEstado(habitacion.getEstado());
		return dto;
	}

	public static void updateEntityFromDTO(Habitacion habitacion, HabitacionRequestDTO dto) {
		habitacion.setNumero(dto.getNumero());
		habitacion.setTipo(dto.getTipo());
		habitacion.setCapacidad(dto.getCapacidad());
		habitacion.setPrecioPorNoche(dto.getPrecioPorNoche());
		habitacion.setEstado(dto.getEstado());
	}
}



