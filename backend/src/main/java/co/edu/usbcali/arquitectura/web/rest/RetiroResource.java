package co.edu.usbcali.arquitectura.web.rest;

import co.edu.usbcali.arquitectura.repository.RetiroRepository;
import co.edu.usbcali.arquitectura.service.RetiroService;
import co.edu.usbcali.arquitectura.service.dto.RetiroDTO;
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
 * REST controller for managing {@link co.edu.usbcali.arquitectura.domain.Retiro}.
 */
@RestController
@RequestMapping("/api")
public class RetiroResource {

    private final Logger log = LoggerFactory.getLogger(RetiroResource.class);

    private static final String ENTITY_NAME = "backendRetiro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RetiroService retiroService;

    private final RetiroRepository retiroRepository;

    public RetiroResource(RetiroService retiroService, RetiroRepository retiroRepository) {
        this.retiroService = retiroService;
        this.retiroRepository = retiroRepository;
    }

    /**
     * {@code POST  /retiros} : Create a new retiro.
     *
     * @param retiroDTO the retiroDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new retiroDTO, or with status {@code 400 (Bad Request)} if the retiro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/retiros")
    public ResponseEntity<RetiroDTO> createRetiro(@Valid @RequestBody RetiroDTO retiroDTO) throws URISyntaxException {
        log.debug("REST request to save Retiro : {}", retiroDTO);
        if (retiroDTO.getId() != null) {
            throw new BadRequestAlertException("A new retiro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RetiroDTO result = retiroService.save(retiroDTO);
        return ResponseEntity
            .created(new URI("/api/retiros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /retiros/:id} : Updates an existing retiro.
     *
     * @param id the id of the retiroDTO to save.
     * @param retiroDTO the retiroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retiroDTO,
     * or with status {@code 400 (Bad Request)} if the retiroDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the retiroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/retiros/{id}")
    public ResponseEntity<RetiroDTO> updateRetiro(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RetiroDTO retiroDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Retiro : {}, {}", id, retiroDTO);
        if (retiroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, retiroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!retiroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RetiroDTO result = retiroService.update(retiroDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, retiroDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /retiros/:id} : Partial updates given fields of an existing retiro, field will ignore if it is null
     *
     * @param id the id of the retiroDTO to save.
     * @param retiroDTO the retiroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated retiroDTO,
     * or with status {@code 400 (Bad Request)} if the retiroDTO is not valid,
     * or with status {@code 404 (Not Found)} if the retiroDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the retiroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/retiros/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RetiroDTO> partialUpdateRetiro(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RetiroDTO retiroDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Retiro partially : {}, {}", id, retiroDTO);
        if (retiroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, retiroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!retiroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RetiroDTO> result = retiroService.partialUpdate(retiroDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, retiroDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /retiros} : get all the retiros.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of retiros in body.
     */
    @GetMapping("/retiros")
    public ResponseEntity<List<RetiroDTO>> getAllRetiros(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Retiros");
        Page<RetiroDTO> page = retiroService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /retiros/:id} : get the "id" retiro.
     *
     * @param id the id of the retiroDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the retiroDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/retiros/{id}")
    public ResponseEntity<RetiroDTO> getRetiro(@PathVariable Long id) {
        log.debug("REST request to get Retiro : {}", id);
        Optional<RetiroDTO> retiroDTO = retiroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(retiroDTO);
    }

    /**
     * {@code DELETE  /retiros/:id} : delete the "id" retiro.
     *
     * @param id the id of the retiroDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/retiros/{id}")
    public ResponseEntity<Void> deleteRetiro(@PathVariable Long id) {
        log.debug("REST request to delete Retiro : {}", id);
        retiroService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
