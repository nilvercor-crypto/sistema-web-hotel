package com.acuario.hotel.service;

import com.acuario.hotel.dto.ClienteRequestDTO;
import com.acuario.hotel.dto.ClienteResponseDTO;
import java.util.List;

public interface ClienteService {
	List<ClienteResponseDTO> listar();

	ClienteResponseDTO crear(ClienteRequestDTO dto);

	ClienteResponseDTO obtener(Long id);

	ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto);

	void eliminar(Long id);

	List<ClienteResponseDTO> buscar(String busqueda);
}
