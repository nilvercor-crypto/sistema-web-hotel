package com.acuario.hotel.service;

import com.acuario.hotel.dto.ReservaRequestDTO;
import com.acuario.hotel.dto.ReservaResponseDTO;
import com.acuario.hotel.dto.ReservaServicioDTO;
import com.acuario.hotel.exception.BusinessException;
import com.acuario.hotel.exception.ErrorCode;
import com.acuario.hotel.exception.ResourceNotFoundException;
import com.acuario.hotel.exception.ValidationException;
import com.acuario.hotel.model.*;
import com.acuario.hotel.repository.ClienteRepository;
import com.acuario.hotel.repository.HabitacionRepository;
import com.acuario.hotel.repository.ReservaRepository;
import com.acuario.hotel.repository.ReservaServicioRepository;
import com.acuario.hotel.repository.ServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservaServiceImpl implements ReservaService {

	private final ReservaRepository reservaRepo;
	private final ClienteRepository clienteRepo;
	private final HabitacionRepository habitacionRepo;
	private final ServicioRepository servicioRepo;
	private final ReservaServicioRepository reservaServicioRepo;

	public ReservaServiceImpl(ReservaRepository reservaRepo, ClienteRepository clienteRepo,
			HabitacionRepository habitacionRepo, ServicioRepository servicioRepo,
			ReservaServicioRepository reservaServicioRepo) {
		this.reservaRepo = reservaRepo;
		this.clienteRepo = clienteRepo;
		this.habitacionRepo = habitacionRepo;
		this.servicioRepo = servicioRepo;
		this.reservaServicioRepo = reservaServicioRepo;
	}

	@Override
	public List<ReservaResponseDTO> listar() {
		return reservaRepo.findAll().stream()
				.map(this::convertirADTO)
				.collect(Collectors.toList());
	}

	@Override
	public ReservaResponseDTO crear(ReservaRequestDTO dto) {
		if (dto.getFechaIngreso() == null || dto.getFechaSalida() == null)
			throw new ValidationException("Debe indicar fechas de ingreso y salida");
		if (dto.getFechaIngreso().isAfter(dto.getFechaSalida()))
			throw new ValidationException("La fecha de ingreso no puede ser posterior a la fecha de salida");
		
		LocalDate hoy = LocalDate.now();
		if (dto.getFechaIngreso().isBefore(hoy))
			throw new ValidationException("No se pueden hacer reservas con fecha de ingreso anterior al día de hoy");

		Habitacion hab = habitacionRepo.findById(dto.getHabitacionId())
				.orElseThrow(() -> new ResourceNotFoundException("Habitación", "id", dto.getHabitacionId()));
		Cliente cli = clienteRepo.findById(dto.getClienteId())
				.orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", dto.getClienteId()));

		long solapes = reservaRepo.contarSolapadas(hab, dto.getFechaIngreso(), dto.getFechaSalida(),
				List.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA));
		if (solapes > 0)
			throw new BusinessException("La habitación no está disponible en ese rango", ErrorCode.HABITACION_NO_DISPONIBLE.getCode());

		long noches = ChronoUnit.DAYS.between(dto.getFechaIngreso(), dto.getFechaSalida());
		if (noches < 0)
			throw new ValidationException("Rango de fechas inválido");
		
		if (noches == 0)
			noches = 1;

		Reserva reserva = new Reserva();
		reserva.setHabitacion(hab);
		reserva.setCliente(cli);
		reserva.setFechaIngreso(dto.getFechaIngreso());
		reserva.setFechaSalida(dto.getFechaSalida());
		reserva.setEstado(EstadoReserva.PENDIENTE);

		BigDecimal montoHabitacion = hab.getPrecioPorNoche().multiply(BigDecimal.valueOf(noches));

		BigDecimal montoServicios = BigDecimal.ZERO;
		if (dto.getServicios() != null && !dto.getServicios().isEmpty()) {
			for (ReservaServicioDTO servicioDTO : dto.getServicios()) {
				Servicio servicio = servicioRepo.findById(servicioDTO.getServicioId())
						.orElseThrow(() -> new ResourceNotFoundException("Servicio", "id", servicioDTO.getServicioId()));

				ReservaServicio reservaServicio = new ReservaServicio();
				reservaServicio.setReserva(reserva);
				reservaServicio.setServicio(servicio);
				reservaServicio.setCantidad(servicioDTO.getCantidad());

				BigDecimal subtotal = servicio.getPrecio()
						.multiply(BigDecimal.valueOf(servicioDTO.getCantidad()));
				montoServicios = montoServicios.add(subtotal);

				reserva.getServicios().add(reservaServicio);
			}
		}

		BigDecimal montoTotal = montoHabitacion.add(montoServicios);
		reserva.setMontoTotal(montoTotal);

		Reserva guardada = reservaRepo.save(reserva);
		return convertirADTO(guardada);
	}

	@Override
	public ReservaResponseDTO obtener(Long id) {
		Reserva reserva = reservaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", id));
		return convertirADTO(reserva);
	}

	@Override
	public ReservaResponseDTO actualizar(Long id, ReservaRequestDTO dto) {
		Reserva reserva = reservaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", id));

		Habitacion habitacionAnterior = reserva.getHabitacion();
		EstadoReserva estadoAnterior = reserva.getEstado();

		if (dto.getFechaIngreso() == null || dto.getFechaSalida() == null)
			throw new ValidationException("Debe indicar fechas de ingreso y salida");
		if (dto.getFechaIngreso().isAfter(dto.getFechaSalida()))
			throw new ValidationException("La fecha de ingreso no puede ser posterior a la fecha de salida");

		LocalDate hoy = LocalDate.now();
		if (dto.getFechaIngreso().isBefore(hoy))
			throw new ValidationException("No se pueden hacer reservas con fecha de ingreso anterior al día de hoy");

		Habitacion nuevaHabitacion = habitacionRepo.findById(dto.getHabitacionId())
				.orElseThrow(() -> new ResourceNotFoundException("Habitación", "id", dto.getHabitacionId()));

		long solapes = reservaRepo.contarSolapadasExcluyendo(nuevaHabitacion, dto.getFechaIngreso(), dto.getFechaSalida(),
				List.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA), reserva.getId());
		if (solapes > 0)
			throw new BusinessException("La habitación no está disponible en ese rango", ErrorCode.HABITACION_NO_DISPONIBLE.getCode());

		Cliente cliente = clienteRepo.findById(dto.getClienteId())
				.orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", dto.getClienteId()));

		reserva.setCliente(cliente);
		reserva.setHabitacion(nuevaHabitacion);
		reserva.setFechaIngreso(dto.getFechaIngreso());
		reserva.setFechaSalida(dto.getFechaSalida());

		reserva.getServicios().clear();
		BigDecimal montoServicios = BigDecimal.ZERO;
		if (dto.getServicios() != null && !dto.getServicios().isEmpty()) {
			for (ReservaServicioDTO servicioDTO : dto.getServicios()) {
				Servicio servicio = servicioRepo.findById(servicioDTO.getServicioId())
						.orElseThrow(() -> new ResourceNotFoundException("Servicio", "id", servicioDTO.getServicioId()));

				ReservaServicio reservaServicio = new ReservaServicio();
				reservaServicio.setReserva(reserva);
				reservaServicio.setServicio(servicio);
				reservaServicio.setCantidad(servicioDTO.getCantidad());

				BigDecimal subtotal = servicio.getPrecio().multiply(BigDecimal.valueOf(servicioDTO.getCantidad()));
				montoServicios = montoServicios.add(subtotal);

				reserva.getServicios().add(reservaServicio);
			}
		}

		long noches = ChronoUnit.DAYS.between(dto.getFechaIngreso(), dto.getFechaSalida());
		if (noches == 0) noches = 1;
		BigDecimal montoHabitacion = nuevaHabitacion.getPrecioPorNoche().multiply(BigDecimal.valueOf(noches));
		BigDecimal montoTotal = montoHabitacion.add(montoServicios);
		reserva.setMontoTotal(montoTotal);

		if (!habitacionAnterior.getId().equals(nuevaHabitacion.getId()) && estadoAnterior == EstadoReserva.CONFIRMADA) {
			if (habitacionAnterior.getEstado() == EstadoHabitacion.OCUPADA) {
				LocalDate hoy2 = LocalDate.now();
				long reservasActivas = reservaRepo.contarSolapadas(habitacionAnterior, hoy2, hoy2,
						List.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA));
				if (reservasActivas == 0) {
					habitacionAnterior.setEstado(EstadoHabitacion.DISPONIBLE);
					habitacionRepo.save(habitacionAnterior);
				}
			}
			if (dto.getFechaIngreso().isBefore(hoy) || dto.getFechaIngreso().isEqual(hoy)) {
				if (nuevaHabitacion.getEstado() != EstadoHabitacion.MANTENIMIENTO) {
					nuevaHabitacion.setEstado(EstadoHabitacion.OCUPADA);
					habitacionRepo.save(nuevaHabitacion);
				}
			}
		}

		Reserva guardada = reservaRepo.save(reserva);
		return convertirADTO(guardada);
	}

	@Override
	public ReservaResponseDTO confirmar(Long id) {
		Reserva reserva = reservaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", id));
		if (reserva.getEstado() == EstadoReserva.CANCELADA || reserva.getEstado() == EstadoReserva.FINALIZADA)
			throw new BusinessException("No se puede confirmar una reserva cancelada/finalizada", ErrorCode.RESERVA_ESTADO_INVALIDO.getCode());
		
		reserva.setEstado(EstadoReserva.CONFIRMADA);
		
		Habitacion habitacion = reserva.getHabitacion();
		LocalDate hoy = LocalDate.now();
		if (reserva.getFechaIngreso().isBefore(hoy) || reserva.getFechaIngreso().isEqual(hoy)) {
			if (habitacion.getEstado() != EstadoHabitacion.MANTENIMIENTO) {
				habitacion.setEstado(EstadoHabitacion.OCUPADA);
				habitacionRepo.save(habitacion);
			}
		}
		
		Reserva guardada = reservaRepo.save(reserva);
		return convertirADTO(guardada);
	}

	@Override
	public void cancelar(Long id) {
		Reserva reserva = reservaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", id));
		
		EstadoReserva estadoAnterior = reserva.getEstado();
		reserva.setEstado(EstadoReserva.CANCELADA);
		reservaRepo.save(reserva);
		
		if (estadoAnterior == EstadoReserva.CONFIRMADA) {
			Habitacion habitacion = reserva.getHabitacion();
			if (habitacion.getEstado() == EstadoHabitacion.OCUPADA) {
				LocalDate hoy = LocalDate.now();
				long reservasActivas = reservaRepo.contarSolapadas(habitacion, hoy, hoy,
						List.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA));
				
				if (reservasActivas == 0) {
					habitacion.setEstado(EstadoHabitacion.DISPONIBLE);
					habitacionRepo.save(habitacion);
				}
			}
		}
	}

	@Override
	public void finalizar(Long id) {
		Reserva reserva = reservaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", id));
		
		if (reserva.getEstado() != EstadoReserva.CONFIRMADA)
			throw new BusinessException("Solo se pueden finalizar reservas confirmadas", ErrorCode.RESERVA_ESTADO_INVALIDO.getCode());
		
		reserva.setEstado(EstadoReserva.FINALIZADA);
		reservaRepo.save(reserva);
		
		Habitacion habitacion = reserva.getHabitacion();
		if (habitacion.getEstado() == EstadoHabitacion.OCUPADA) {
			LocalDate hoy = LocalDate.now();
			long reservasActivas = reservaRepo.contarSolapadas(habitacion, hoy, hoy,
					List.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA));
			
			if (reservasActivas == 0 && habitacion.getEstado() != EstadoHabitacion.MANTENIMIENTO) {
				habitacion.setEstado(EstadoHabitacion.DISPONIBLE);
				habitacionRepo.save(habitacion);
			}
		}
	}

	@Override
	public void eliminar(Long id) {
		Reserva reserva = reservaRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", id));
		
		EstadoReserva estadoAnterior = reserva.getEstado();
		Habitacion habitacion = reserva.getHabitacion();
		
		reservaRepo.deleteById(id);
		
		if (estadoAnterior == EstadoReserva.CONFIRMADA) {
			if (habitacion.getEstado() == EstadoHabitacion.OCUPADA) {
				LocalDate hoy = LocalDate.now();
				long reservasActivas = reservaRepo.contarSolapadas(habitacion, hoy, hoy,
						List.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA));
				
				if (reservasActivas == 0) {
					habitacion.setEstado(EstadoHabitacion.DISPONIBLE);
					habitacionRepo.save(habitacion);
				}
			}
		}
	}

	@Override
	public List<ReservaResponseDTO> buscar(Long clienteId, LocalDate fechaIngreso, LocalDate fechaSalida, EstadoReserva estado) {
		return reservaRepo.buscarPorFiltros(clienteId, fechaIngreso, fechaSalida, estado).stream()
				.map(this::convertirADTO)
				.collect(Collectors.toList());
	}

	private ReservaResponseDTO convertirADTO(Reserva reserva) {
		ReservaResponseDTO dto = new ReservaResponseDTO();
		dto.setId(reserva.getId());
		dto.setClienteId(reserva.getCliente().getId());
		dto.setClienteNombre(reserva.getCliente().getNombre());
		dto.setClienteApellido(reserva.getCliente().getApellido());
		dto.setHabitacionId(reserva.getHabitacion().getId());
		dto.setHabitacionNumero(reserva.getHabitacion().getNumero());
		dto.setHabitacionTipo(reserva.getHabitacion().getTipo());
		dto.setFechaIngreso(reserva.getFechaIngreso());
		dto.setFechaSalida(reserva.getFechaSalida());
		dto.setFechaReserva(reserva.getFechaReserva());
		dto.setEstado(reserva.getEstado());

		long noches = ChronoUnit.DAYS.between(reserva.getFechaIngreso(), reserva.getFechaSalida());
		if (noches == 0)
			noches = 1;
		BigDecimal montoHabitacion = reserva.getHabitacion().getPrecioPorNoche()
				.multiply(BigDecimal.valueOf(noches));
		dto.setMontoHabitacion(montoHabitacion);

		BigDecimal montoServicios = BigDecimal.ZERO;
		List<ReservaServicioDTO> serviciosDTO = new ArrayList<>();
		if (reserva.getServicios() != null) {
			for (ReservaServicio rs : reserva.getServicios()) {
				ReservaServicioDTO servicioDTO = new ReservaServicioDTO();
				servicioDTO.setId(rs.getId());
				servicioDTO.setServicioId(rs.getServicio().getId());
				servicioDTO.setServicioNombre(rs.getServicio().getNombreServicio());
				servicioDTO.setServicioPrecio(rs.getServicio().getPrecio());
				servicioDTO.setCantidad(rs.getCantidad());
				BigDecimal subtotal = rs.getServicio().getPrecio()
						.multiply(BigDecimal.valueOf(rs.getCantidad()));
				servicioDTO.setSubtotal(subtotal);
				montoServicios = montoServicios.add(subtotal);
				serviciosDTO.add(servicioDTO);
			}
		}
		dto.setMontoServicios(montoServicios);
		dto.setMontoTotal(montoHabitacion.add(montoServicios));
		dto.setServicios(serviciosDTO);

		return dto;
	}
}
