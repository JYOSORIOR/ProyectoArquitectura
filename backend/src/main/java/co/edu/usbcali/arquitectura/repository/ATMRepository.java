package co.edu.usbcali.arquitectura.repository;

import co.edu.usbcali.arquitectura.domain.ATM;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ATM entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ATMRepository extends JpaRepository<ATM, Long> {}
