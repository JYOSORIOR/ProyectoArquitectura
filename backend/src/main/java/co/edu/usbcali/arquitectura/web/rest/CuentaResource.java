package co.edu.usbcali.arquitectura.web.rest;

import co.edu.usbcali.arquitectura.repository.CuentaRepository;
import co.edu.usbcali.arquitectura.service.CuentaService;
import co.edu.usbcali.arquitectura.service.dto.CuentaDTO;
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
 * REST controller for managing {@link co.edu.usbcali.arquitectura.domain.Cuenta}.
 */
@RestController
@RequestMapping("/api")
public class CuentaResource {

    private final Logger log = LoggerFactory.getLogger(CuentaResource.class);

    private static final String ENTITY_NAME = "backendCuenta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CuentaService cuentaService;

    private final CuentaRepository cuentaRepository;

    public CuentaResource(CuentaService cuentaService, CuentaRepository cuentaRepository) {
        this.cuentaService = cuentaService;
        this.cuentaRepository = cuentaRepository;
    }

    /**
     * {@code POST  /cuentas} : Create a new cuenta.
     *
     * @param cuentaDTO the cuentaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cuentaDTO, or with status {@code 400 (Bad Request)} if the cuenta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cuentas")
    public ResponseEntity<CuentaDTO> createCuenta(@Valid @RequestBody CuentaDTO cuentaDTO) throws URISyntaxException {
        log.debug("REST request to save Cuenta : {}", cuentaDTO);
        if (cuentaDTO.getId() != null) {
            throw new BadRequestAlertException("A new cuenta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CuentaDTO result = cuentaService.save(cuentaDTO);
        return ResponseEntity
            .created(new URI("/api/cuentas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cuentas/:id} : Updates an existing cuenta.
     *
     * @param id the id of the cuentaDTO to save.
     * @param cuentaDTO the cuentaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cuentaDTO,
     * or with status {@code 400 (Bad Request)} if the cuentaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cuentaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cuentas/{id}")
    public ResponseEntity<CuentaDTO> updateCuenta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CuentaDTO cuentaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Cuenta : {}, {}", id, cuentaDTO);
        if (cuentaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cuentaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cuentaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CuentaDTO result = cuentaService.update(cuentaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cuentaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cuentas/:id} : Partial updates given fields of an existing cuenta, field will ignore if it is null
     *
     * @param id the id of the cuentaDTO to save.
     * @param cuentaDTO the cuentaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cuentaDTO,
     * or with status {@code 400 (Bad Request)} if the cuentaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cuentaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cuentaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cuentas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CuentaDTO> partialUpdateCuenta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CuentaDTO cuentaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cuenta partially : {}, {}", id, cuentaDTO);
        if (cuentaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cuentaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cuentaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CuentaDTO> result = cuentaService.partialUpdate(cuentaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cuentaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cuentas} : get all the cuentas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cuentas in body.
     */
    @GetMapping("/cuentas")
    public ResponseEntity<List<CuentaDTO>> getAllCuentas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Cuentas");
        Page<CuentaDTO> page = cuentaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cuentas/:id} : get the "id" cuenta.
     *
     * @param id the id of the cuentaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cuentaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cuentas/{id}")
    public ResponseEntity<CuentaDTO> getCuenta(@PathVariable Long id) {
        log.debug("REST request to get Cuenta : {}", id);
        Optional<CuentaDTO> cuentaDTO = cuentaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cuentaDTO);
    }

    /**
     * {@code DELETE  /cuentas/:id} : delete the "id" cuenta.
     *
     * @param id the id of the cuentaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cuentas/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable Long id) {
        log.debug("REST request to delete Cuenta : {}", id);
        cuentaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
