package com.acuario.hotel.repository;

import com.acuario.hotel.model.EstadoHabitacion;
import com.acuario.hotel.model.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
	Optional<Habitacion> findByNumero(String numero);

	boolean existsByNumero(String numero);

	List<Habitacion> findByEstado(EstadoHabitacion estado);

	List<Habitacion> findByTipoIgnoreCase(String tipo);

	@Query("""
			SELECT h FROM Habitacion h
			WHERE h.estado = :estado
			AND (:tipo IS NULL OR LOWER(h.tipo) = LOWER(:tipo))
			AND h.id NOT IN (
				SELECT r.habitacion.id FROM Reserva r
				WHERE r.estado IN ('PENDIENTE', 'CONFIRMADA')
				AND (r.fechaIngreso < :fechaSalida AND :fechaIngreso < r.fechaSalida)
			)
		""")
	List<Habitacion> findDisponiblesPorFechas(
			@Param("estado") EstadoHabitacion estado,
			@Param("fechaIngreso") LocalDate fechaIngreso,
			@Param("fechaSalida") LocalDate fechaSalida,
			@Param("tipo") String tipo);
}
