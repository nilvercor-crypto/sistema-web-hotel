package com.acuario.hotel.repository;

import com.acuario.hotel.model.ReservaServicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaServicioRepository extends JpaRepository<ReservaServicio, Long> {
	
	List<ReservaServicio> findByReservaId(Long reservaId);
	
	void deleteByReservaId(Long reservaId);
}




