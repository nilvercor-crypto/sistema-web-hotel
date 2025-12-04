package com.acuario.hotel.service;

import com.acuario.hotel.dto.HotelRequestDTO;
import com.acuario.hotel.dto.HotelResponseDTO;

public interface HotelService {
	HotelResponseDTO obtener();
	HotelResponseDTO actualizar(HotelRequestDTO dto);
}

