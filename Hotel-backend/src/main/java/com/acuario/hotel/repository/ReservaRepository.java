package com.acuario.hotel.repository;

import com.acuario.hotel.model.EstadoReserva;
import com.acuario.hotel.model.Habitacion;
import com.acuario.hotel.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

	@Query("""
			    SELECT COUNT(r) FROM Reserva r
			     WHERE r.habitacion = :habitacion
			       AND r.estado IN :estados
			       AND (r.fechaIngreso < :fechaSalida AND :fechaIngreso < r.fechaSalida)
			""")
	long contarSolapadas(Habitacion habitacion, LocalDate fechaIngreso, LocalDate fechaSalida,
			List<EstadoReserva> estados);

	@Query("""
			    SELECT COUNT(r) FROM Reserva r
			     WHERE r.habitacion = :habitacion
			       AND r.id != :excluirReservaId
			       AND r.estado IN :estados
			       AND (r.fechaIngreso < :fechaSalida AND :fechaIngreso < r.fechaSalida)
			""")
	long contarSolapadasExcluyendo(Habitacion habitacion, LocalDate fechaIngreso, LocalDate fechaSalida,
			List<EstadoReserva> estados, Long excluirReservaId);

	@Query("SELECT COUNT(r) FROM Reserva r WHERE DATE(r.fechaReserva) = :fecha")
	long countByFechaCreacion(@Param("fecha") LocalDate fecha);

	@Query("SELECT COUNT(r) FROM Reserva r WHERE YEAR(r.fechaReserva) = :anio AND MONTH(r.fechaReserva) = :mes")
	long countByAnioAndMes(@Param("anio") int anio, @Param("mes") int mes);

	@Query("SELECT COUNT(r) FROM Reserva r WHERE YEAR(r.fechaReserva) = :anio")
	long countByAnio(@Param("anio") int anio);

	@Query("SELECT COUNT(r) FROM Reserva r WHERE r.estado = :estado")
	long countByEstado(@Param("estado") EstadoReserva estado);

	@Query("""
		SELECT COALESCE(SUM(r.montoTotal), 0) FROM Reserva r
		WHERE r.estado IN ('CONFIRMADA', 'FINALIZADA')
	""")
	BigDecimal sumIngresosTotales();

	@Query("""
		SELECT COALESCE(SUM(r.montoTotal), 0) FROM Reserva r
		WHERE r.estado IN ('CONFIRMADA', 'FINALIZADA')
		AND YEAR(r.fechaReserva) = :anio AND MONTH(r.fechaReserva) = :mes
	""")
	BigDecimal sumIngresosPorMes(@Param("anio") int anio, @Param("mes") int mes);

	@Query("""
		SELECT COALESCE(SUM(r.montoTotal), 0) FROM Reserva r
		WHERE r.estado IN ('CONFIRMADA', 'FINALIZADA')
		AND YEAR(r.fechaReserva) = :anio
	""")
	BigDecimal sumIngresosPorAnio(@Param("anio") int anio);

	@Query("""
		SELECT r FROM Reserva r
		WHERE (:clienteId IS NULL OR r.cliente.id = :clienteId)
		AND (:fechaIngreso IS NULL OR r.fechaIngreso >= :fechaIngreso)
		AND (:fechaSalida IS NULL OR r.fechaSalida <= :fechaSalida)
		AND (:estado IS NULL OR r.estado = :estado)
		ORDER BY r.fechaReserva DESC
		""")
	List<Reserva> buscarPorFiltros(
			@Param("clienteId") Long clienteId,
			@Param("fechaIngreso") LocalDate fechaIngreso,
			@Param("fechaSalida") LocalDate fechaSalida,
			@Param("estado") EstadoReserva estado);
}
