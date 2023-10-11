package co.edu.usbcali.arquitectura.repository;

import co.edu.usbcali.arquitectura.domain.Retiro;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Retiro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RetiroRepository extends JpaRepository<Retiro, Long> {}
