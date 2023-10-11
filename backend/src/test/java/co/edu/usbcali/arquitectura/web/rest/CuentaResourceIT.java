package co.edu.usbcali.arquitectura.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.usbcali.arquitectura.IntegrationTest;
import co.edu.usbcali.arquitectura.domain.Cuenta;
import co.edu.usbcali.arquitectura.repository.CuentaRepository;
import co.edu.usbcali.arquitectura.service.dto.CuentaDTO;
import co.edu.usbcali.arquitectura.service.mapper.CuentaMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CuentaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CuentaResourceIT {

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final Double DEFAULT_SALDO = 1D;
    private static final Double UPDATED_SALDO = 2D;

    private static final String ENTITY_API_URL = "/api/cuentas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private CuentaMapper cuentaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCuentaMockMvc;

    private Cuenta cuenta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cuenta createEntity(EntityManager em) {
        Cuenta cuenta = new Cuenta().tipo(DEFAULT_TIPO).estado(DEFAULT_ESTADO).saldo(DEFAULT_SALDO);
        return cuenta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cuenta createUpdatedEntity(EntityManager em) {
        Cuenta cuenta = new Cuenta().tipo(UPDATED_TIPO).estado(UPDATED_ESTADO).saldo(UPDATED_SALDO);
        return cuenta;
    }

    @BeforeEach
    public void initTest() {
        cuenta = createEntity(em);
    }

    @Test
    @Transactional
    void createCuenta() throws Exception {
        int databaseSizeBeforeCreate = cuentaRepository.findAll().size();
        // Create the Cuenta
        CuentaDTO cuentaDTO = cuentaMapper.toDto(cuenta);
        restCuentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuentaDTO)))
            .andExpect(status().isCreated());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeCreate + 1);
        Cuenta testCuenta = cuentaList.get(cuentaList.size() - 1);
        assertThat(testCuenta.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testCuenta.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testCuenta.getSaldo()).isEqualTo(DEFAULT_SALDO);
    }

    @Test
    @Transactional
    void createCuentaWithExistingId() throws Exception {
        // Create the Cuenta with an existing ID
        cuenta.setId(1L);
        CuentaDTO cuentaDTO = cuentaMapper.toDto(cuenta);

        int databaseSizeBeforeCreate = cuentaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCuentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuentaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaRepository.findAll().size();
        // set the field null
        cuenta.setTipo(null);

        // Create the Cuenta, which fails.
        CuentaDTO cuentaDTO = cuentaMapper.toDto(cuenta);

        restCuentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuentaDTO)))
            .andExpect(status().isBadRequest());

        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaRepository.findAll().size();
        // set the field null
        cuenta.setEstado(null);

        // Create the Cuenta, which fails.
        CuentaDTO cuentaDTO = cuentaMapper.toDto(cuenta);

        restCuentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuentaDTO)))
            .andExpect(status().isBadRequest());

        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSaldoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuentaRepository.findAll().size();
        // set the field null
        cuenta.setSaldo(null);

        // Create the Cuenta, which fails.
        CuentaDTO cuentaDTO = cuentaMapper.toDto(cuenta);

        restCuentaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuentaDTO)))
            .andExpect(status().isBadRequest());

        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCuentas() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get all the cuentaList
        restCuentaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuenta.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].saldo").value(hasItem(DEFAULT_SALDO.doubleValue())));
    }

    @Test
    @Transactional
    void getCuenta() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        // Get the cuenta
        restCuentaMockMvc
            .perform(get(ENTITY_API_URL_ID, cuenta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cuenta.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.saldo").value(DEFAULT_SALDO.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCuenta() throws Exception {
        // Get the cuenta
        restCuentaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCuenta() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        int databaseSizeBeforeUpdate = cuentaRepository.findAll().size();

        // Update the cuenta
        Cuenta updatedCuenta = cuentaRepository.findById(cuenta.getId()).get();
        // Disconnect from session so that the updates on updatedCuenta are not directly saved in db
        em.detach(updatedCuenta);
        updatedCuenta.tipo(UPDATED_TIPO).estado(UPDATED_ESTADO).saldo(UPDATED_SALDO);
        CuentaDTO cuentaDTO = cuentaMapper.toDto(updatedCuenta);

        restCuentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cuentaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cuentaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeUpdate);
        Cuenta testCuenta = cuentaList.get(cuentaList.size() - 1);
        assertThat(testCuenta.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testCuenta.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testCuenta.getSaldo()).isEqualTo(UPDATED_SALDO);
    }

    @Test
    @Transactional
    void putNonExistingCuenta() throws Exception {
        int databaseSizeBeforeUpdate = cuentaRepository.findAll().size();
        cuenta.setId(count.incrementAndGet());

        // Create the Cuenta
        CuentaDTO cuentaDTO = cuentaMapper.toDto(cuenta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCuentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cuentaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cuentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCuenta() throws Exception {
        int databaseSizeBeforeUpdate = cuentaRepository.findAll().size();
        cuenta.setId(count.incrementAndGet());

        // Create the Cuenta
        CuentaDTO cuentaDTO = cuentaMapper.toDto(cuenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cuentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCuenta() throws Exception {
        int databaseSizeBeforeUpdate = cuentaRepository.findAll().size();
        cuenta.setId(count.incrementAndGet());

        // Create the Cuenta
        CuentaDTO cuentaDTO = cuentaMapper.toDto(cuenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuentaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuentaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCuentaWithPatch() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        int databaseSizeBeforeUpdate = cuentaRepository.findAll().size();

        // Update the cuenta using partial update
        Cuenta partialUpdatedCuenta = new Cuenta();
        partialUpdatedCuenta.setId(cuenta.getId());

        partialUpdatedCuenta.tipo(UPDATED_TIPO).estado(UPDATED_ESTADO).saldo(UPDATED_SALDO);

        restCuentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCuenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCuenta))
            )
            .andExpect(status().isOk());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeUpdate);
        Cuenta testCuenta = cuentaList.get(cuentaList.size() - 1);
        assertThat(testCuenta.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testCuenta.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testCuenta.getSaldo()).isEqualTo(UPDATED_SALDO);
    }

    @Test
    @Transactional
    void fullUpdateCuentaWithPatch() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        int databaseSizeBeforeUpdate = cuentaRepository.findAll().size();

        // Update the cuenta using partial update
        Cuenta partialUpdatedCuenta = new Cuenta();
        partialUpdatedCuenta.setId(cuenta.getId());

        partialUpdatedCuenta.tipo(UPDATED_TIPO).estado(UPDATED_ESTADO).saldo(UPDATED_SALDO);

        restCuentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCuenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCuenta))
            )
            .andExpect(status().isOk());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeUpdate);
        Cuenta testCuenta = cuentaList.get(cuentaList.size() - 1);
        assertThat(testCuenta.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testCuenta.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testCuenta.getSaldo()).isEqualTo(UPDATED_SALDO);
    }

    @Test
    @Transactional
    void patchNonExistingCuenta() throws Exception {
        int databaseSizeBeforeUpdate = cuentaRepository.findAll().size();
        cuenta.setId(count.incrementAndGet());

        // Create the Cuenta
        CuentaDTO cuentaDTO = cuentaMapper.toDto(cuenta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCuentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cuentaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cuentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCuenta() throws Exception {
        int databaseSizeBeforeUpdate = cuentaRepository.findAll().size();
        cuenta.setId(count.incrementAndGet());

        // Create the Cuenta
        CuentaDTO cuentaDTO = cuentaMapper.toDto(cuenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cuentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCuenta() throws Exception {
        int databaseSizeBeforeUpdate = cuentaRepository.findAll().size();
        cuenta.setId(count.incrementAndGet());

        // Create the Cuenta
        CuentaDTO cuentaDTO = cuentaMapper.toDto(cuenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuentaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cuentaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cuenta in the database
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCuenta() throws Exception {
        // Initialize the database
        cuentaRepository.saveAndFlush(cuenta);

        int databaseSizeBeforeDelete = cuentaRepository.findAll().size();

        // Delete the cuenta
        restCuentaMockMvc
            .perform(delete(ENTITY_API_URL_ID, cuenta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cuenta> cuentaList = cuentaRepository.findAll();
        assertThat(cuentaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
