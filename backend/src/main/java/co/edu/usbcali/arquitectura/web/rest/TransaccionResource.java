package co.edu.usbcali.arquitectura.web.rest;

import co.edu.usbcali.arquitectura.repository.TransaccionRepository;
import co.edu.usbcali.arquitectura.service.TransaccionService;
import co.edu.usbcali.arquitectura.service.dto.TransaccionDTO;
import co.edu.usbcali.arquitectura.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link co.edu.usbcali.arquitectura.domain.Transaccion}.
 */
@RestController
@RequestMapping("/api")
public class TransaccionResource {

    private final Logger log = LoggerFactory.getLogger(TransaccionResource.class);

    private static final String ENTITY_NAME = "backendTransaccion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransaccionService transaccionService;

    private final TransaccionRepository transaccionRepository;

    public TransaccionResource(TransaccionService transaccionService, TransaccionRepository transaccionRepository) {
        this.transaccionService = transaccionService;
        this.transaccionRepository = transaccionRepository;
    }

    /**
     * {@code POST  /transaccions} : Create a new transaccion.
     *
     * @param transaccionDTO the transaccionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transaccionDTO, or with status {@code 400 (Bad Request)} if the transaccion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transaccions")
    public ResponseEntity<TransaccionDTO> createTransaccion(@Valid @RequestBody TransaccionDTO transaccionDTO) throws URISyntaxException {
        log.debug("REST request to save Transaccion : {}", transaccionDTO);
        if (transaccionDTO.getId() != null) {
            throw new BadRequestAlertException("A new transaccion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransaccionDTO result = transaccionService.save(transaccionDTO);
        return ResponseEntity
            .created(new URI("/api/transaccions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transaccions/:id} : Updates an existing transaccion.
     *
     * @param id the id of the transaccionDTO to save.
     * @param transaccionDTO the transaccionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transaccionDTO,
     * or with status {@code 400 (Bad Request)} if the transaccionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transaccionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transaccions/{id}")
    public ResponseEntity<TransaccionDTO> updateTransaccion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransaccionDTO transaccionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Transaccion : {}, {}", id, transaccionDTO);
        if (transaccionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transaccionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transaccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransaccionDTO result = transaccionService.update(transaccionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transaccionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transaccions/:id} : Partial updates given fields of an existing transaccion, field will ignore if it is null
     *
     * @param id the id of the transaccionDTO to save.
     * @param transaccionDTO the transaccionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transaccionDTO,
     * or with status {@code 400 (Bad Request)} if the transaccionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transaccionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transaccionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transaccions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransaccionDTO> partialUpdateTransaccion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransaccionDTO transaccionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Transaccion partially : {}, {}", id, transaccionDTO);
        if (transaccionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transaccionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transaccionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransaccionDTO> result = transaccionService.partialUpdate(transaccionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transaccionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transaccions} : get all the transaccions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transaccions in body.
     */
    @GetMapping("/transaccions")
    public ResponseEntity<List<TransaccionDTO>> getAllTransaccions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Transaccions");
        Page<TransaccionDTO> page = transaccionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaccions/:id} : get the "id" transaccion.
     *
     * @param id the id of the transaccionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transaccionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaccions/{id}")
    public ResponseEntity<TransaccionDTO> getTransaccion(@PathVariable Long id) {
        log.debug("REST request to get Transaccion : {}", id);
        Optional<TransaccionDTO> transaccionDTO = transaccionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transaccionDTO);
    }

    /**
     * {@code DELETE  /transaccions/:id} : delete the "id" transaccion.
     *
     * @param id the id of the transaccionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transaccions/{id}")
    public ResponseEntity<Void> deleteTransaccion(@PathVariable Long id) {
        log.debug("REST request to delete Transaccion : {}", id);
        transaccionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
