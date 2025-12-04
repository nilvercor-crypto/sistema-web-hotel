package com.acuario.hotel.repository;

import com.acuario.hotel.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
	Optional<Hotel> findFirstByOrderByIdAsc();
	boolean existsByRuc(String ruc);
	boolean existsByRucAndIdNot(String ruc, Long id);
}

