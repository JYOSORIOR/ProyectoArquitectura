package co.edu.usbcali.arquitectura.service;

import co.edu.usbcali.arquitectura.service.dto.RetiroDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.edu.usbcali.arquitectura.domain.Retiro}.
 */
public interface RetiroService {
    /**
     * Save a retiro.
     *
     * @param retiroDTO the entity to save.
     * @return the persisted entity.
     */
    RetiroDTO save(RetiroDTO retiroDTO);

    /**
     * Updates a retiro.
     *
     * @param retiroDTO the entity to update.
     * @return the persisted entity.
     */
    RetiroDTO update(RetiroDTO retiroDTO);

    /**
     * Partially updates a retiro.
     *
     * @param retiroDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RetiroDTO> partialUpdate(RetiroDTO retiroDTO);

    /**
     * Get all the retiros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RetiroDTO> findAll(Pageable pageable);

    /**
     * Get the "id" retiro.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RetiroDTO> findOne(Long id);

    /**
     * Delete the "id" retiro.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
