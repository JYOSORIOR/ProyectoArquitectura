package co.edu.usbcali.arquitectura.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.usbcali.arquitectura.IntegrationTest;
import co.edu.usbcali.arquitectura.domain.Retiro;
import co.edu.usbcali.arquitectura.repository.RetiroRepository;
import co.edu.usbcali.arquitectura.service.dto.RetiroDTO;
import co.edu.usbcali.arquitectura.service.mapper.RetiroMapper;
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
 * Integration tests for the {@link RetiroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RetiroResourceIT {

    private static final String DEFAULT_CONTIENE = "AAAAAAAAAA";
    private static final String UPDATED_CONTIENE = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final Double DEFAULT_CANTIDAD = 1D;
    private static final Double UPDATED_CANTIDAD = 2D;

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/retiros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RetiroRepository retiroRepository;

    @Autowired
    private RetiroMapper retiroMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRetiroMockMvc;

    private Retiro retiro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Retiro createEntity(EntityManager em) {
        Retiro retiro = new Retiro().contiene(DEFAULT_CONTIENE).estado(DEFAULT_ESTADO).cantidad(DEFAULT_CANTIDAD).fecha(DEFAULT_FECHA);
        return retiro;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Retiro createUpdatedEntity(EntityManager em) {
        Retiro retiro = new Retiro().contiene(UPDATED_CONTIENE).estado(UPDATED_ESTADO).cantidad(UPDATED_CANTIDAD).fecha(UPDATED_FECHA);
        return retiro;
    }

    @BeforeEach
    public void initTest() {
        retiro = createEntity(em);
    }

    @Test
    @Transactional
    void createRetiro() throws Exception {
        int databaseSizeBeforeCreate = retiroRepository.findAll().size();
        // Create the Retiro
        RetiroDTO retiroDTO = retiroMapper.toDto(retiro);
        restRetiroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retiroDTO)))
            .andExpect(status().isCreated());

        // Validate the Retiro in the database
        List<Retiro> retiroList = retiroRepository.findAll();
        assertThat(retiroList).hasSize(databaseSizeBeforeCreate + 1);
        Retiro testRetiro = retiroList.get(retiroList.size() - 1);
        assertThat(testRetiro.getContiene()).isEqualTo(DEFAULT_CONTIENE);
        assertThat(testRetiro.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testRetiro.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testRetiro.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void createRetiroWithExistingId() throws Exception {
        // Create the Retiro with an existing ID
        retiro.setId(1L);
        RetiroDTO retiroDTO = retiroMapper.toDto(retiro);

        int databaseSizeBeforeCreate = retiroRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRetiroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retiroDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Retiro in the database
        List<Retiro> retiroList = retiroRepository.findAll();
        assertThat(retiroList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkContieneIsRequired() throws Exception {
        int databaseSizeBeforeTest = retiroRepository.findAll().size();
        // set the field null
        retiro.setContiene(null);

        // Create the Retiro, which fails.
        RetiroDTO retiroDTO = retiroMapper.toDto(retiro);

        restRetiroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retiroDTO)))
            .andExpect(status().isBadRequest());

        List<Retiro> retiroList = retiroRepository.findAll();
        assertThat(retiroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = retiroRepository.findAll().size();
        // set the field null
        retiro.setEstado(null);

        // Create the Retiro, which fails.
        RetiroDTO retiroDTO = retiroMapper.toDto(retiro);

        restRetiroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retiroDTO)))
            .andExpect(status().isBadRequest());

        List<Retiro> retiroList = retiroRepository.findAll();
        assertThat(retiroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = retiroRepository.findAll().size();
        // set the field null
        retiro.setCantidad(null);

        // Create the Retiro, which fails.
        RetiroDTO retiroDTO = retiroMapper.toDto(retiro);

        restRetiroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retiroDTO)))
            .andExpect(status().isBadRequest());

        List<Retiro> retiroList = retiroRepository.findAll();
        assertThat(retiroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = retiroRepository.findAll().size();
        // set the field null
        retiro.setFecha(null);

        // Create the Retiro, which fails.
        RetiroDTO retiroDTO = retiroMapper.toDto(retiro);

        restRetiroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retiroDTO)))
            .andExpect(status().isBadRequest());

        List<Retiro> retiroList = retiroRepository.findAll();
        assertThat(retiroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRetiros() throws Exception {
        // Initialize the database
        retiroRepository.saveAndFlush(retiro);

        // Get all the retiroList
        restRetiroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(retiro.getId().intValue())))
            .andExpect(jsonPath("$.[*].contiene").value(hasItem(DEFAULT_CONTIENE)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD.doubleValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }

    @Test
    @Transactional
    void getRetiro() throws Exception {
        // Initialize the database
        retiroRepository.saveAndFlush(retiro);

        // Get the retiro
        restRetiroMockMvc
            .perform(get(ENTITY_API_URL_ID, retiro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(retiro.getId().intValue()))
            .andExpect(jsonPath("$.contiene").value(DEFAULT_CONTIENE))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD.doubleValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRetiro() throws Exception {
        // Get the retiro
        restRetiroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRetiro() throws Exception {
        // Initialize the database
        retiroRepository.saveAndFlush(retiro);

        int databaseSizeBeforeUpdate = retiroRepository.findAll().size();

        // Update the retiro
        Retiro updatedRetiro = retiroRepository.findById(retiro.getId()).get();
        // Disconnect from session so that the updates on updatedRetiro are not directly saved in db
        em.detach(updatedRetiro);
        updatedRetiro.contiene(UPDATED_CONTIENE).estado(UPDATED_ESTADO).cantidad(UPDATED_CANTIDAD).fecha(UPDATED_FECHA);
        RetiroDTO retiroDTO = retiroMapper.toDto(updatedRetiro);

        restRetiroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, retiroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retiroDTO))
            )
            .andExpect(status().isOk());

        // Validate the Retiro in the database
        List<Retiro> retiroList = retiroRepository.findAll();
        assertThat(retiroList).hasSize(databaseSizeBeforeUpdate);
        Retiro testRetiro = retiroList.get(retiroList.size() - 1);
        assertThat(testRetiro.getContiene()).isEqualTo(UPDATED_CONTIENE);
        assertThat(testRetiro.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testRetiro.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testRetiro.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void putNonExistingRetiro() throws Exception {
        int databaseSizeBeforeUpdate = retiroRepository.findAll().size();
        retiro.setId(count.incrementAndGet());

        // Create the Retiro
        RetiroDTO retiroDTO = retiroMapper.toDto(retiro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRetiroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, retiroDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retiroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Retiro in the database
        List<Retiro> retiroList = retiroRepository.findAll();
        assertThat(retiroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRetiro() throws Exception {
        int databaseSizeBeforeUpdate = retiroRepository.findAll().size();
        retiro.setId(count.incrementAndGet());

        // Create the Retiro
        RetiroDTO retiroDTO = retiroMapper.toDto(retiro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetiroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(retiroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Retiro in the database
        List<Retiro> retiroList = retiroRepository.findAll();
        assertThat(retiroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRetiro() throws Exception {
        int databaseSizeBeforeUpdate = retiroRepository.findAll().size();
        retiro.setId(count.incrementAndGet());

        // Create the Retiro
        RetiroDTO retiroDTO = retiroMapper.toDto(retiro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetiroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(retiroDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Retiro in the database
        List<Retiro> retiroList = retiroRepository.findAll();
        assertThat(retiroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRetiroWithPatch() throws Exception {
        // Initialize the database
        retiroRepository.saveAndFlush(retiro);

        int databaseSizeBeforeUpdate = retiroRepository.findAll().size();

        // Update the retiro using partial update
        Retiro partialUpdatedRetiro = new Retiro();
        partialUpdatedRetiro.setId(retiro.getId());

        partialUpdatedRetiro.cantidad(UPDATED_CANTIDAD).fecha(UPDATED_FECHA);

        restRetiroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRetiro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRetiro))
            )
            .andExpect(status().isOk());

        // Validate the Retiro in the database
        List<Retiro> retiroList = retiroRepository.findAll();
        assertThat(retiroList).hasSize(databaseSizeBeforeUpdate);
        Retiro testRetiro = retiroList.get(retiroList.size() - 1);
        assertThat(testRetiro.getContiene()).isEqualTo(DEFAULT_CONTIENE);
        assertThat(testRetiro.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testRetiro.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testRetiro.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void fullUpdateRetiroWithPatch() throws Exception {
        // Initialize the database
        retiroRepository.saveAndFlush(retiro);

        int databaseSizeBeforeUpdate = retiroRepository.findAll().size();

        // Update the retiro using partial update
        Retiro partialUpdatedRetiro = new Retiro();
        partialUpdatedRetiro.setId(retiro.getId());

        partialUpdatedRetiro.contiene(UPDATED_CONTIENE).estado(UPDATED_ESTADO).cantidad(UPDATED_CANTIDAD).fecha(UPDATED_FECHA);

        restRetiroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRetiro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRetiro))
            )
            .andExpect(status().isOk());

        // Validate the Retiro in the database
        List<Retiro> retiroList = retiroRepository.findAll();
        assertThat(retiroList).hasSize(databaseSizeBeforeUpdate);
        Retiro testRetiro = retiroList.get(retiroList.size() - 1);
        assertThat(testRetiro.getContiene()).isEqualTo(UPDATED_CONTIENE);
        assertThat(testRetiro.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testRetiro.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testRetiro.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void patchNonExistingRetiro() throws Exception {
        int databaseSizeBeforeUpdate = retiroRepository.findAll().size();
        retiro.setId(count.incrementAndGet());

        // Create the Retiro
        RetiroDTO retiroDTO = retiroMapper.toDto(retiro);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRetiroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, retiroDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retiroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Retiro in the database
        List<Retiro> retiroList = retiroRepository.findAll();
        assertThat(retiroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRetiro() throws Exception {
        int databaseSizeBeforeUpdate = retiroRepository.findAll().size();
        retiro.setId(count.incrementAndGet());

        // Create the Retiro
        RetiroDTO retiroDTO = retiroMapper.toDto(retiro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetiroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(retiroDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Retiro in the database
        List<Retiro> retiroList = retiroRepository.findAll();
        assertThat(retiroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRetiro() throws Exception {
        int databaseSizeBeforeUpdate = retiroRepository.findAll().size();
        retiro.setId(count.incrementAndGet());

        // Create the Retiro
        RetiroDTO retiroDTO = retiroMapper.toDto(retiro);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRetiroMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(retiroDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Retiro in the database
        List<Retiro> retiroList = retiroRepository.findAll();
        assertThat(retiroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRetiro() throws Exception {
        // Initialize the database
        retiroRepository.saveAndFlush(retiro);

        int databaseSizeBeforeDelete = retiroRepository.findAll().size();

        // Delete the retiro
        restRetiroMockMvc
            .perform(delete(ENTITY_API_URL_ID, retiro.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Retiro> retiroList = retiroRepository.findAll();
        assertThat(retiroList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
