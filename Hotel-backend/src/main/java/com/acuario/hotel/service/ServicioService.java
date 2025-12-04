package com.acuario.hotel.service;

import com.acuario.hotel.dto.ServicioRequestDTO;
import com.acuario.hotel.dto.ServicioResponseDTO;
import java.util.List;

public interface ServicioService {
	
	List<ServicioResponseDTO> listar();
	
	ServicioResponseDTO crear(ServicioRequestDTO dto);
	
	ServicioResponseDTO obtener(Long id);
	
	ServicioResponseDTO actualizar(Long id, ServicioRequestDTO dto);
	
	void eliminar(Long id);
}


