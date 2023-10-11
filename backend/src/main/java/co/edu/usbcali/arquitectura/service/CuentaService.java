package co.edu.usbcali.arquitectura.service;

import co.edu.usbcali.arquitectura.service.dto.CuentaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.edu.usbcali.arquitectura.domain.Cuenta}.
 */
public interface CuentaService {
    /**
     * Save a cuenta.
     *
     * @param cuentaDTO the entity to save.
     * @return the persisted entity.
     */
    CuentaDTO save(CuentaDTO cuentaDTO);

    /**
     * Updates a cuenta.
     *
     * @param cuentaDTO the entity to update.
     * @return the persisted entity.
     */
    CuentaDTO update(CuentaDTO cuentaDTO);

    /**
     * Partially updates a cuenta.
     *
     * @param cuentaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CuentaDTO> partialUpdate(CuentaDTO cuentaDTO);

    /**
     * Get all the cuentas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CuentaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" cuenta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CuentaDTO> findOne(Long id);

    /**
     * Delete the "id" cuenta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
