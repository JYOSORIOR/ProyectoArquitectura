package co.edu.usbcali.arquitectura.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.usbcali.arquitectura.IntegrationTest;
import co.edu.usbcali.arquitectura.domain.Transaccion;
import co.edu.usbcali.arquitectura.repository.TransaccionRepository;
import co.edu.usbcali.arquitectura.service.dto.TransaccionDTO;
import co.edu.usbcali.arquitectura.service.mapper.TransaccionMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link TransaccionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransaccionResourceIT {

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_CANTIDAD = 1D;
    private static final Double UPDATED_CANTIDAD = 2D;

    private static final String ENTITY_API_URL = "/api/transaccions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private TransaccionMapper transaccionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransaccionMockMvc;

    private Transaccion transaccion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaccion createEntity(EntityManager em) {
        Transaccion transaccion = new Transaccion().estado(DEFAULT_ESTADO).fecha(DEFAULT_FECHA).cantidad(DEFAULT_CANTIDAD);
        return transaccion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaccion createUpdatedEntity(EntityManager em) {
        Transaccion transaccion = new Transaccion().estado(UPDATED_ESTADO).fecha(UPDATED_FECHA).cantidad(UPDATED_CANTIDAD);
        return transaccion;
    }

    @BeforeEach
    public void initTest() {
        transaccion = createEntity(em);
    }

    @Test
    @Transactional
    void createTransaccion() throws Exception {
        int databaseSizeBeforeCreate = transaccionRepository.findAll().size();
        // Create the Transaccion
        TransaccionDTO transaccionDTO = transaccionMapper.toDto(transaccion);
        restTransaccionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaccionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeCreate + 1);
        Transaccion testTransaccion = transaccionList.get(transaccionList.size() - 1);
        assertThat(testTransaccion.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testTransaccion.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testTransaccion.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void createTransaccionWithExistingId() throws Exception {
        // Create the Transaccion with an existing ID
        transaccion.setId(1L);
        TransaccionDTO transaccionDTO = transaccionMapper.toDto(transaccion);

        int databaseSizeBeforeCreate = transaccionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransaccionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = transaccionRepository.findAll().size();
        // set the field null
        transaccion.setEstado(null);

        // Create the Transaccion, which fails.
        TransaccionDTO transaccionDTO = transaccionMapper.toDto(transaccion);

        restTransaccionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaccionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = transaccionRepository.findAll().size();
        // set the field null
        transaccion.setFecha(null);

        // Create the Transaccion, which fails.
        TransaccionDTO transaccionDTO = transaccionMapper.toDto(transaccion);

        restTransaccionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaccionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = transaccionRepository.findAll().size();
        // set the field null
        transaccion.setCantidad(null);

        // Create the Transaccion, which fails.
        TransaccionDTO transaccionDTO = transaccionMapper.toDto(transaccion);

        restTransaccionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaccionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransaccions() throws Exception {
        // Initialize the database
        transaccionRepository.saveAndFlush(transaccion);

        // Get all the transaccionList
        restTransaccionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD.doubleValue())));
    }

    @Test
    @Transactional
    void getTransaccion() throws Exception {
        // Initialize the database
        transaccionRepository.saveAndFlush(transaccion);

        // Get the transaccion
        restTransaccionMockMvc
            .perform(get(ENTITY_API_URL_ID, transaccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transaccion.getId().intValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingTransaccion() throws Exception {
        // Get the transaccion
        restTransaccionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransaccion() throws Exception {
        // Initialize the database
        transaccionRepository.saveAndFlush(transaccion);

        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();

        // Update the transaccion
        Transaccion updatedTransaccion = transaccionRepository.findById(transaccion.getId()).get();
        // Disconnect from session so that the updates on updatedTransaccion are not directly saved in db
        em.detach(updatedTransaccion);
        updatedTransaccion.estado(UPDATED_ESTADO).fecha(UPDATED_FECHA).cantidad(UPDATED_CANTIDAD);
        TransaccionDTO transaccionDTO = transaccionMapper.toDto(updatedTransaccion);

        restTransaccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transaccionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaccionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
        Transaccion testTransaccion = transaccionList.get(transaccionList.size() - 1);
        assertThat(testTransaccion.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testTransaccion.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testTransaccion.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void putNonExistingTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();
        transaccion.setId(count.incrementAndGet());

        // Create the Transaccion
        TransaccionDTO transaccionDTO = transaccionMapper.toDto(transaccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransaccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transaccionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();
        transaccion.setId(count.incrementAndGet());

        // Create the Transaccion
        TransaccionDTO transaccionDTO = transaccionMapper.toDto(transaccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransaccionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();
        transaccion.setId(count.incrementAndGet());

        // Create the Transaccion
        TransaccionDTO transaccionDTO = transaccionMapper.toDto(transaccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransaccionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaccionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransaccionWithPatch() throws Exception {
        // Initialize the database
        transaccionRepository.saveAndFlush(transaccion);

        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();

        // Update the transaccion using partial update
        Transaccion partialUpdatedTransaccion = new Transaccion();
        partialUpdatedTransaccion.setId(transaccion.getId());

        restTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransaccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaccion))
            )
            .andExpect(status().isOk());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
        Transaccion testTransaccion = transaccionList.get(transaccionList.size() - 1);
        assertThat(testTransaccion.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testTransaccion.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testTransaccion.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void fullUpdateTransaccionWithPatch() throws Exception {
        // Initialize the database
        transaccionRepository.saveAndFlush(transaccion);

        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();

        // Update the transaccion using partial update
        Transaccion partialUpdatedTransaccion = new Transaccion();
        partialUpdatedTransaccion.setId(transaccion.getId());

        partialUpdatedTransaccion.estado(UPDATED_ESTADO).fecha(UPDATED_FECHA).cantidad(UPDATED_CANTIDAD);

        restTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransaccion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaccion))
            )
            .andExpect(status().isOk());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
        Transaccion testTransaccion = transaccionList.get(transaccionList.size() - 1);
        assertThat(testTransaccion.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testTransaccion.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testTransaccion.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void patchNonExistingTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();
        transaccion.setId(count.incrementAndGet());

        // Create the Transaccion
        TransaccionDTO transaccionDTO = transaccionMapper.toDto(transaccion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transaccionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transaccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();
        transaccion.setId(count.incrementAndGet());

        // Create the Transaccion
        TransaccionDTO transaccionDTO = transaccionMapper.toDto(transaccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transaccionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransaccion() throws Exception {
        int databaseSizeBeforeUpdate = transaccionRepository.findAll().size();
        transaccion.setId(count.incrementAndGet());

        // Create the Transaccion
        TransaccionDTO transaccionDTO = transaccionMapper.toDto(transaccion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransaccionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(transaccionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transaccion in the database
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransaccion() throws Exception {
        // Initialize the database
        transaccionRepository.saveAndFlush(transaccion);

        int databaseSizeBeforeDelete = transaccionRepository.findAll().size();

        // Delete the transaccion
        restTransaccionMockMvc
            .perform(delete(ENTITY_API_URL_ID, transaccion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transaccion> transaccionList = transaccionRepository.findAll();
        assertThat(transaccionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
