package com.acuario.hotel.mapper;

import com.acuario.hotel.dto.ServicioRequestDTO;
import com.acuario.hotel.dto.ServicioResponseDTO;
import com.acuario.hotel.model.Servicio;

public class ServicioMapper {

	public static Servicio toEntity(ServicioRequestDTO dto) {
		Servicio servicio = new Servicio();
		servicio.setNombreServicio(dto.getNombreServicio());
		servicio.setDescripcion(dto.getDescripcion());
		servicio.setPrecio(dto.getPrecio());
		return servicio;
	}

	public static ServicioResponseDTO toDTO(Servicio servicio) {
		ServicioResponseDTO dto = new ServicioResponseDTO();
		dto.setId(servicio.getId());
		dto.setNombreServicio(servicio.getNombreServicio());
		dto.setDescripcion(servicio.getDescripcion());
		dto.setPrecio(servicio.getPrecio());
		return dto;
	}

	public static void updateEntityFromDTO(Servicio servicio, ServicioRequestDTO dto) {
		servicio.setNombreServicio(dto.getNombreServicio());
		servicio.setDescripcion(dto.getDescripcion());
		servicio.setPrecio(dto.getPrecio());
	}
}



