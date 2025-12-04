package com.acuario.hotel.mapper;

import com.acuario.hotel.dto.ClienteRequestDTO;
import com.acuario.hotel.dto.ClienteResponseDTO;
import com.acuario.hotel.model.Cliente;

public class ClienteMapper {

	public static Cliente toEntity(ClienteRequestDTO dto) {
		Cliente cliente = new Cliente();
		cliente.setNombre(dto.getNombre());
		cliente.setApellido(dto.getApellido());
		cliente.setTipoDocumento(dto.getTipoDocumento());
		cliente.setDocumentoIdentidad(dto.getDocumentoIdentidad());
		cliente.setEmail(dto.getEmail());
		cliente.setCelular(dto.getCelular());
		cliente.setDireccion(dto.getDireccion());
		return cliente;
	}

	public static ClienteResponseDTO toDTO(Cliente cliente) {
		ClienteResponseDTO dto = new ClienteResponseDTO();
		dto.setId(cliente.getId());
		dto.setNombre(cliente.getNombre());
		dto.setApellido(cliente.getApellido());
		dto.setTipoDocumento(cliente.getTipoDocumento());
		dto.setDocumentoIdentidad(cliente.getDocumentoIdentidad());
		dto.setEmail(cliente.getEmail());
		dto.setCelular(cliente.getCelular());
		dto.setDireccion(cliente.getDireccion());
		dto.setFechaRegistro(cliente.getFechaRegistro());
		return dto;
	}

	public static void updateEntityFromDTO(Cliente cliente, ClienteRequestDTO dto) {
		cliente.setNombre(dto.getNombre());
		cliente.setApellido(dto.getApellido());
		cliente.setTipoDocumento(dto.getTipoDocumento());
		if (dto.getDocumentoIdentidad() != null) {
			cliente.setDocumentoIdentidad(dto.getDocumentoIdentidad());
		}
		cliente.setEmail(dto.getEmail());
		cliente.setCelular(dto.getCelular());
		cliente.setDireccion(dto.getDireccion());
	}
}



