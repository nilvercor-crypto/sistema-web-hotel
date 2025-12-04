package com.acuario.hotel.service;

import com.acuario.hotel.dto.ClienteRequestDTO;
import com.acuario.hotel.dto.ClienteResponseDTO;
import com.acuario.hotel.exception.BusinessException;
import com.acuario.hotel.exception.ErrorCode;
import com.acuario.hotel.exception.ResourceNotFoundException;
import com.acuario.hotel.exception.ValidationException;
import com.acuario.hotel.mapper.ClienteMapper;
import com.acuario.hotel.model.Cliente;
import com.acuario.hotel.model.TipoDocumento;
import com.acuario.hotel.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

	private final ClienteRepository repo;

	public ClienteServiceImpl(ClienteRepository repo) {
		this.repo = repo;
	}

	@Override
	public List<ClienteResponseDTO> listar() {
		return repo.findAll().stream()
				.map(ClienteMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public ClienteResponseDTO crear(ClienteRequestDTO dto) {
		Cliente cliente = ClienteMapper.toEntity(dto);
		validarDocumentoPorTipo(cliente);
		if (cliente.getDocumentoIdentidad() != null && repo.existsByDocumentoIdentidad(cliente.getDocumentoIdentidad())) {
			throw new BusinessException("Ya existe un cliente con este documento", ErrorCode.CLIENTE_DUPLICADO.getCode());
		}
		Cliente guardado = repo.save(cliente);
		return ClienteMapper.toDTO(guardado);
	}

	@Override
	public ClienteResponseDTO obtener(Long id) {
		Cliente cliente = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", id));
		return ClienteMapper.toDTO(cliente);
	}

	@Override
	public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {
		Cliente db = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", id));
		
		ClienteMapper.updateEntityFromDTO(db, dto);
		validarDocumentoPorTipo(db);
		
		Cliente actualizado = repo.save(db);
		return ClienteMapper.toDTO(actualizado);
	}

	@Override
	public void eliminar(Long id) {
		if (!repo.existsById(id)) {
			throw new ResourceNotFoundException("Cliente", "id", id);
		}
		repo.deleteById(id);
	}

	@Override
	public List<ClienteResponseDTO> buscar(String busqueda) {
		if (busqueda == null || busqueda.trim().isEmpty()) {
			return listar();
		}
		return repo.buscarPorNombreOEmailODocumento(busqueda.trim()).stream()
				.map(ClienteMapper::toDTO)
				.collect(Collectors.toList());
	}

	private void validarDocumentoPorTipo(Cliente c) {
		if (c.getTipoDocumento() == null || c.getDocumentoIdentidad() == null)
			return;

		String doc = c.getDocumentoIdentidad().trim();
		TipoDocumento tipo = c.getTipoDocumento();

		switch (tipo) {
		case DNI -> {
			if (!doc.matches("\\d{8}"))
				throw new ValidationException("El DNI debe tener exactamente 8 dígitos numéricos");
		}
		case CARNET_EXTRANJERIA -> {
			if (!doc.matches("[A-Za-z0-9]{9,12}"))
				throw new ValidationException(
						"El Carnet de Extranjería debe tener entre 9 y 12 caracteres alfanuméricos");
		}
		case PASAPORTE -> {
			if (!doc.matches("[A-Za-z0-9]{6,12}"))
				throw new ValidationException(
						"El Pasaporte debe tener entre 6 y 12 caracteres alfanuméricos");
		}
		case OTRO -> {
			if (doc.length() < 4 || doc.length() > 15)
				throw new ValidationException("El documento debe tener entre 4 y 15 caracteres");
		}
		default -> {
		}
		}
	}
}
