package com.acuario.hotel.service;

import com.acuario.hotel.dto.HabitacionRequestDTO;
import com.acuario.hotel.dto.HabitacionResponseDTO;
import com.acuario.hotel.exception.BusinessException;
import com.acuario.hotel.exception.ErrorCode;
import com.acuario.hotel.exception.ResourceNotFoundException;
import com.acuario.hotel.exception.ValidationException;
import com.acuario.hotel.mapper.HabitacionMapper;
import com.acuario.hotel.model.EstadoHabitacion;
import com.acuario.hotel.model.EstadoReserva;
import com.acuario.hotel.model.Habitacion;
import com.acuario.hotel.repository.HabitacionRepository;
import com.acuario.hotel.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HabitacionServiceImpl implements HabitacionService {

	private final HabitacionRepository repo;
	private final ReservaRepository reservaRepo;

	public HabitacionServiceImpl(HabitacionRepository repo, ReservaRepository reservaRepo) {
		this.repo = repo;
		this.reservaRepo = reservaRepo;
	}

	@Override
	public List<HabitacionResponseDTO> listar() {
		return repo.findAll().stream()
				.map(HabitacionMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public HabitacionResponseDTO crear(HabitacionRequestDTO dto) {
		Habitacion habitacion = HabitacionMapper.toEntity(dto);
		if (repo.existsByNumero(habitacion.getNumero()))
			throw new BusinessException("El número de habitación ya existe", ErrorCode.BUSINESS_RULE_VIOLATION.getCode());
		Habitacion guardada = repo.save(habitacion);
		return HabitacionMapper.toDTO(guardada);
	}

	@Override
	public HabitacionResponseDTO obtener(Long id) {
		Habitacion habitacion = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Habitación", "id", id));
		return HabitacionMapper.toDTO(habitacion);
	}

	@Override
	public HabitacionResponseDTO actualizar(Long id, HabitacionRequestDTO dto) {
		Habitacion db = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Habitación", "id", id));

		if (!db.getNumero().equals(dto.getNumero()) && repo.existsByNumero(dto.getNumero()))
			throw new BusinessException("El número de habitación ya existe", ErrorCode.BUSINESS_RULE_VIOLATION.getCode());

		HabitacionMapper.updateEntityFromDTO(db, dto);
		Habitacion actualizada = repo.save(db);
		return HabitacionMapper.toDTO(actualizada);
	}

	@Override
	public void eliminar(Long id) {
		if (!repo.existsById(id)) {
			throw new ResourceNotFoundException("Habitación", "id", id);
		}
		repo.deleteById(id);
	}

	@Override
	public List<HabitacionResponseDTO> listarPorEstado(EstadoHabitacion estado) {
		return repo.findByEstado(estado).stream()
				.map(HabitacionMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<HabitacionResponseDTO> listarPorTipo(String tipo) {
		return repo.findByTipoIgnoreCase(tipo).stream()
				.map(HabitacionMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<HabitacionResponseDTO> buscarDisponibles(LocalDate fechaIngreso, LocalDate fechaSalida, String tipo) {
		if (fechaIngreso == null || fechaSalida == null) {
			throw new ValidationException("Las fechas de ingreso y salida son obligatorias");
		}
		if (fechaIngreso.isBefore(LocalDate.now())) {
			throw new ValidationException("La fecha de ingreso no puede ser anterior al día de hoy");
		}
		if (fechaSalida.isBefore(fechaIngreso)) {
			throw new ValidationException("La fecha de salida debe ser posterior a la fecha de ingreso");
		}

		List<Habitacion> habitaciones = repo.findByEstado(EstadoHabitacion.DISPONIBLE);

		if (tipo != null && !tipo.trim().isEmpty()) {
			habitaciones = habitaciones.stream()
					.filter(h -> h.getTipo().equalsIgnoreCase(tipo.trim()))
					.toList();
		}

		List<EstadoReserva> estadosActivos = List.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA);
		List<Habitacion> disponibles = new ArrayList<>();

		for (Habitacion habitacion : habitaciones) {
			long reservasSolapadas = reservaRepo.contarSolapadas(habitacion, fechaIngreso, fechaSalida, estadosActivos);
			if (reservasSolapadas == 0) {
				disponibles.add(habitacion);
			}
		}

		return disponibles.stream()
				.map(HabitacionMapper::toDTO)
				.collect(Collectors.toList());
	}
}
