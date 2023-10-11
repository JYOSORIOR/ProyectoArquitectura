package co.edu.usbcali.arquitectura.web.rest;

import co.edu.usbcali.arquitectura.repository.ATMRepository;
import co.edu.usbcali.arquitectura.service.ATMService;
import co.edu.usbcali.arquitectura.service.dto.ATMDTO;
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
 * REST controller for managing {@link co.edu.usbcali.arquitectura.domain.ATM}.
 */
@RestController
@RequestMapping("/api")
public class ATMResource {

    private final Logger log = LoggerFactory.getLogger(ATMResource.class);

    private static final String ENTITY_NAME = "backendAtm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ATMService aTMService;

    private final ATMRepository aTMRepository;

    public ATMResource(ATMService aTMService, ATMRepository aTMRepository) {
        this.aTMService = aTMService;
        this.aTMRepository = aTMRepository;
    }

    /**
     * {@code POST  /atms} : Create a new aTM.
     *
     * @param aTMDTO the aTMDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aTMDTO, or with status {@code 400 (Bad Request)} if the aTM has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/atms")
    public ResponseEntity<ATMDTO> createATM(@Valid @RequestBody ATMDTO aTMDTO) throws URISyntaxException {
        log.debug("REST request to save ATM : {}", aTMDTO);
        if (aTMDTO.getId() != null) {
            throw new BadRequestAlertException("A new aTM cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ATMDTO result = aTMService.save(aTMDTO);
        return ResponseEntity
            .created(new URI("/api/atms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /atms/:id} : Updates an existing aTM.
     *
     * @param id the id of the aTMDTO to save.
     * @param aTMDTO the aTMDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aTMDTO,
     * or with status {@code 400 (Bad Request)} if the aTMDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aTMDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/atms/{id}")
    public ResponseEntity<ATMDTO> updateATM(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody ATMDTO aTMDTO)
        throws URISyntaxException {
        log.debug("REST request to update ATM : {}, {}", id, aTMDTO);
        if (aTMDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aTMDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aTMRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ATMDTO result = aTMService.update(aTMDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aTMDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /atms/:id} : Partial updates given fields of an existing aTM, field will ignore if it is null
     *
     * @param id the id of the aTMDTO to save.
     * @param aTMDTO the aTMDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aTMDTO,
     * or with status {@code 400 (Bad Request)} if the aTMDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aTMDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aTMDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/atms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ATMDTO> partialUpdateATM(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ATMDTO aTMDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ATM partially : {}, {}", id, aTMDTO);
        if (aTMDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aTMDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aTMRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ATMDTO> result = aTMService.partialUpdate(aTMDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aTMDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /atms} : get all the aTMS.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aTMS in body.
     */
    @GetMapping("/atms")
    public ResponseEntity<List<ATMDTO>> getAllATMS(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ATMS");
        Page<ATMDTO> page = aTMService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /atms/:id} : get the "id" aTM.
     *
     * @param id the id of the aTMDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aTMDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/atms/{id}")
    public ResponseEntity<ATMDTO> getATM(@PathVariable Long id) {
        log.debug("REST request to get ATM : {}", id);
        Optional<ATMDTO> aTMDTO = aTMService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aTMDTO);
    }

    /**
     * {@code DELETE  /atms/:id} : delete the "id" aTM.
     *
     * @param id the id of the aTMDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/atms/{id}")
    public ResponseEntity<Void> deleteATM(@PathVariable Long id) {
        log.debug("REST request to delete ATM : {}", id);
        aTMService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
