package co.edu.usbcali.arquitectura.service;

import co.edu.usbcali.arquitectura.service.dto.TransaccionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.edu.usbcali.arquitectura.domain.Transaccion}.
 */
public interface TransaccionService {
    /**
     * Save a transaccion.
     *
     * @param transaccionDTO the entity to save.
     * @return the persisted entity.
     */
    TransaccionDTO save(TransaccionDTO transaccionDTO);

    /**
     * Updates a transaccion.
     *
     * @param transaccionDTO the entity to update.
     * @return the persisted entity.
     */
    TransaccionDTO update(TransaccionDTO transaccionDTO);

    /**
     * Partially updates a transaccion.
     *
     * @param transaccionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransaccionDTO> partialUpdate(TransaccionDTO transaccionDTO);

    /**
     * Get all the transaccions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransaccionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" transaccion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransaccionDTO> findOne(Long id);

    /**
     * Delete the "id" transaccion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
