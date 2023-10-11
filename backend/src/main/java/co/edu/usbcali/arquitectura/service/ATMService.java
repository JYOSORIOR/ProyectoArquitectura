package co.edu.usbcali.arquitectura.service;

import co.edu.usbcali.arquitectura.service.dto.ATMDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.edu.usbcali.arquitectura.domain.ATM}.
 */
public interface ATMService {
    /**
     * Save a aTM.
     *
     * @param aTMDTO the entity to save.
     * @return the persisted entity.
     */
    ATMDTO save(ATMDTO aTMDTO);

    /**
     * Updates a aTM.
     *
     * @param aTMDTO the entity to update.
     * @return the persisted entity.
     */
    ATMDTO update(ATMDTO aTMDTO);

    /**
     * Partially updates a aTM.
     *
     * @param aTMDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ATMDTO> partialUpdate(ATMDTO aTMDTO);

    /**
     * Get all the aTMS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ATMDTO> findAll(Pageable pageable);

    /**
     * Get the "id" aTM.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ATMDTO> findOne(Long id);

    /**
     * Delete the "id" aTM.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
