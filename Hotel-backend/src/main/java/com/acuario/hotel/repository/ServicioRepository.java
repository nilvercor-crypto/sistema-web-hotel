package com.acuario.hotel.repository;

import com.acuario.hotel.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
	
	Optional<Servicio> findByNombreServicioIgnoreCase(String nombreServicio);
	
	boolean existsByNombreServicioIgnoreCase(String nombreServicio);
}




