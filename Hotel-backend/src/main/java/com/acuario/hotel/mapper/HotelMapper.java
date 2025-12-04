package com.acuario.hotel.mapper;

import com.acuario.hotel.dto.HotelRequestDTO;
import com.acuario.hotel.dto.HotelResponseDTO;
import com.acuario.hotel.model.Hotel;

public class HotelMapper {

	public static Hotel toEntity(HotelRequestDTO dto) {
		Hotel hotel = new Hotel();
		hotel.setNombre(dto.getNombre());
		hotel.setRuc(dto.getRuc());
		hotel.setDireccion(dto.getDireccion());
		hotel.setCelular(dto.getCelular());
		hotel.setEmail(dto.getEmail());
		return hotel;
	}

	public static HotelResponseDTO toDTO(Hotel hotel) {
		HotelResponseDTO dto = new HotelResponseDTO();
		dto.setId(hotel.getId());
		dto.setNombre(hotel.getNombre());
		dto.setRuc(hotel.getRuc());
		dto.setDireccion(hotel.getDireccion());
		dto.setCelular(hotel.getCelular());
		dto.setEmail(hotel.getEmail());
		dto.setFechaRegistro(hotel.getFechaRegistro());
		return dto;
	}

	public static void updateEntityFromDTO(Hotel hotel, HotelRequestDTO dto) {
		hotel.setNombre(dto.getNombre());
		hotel.setRuc(dto.getRuc());
		hotel.setDireccion(dto.getDireccion());
		hotel.setCelular(dto.getCelular());
		hotel.setEmail(dto.getEmail());
	}
}
