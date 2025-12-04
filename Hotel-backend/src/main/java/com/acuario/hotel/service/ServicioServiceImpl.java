package com.acuario.hotel.service;

import com.acuario.hotel.dto.ServicioRequestDTO;
import com.acuario.hotel.dto.ServicioResponseDTO;
import com.acuario.hotel.exception.BusinessException;
import com.acuario.hotel.exception.ErrorCode;
import com.acuario.hotel.exception.ResourceNotFoundException;
import com.acuario.hotel.mapper.ServicioMapper;
import com.acuario.hotel.model.Servicio;
import com.acuario.hotel.repository.ServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServicioServiceImpl implements ServicioService {

	private final ServicioRepository repository;

	public ServicioServiceImpl(ServicioRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<ServicioResponseDTO> listar() {
		return repository.findAll().stream()
				.map(ServicioMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public ServicioResponseDTO crear(ServicioRequestDTO dto) {
		if (dto.getNombreServicio() != null && 
			repository.existsByNombreServicioIgnoreCase(dto.getNombreServicio())) {
			throw new BusinessException("Ya existe un servicio con ese nombre", ErrorCode.SERVICIO_DUPLICADO.getCode());
		}
		Servicio servicio = ServicioMapper.toEntity(dto);
		Servicio guardado = repository.save(servicio);
		return ServicioMapper.toDTO(guardado);
	}

	@Override
	public ServicioResponseDTO obtener(Long id) {
		Servicio servicio = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Servicio", "id", id));
		return ServicioMapper.toDTO(servicio);
	}

	@Override
	public ServicioResponseDTO actualizar(Long id, ServicioRequestDTO dto) {
		Servicio existente = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Servicio", "id", id));
		
		if (dto.getNombreServicio() != null && 
			!dto.getNombreServicio().equalsIgnoreCase(existente.getNombreServicio()) &&
			repository.existsByNombreServicioIgnoreCase(dto.getNombreServicio())) {
			throw new BusinessException("Ya existe un servicio con ese nombre", ErrorCode.SERVICIO_DUPLICADO.getCode());
		}
		
		ServicioMapper.updateEntityFromDTO(existente, dto);
		Servicio actualizado = repository.save(existente);
		return ServicioMapper.toDTO(actualizado);
	}

	@Override
	public void eliminar(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Servicio", "id", id);
		}
		repository.deleteById(id);
	}
}


