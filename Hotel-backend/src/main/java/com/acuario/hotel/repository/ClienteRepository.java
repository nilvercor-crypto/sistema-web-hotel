package com.acuario.hotel.repository;

import com.acuario.hotel.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	Optional<Cliente> findByDocumentoIdentidad(String documentoIdentidad);

	boolean existsByDocumentoIdentidad(String documentoIdentidad);

	@Query("""
		SELECT c FROM Cliente c
		WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
		OR LOWER(c.apellido) LIKE LOWER(CONCAT('%', :nombre, '%'))
		OR LOWER(c.email) LIKE LOWER(CONCAT('%', :nombre, '%'))
		OR c.documentoIdentidad LIKE CONCAT('%', :nombre, '%')
		""")
	List<Cliente> buscarPorNombreOEmailODocumento(@Param("nombre") String busqueda);
}
