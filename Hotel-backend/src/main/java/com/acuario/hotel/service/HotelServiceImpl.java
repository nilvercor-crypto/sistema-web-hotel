package com.acuario.hotel.service;

import com.acuario.hotel.dto.HotelRequestDTO;
import com.acuario.hotel.dto.HotelResponseDTO;
import com.acuario.hotel.exception.BusinessException;
import com.acuario.hotel.exception.ErrorCode;
import com.acuario.hotel.exception.ResourceNotFoundException;
import com.acuario.hotel.mapper.HotelMapper;
import com.acuario.hotel.model.Hotel;
import com.acuario.hotel.repository.HotelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HotelServiceImpl implements HotelService {

	private final HotelRepository repository;

	public HotelServiceImpl(HotelRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional(readOnly = true)
	public HotelResponseDTO obtener() {
		Hotel hotel = repository.findFirstByOrderByIdAsc()
				.orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", "no encontrado"));
		return HotelMapper.toDTO(hotel);
	}

	@Override
	public HotelResponseDTO actualizar(HotelRequestDTO dto) {
		Hotel hotel = repository.findFirstByOrderByIdAsc()
				.orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", "no encontrado"));

		if (dto.getRuc() != null && !dto.getRuc().trim().isEmpty()) {
			if (repository.existsByRucAndIdNot(dto.getRuc(), hotel.getId())) {
				throw new BusinessException("Ya existe un hotel con ese RUC", ErrorCode.BUSINESS_RULE_VIOLATION.getCode());
			}
		}

		HotelMapper.updateEntityFromDTO(hotel, dto);
		Hotel actualizado = repository.save(hotel);
		return HotelMapper.toDTO(actualizado);
	}
}
