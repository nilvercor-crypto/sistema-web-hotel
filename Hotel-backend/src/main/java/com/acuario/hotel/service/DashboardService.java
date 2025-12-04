package com.acuario.hotel.service;

import com.acuario.hotel.dto.DashboardStatsDTO;
import com.acuario.hotel.model.EstadoHabitacion;
import com.acuario.hotel.model.EstadoReserva;
import com.acuario.hotel.repository.ClienteRepository;
import com.acuario.hotel.repository.HabitacionRepository;
import com.acuario.hotel.repository.ReservaRepository;
import com.acuario.hotel.repository.ServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional
public class DashboardService {

    private final ReservaRepository reservaRepository;
    private final HabitacionRepository habitacionRepository;
    private final ClienteRepository clienteRepository;
    private final ServicioRepository servicioRepository;

    public DashboardService(
            ReservaRepository reservaRepository,
            HabitacionRepository habitacionRepository,
            ClienteRepository clienteRepository,
            ServicioRepository servicioRepository) {
        this.reservaRepository = reservaRepository;
        this.habitacionRepository = habitacionRepository;
        this.clienteRepository = clienteRepository;
        this.servicioRepository = servicioRepository;
    }

    public DashboardStatsDTO obtenerEstadisticas() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        LocalDate hoy = LocalDate.now();
        int anio = hoy.getYear();
        int mes = hoy.getMonthValue();

        stats.setTotalReservasHoy(reservaRepository.countByFechaCreacion(hoy));
        stats.setTotalReservasMes(reservaRepository.countByAnioAndMes(anio, mes));
        stats.setTotalReservasAnio(reservaRepository.countByAnio(anio));

        BigDecimal ingresosTotales = reservaRepository.sumIngresosTotales();
        stats.setIngresosTotales(ingresosTotales != null ? ingresosTotales : BigDecimal.ZERO);

        BigDecimal ingresosMes = reservaRepository.sumIngresosPorMes(anio, mes);
        stats.setIngresosMes(ingresosMes != null ? ingresosMes : BigDecimal.ZERO);

        BigDecimal ingresosAnio = reservaRepository.sumIngresosPorAnio(anio);
        stats.setIngresosAnio(ingresosAnio != null ? ingresosAnio : BigDecimal.ZERO);

        stats.setHabitacionesOcupadas((long) habitacionRepository.findByEstado(EstadoHabitacion.OCUPADA).size());
        stats.setHabitacionesDisponibles((long) habitacionRepository.findByEstado(EstadoHabitacion.DISPONIBLE).size());
        stats.setHabitacionesMantenimiento((long) habitacionRepository.findByEstado(EstadoHabitacion.MANTENIMIENTO).size());

        stats.setReservasPendientes(reservaRepository.countByEstado(EstadoReserva.PENDIENTE));
        stats.setReservasConfirmadas(reservaRepository.countByEstado(EstadoReserva.CONFIRMADA));
        stats.setReservasCanceladas(reservaRepository.countByEstado(EstadoReserva.CANCELADA));
        stats.setReservasFinalizadas(reservaRepository.countByEstado(EstadoReserva.FINALIZADA));

        stats.setTotalClientes(clienteRepository.count());
        stats.setTotalHabitaciones(habitacionRepository.count());
        stats.setTotalServicios(servicioRepository.count());

        return stats;
    }
}

