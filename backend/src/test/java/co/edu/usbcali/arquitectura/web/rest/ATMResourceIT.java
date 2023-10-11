package co.edu.usbcali.arquitectura.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.usbcali.arquitectura.IntegrationTest;
import co.edu.usbcali.arquitectura.domain.ATM;
import co.edu.usbcali.arquitectura.repository.ATMRepository;
import co.edu.usbcali.arquitectura.service.dto.ATMDTO;
import co.edu.usbcali.arquitectura.service.mapper.ATMMapper;
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
 * Integration tests for the {@link ATMResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ATMResourceIT {

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final Double DEFAULT_SALDODISPONIBLE = 1D;
    private static final Double UPDATED_SALDODISPONIBLE = 2D;

    private static final String DEFAULT_UBICACION = "AAAAAAAAAA";
    private static final String UPDATED_UBICACION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/atms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ATMRepository aTMRepository;

    @Autowired
    private ATMMapper aTMMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restATMMockMvc;

    private ATM aTM;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ATM createEntity(EntityManager em) {
        ATM aTM = new ATM().estado(DEFAULT_ESTADO).tipo(DEFAULT_TIPO).saldodisponible(DEFAULT_SALDODISPONIBLE).ubicacion(DEFAULT_UBICACION);
        return aTM;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ATM createUpdatedEntity(EntityManager em) {
        ATM aTM = new ATM().estado(UPDATED_ESTADO).tipo(UPDATED_TIPO).saldodisponible(UPDATED_SALDODISPONIBLE).ubicacion(UPDATED_UBICACION);
        return aTM;
    }

    @BeforeEach
    public void initTest() {
        aTM = createEntity(em);
    }

    @Test
    @Transactional
    void createATM() throws Exception {
        int databaseSizeBeforeCreate = aTMRepository.findAll().size();
        // Create the ATM
        ATMDTO aTMDTO = aTMMapper.toDto(aTM);
        restATMMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aTMDTO)))
            .andExpect(status().isCreated());

        // Validate the ATM in the database
        List<ATM> aTMList = aTMRepository.findAll();
        assertThat(aTMList).hasSize(databaseSizeBeforeCreate + 1);
        ATM testATM = aTMList.get(aTMList.size() - 1);
        assertThat(testATM.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testATM.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testATM.getSaldodisponible()).isEqualTo(DEFAULT_SALDODISPONIBLE);
        assertThat(testATM.getUbicacion()).isEqualTo(DEFAULT_UBICACION);
    }

    @Test
    @Transactional
    void createATMWithExistingId() throws Exception {
        // Create the ATM with an existing ID
        aTM.setId(1L);
        ATMDTO aTMDTO = aTMMapper.toDto(aTM);

        int databaseSizeBeforeCreate = aTMRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restATMMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aTMDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ATM in the database
        List<ATM> aTMList = aTMRepository.findAll();
        assertThat(aTMList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = aTMRepository.findAll().size();
        // set the field null
        aTM.setEstado(null);

        // Create the ATM, which fails.
        ATMDTO aTMDTO = aTMMapper.toDto(aTM);

        restATMMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aTMDTO)))
            .andExpect(status().isBadRequest());

        List<ATM> aTMList = aTMRepository.findAll();
        assertThat(aTMList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = aTMRepository.findAll().size();
        // set the field null
        aTM.setTipo(null);

        // Create the ATM, which fails.
        ATMDTO aTMDTO = aTMMapper.toDto(aTM);

        restATMMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aTMDTO)))
            .andExpect(status().isBadRequest());

        List<ATM> aTMList = aTMRepository.findAll();
        assertThat(aTMList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSaldodisponibleIsRequired() throws Exception {
        int databaseSizeBeforeTest = aTMRepository.findAll().size();
        // set the field null
        aTM.setSaldodisponible(null);

        // Create the ATM, which fails.
        ATMDTO aTMDTO = aTMMapper.toDto(aTM);

        restATMMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aTMDTO)))
            .andExpect(status().isBadRequest());

        List<ATM> aTMList = aTMRepository.findAll();
        assertThat(aTMList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUbicacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = aTMRepository.findAll().size();
        // set the field null
        aTM.setUbicacion(null);

        // Create the ATM, which fails.
        ATMDTO aTMDTO = aTMMapper.toDto(aTM);

        restATMMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aTMDTO)))
            .andExpect(status().isBadRequest());

        List<ATM> aTMList = aTMRepository.findAll();
        assertThat(aTMList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllATMS() throws Exception {
        // Initialize the database
        aTMRepository.saveAndFlush(aTM);

        // Get all the aTMList
        restATMMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aTM.getId().intValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].saldodisponible").value(hasItem(DEFAULT_SALDODISPONIBLE.doubleValue())))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION)));
    }

    @Test
    @Transactional
    void getATM() throws Exception {
        // Initialize the database
        aTMRepository.saveAndFlush(aTM);

        // Get the aTM
        restATMMockMvc
            .perform(get(ENTITY_API_URL_ID, aTM.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aTM.getId().intValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.saldodisponible").value(DEFAULT_SALDODISPONIBLE.doubleValue()))
            .andExpect(jsonPath("$.ubicacion").value(DEFAULT_UBICACION));
    }

    @Test
    @Transactional
    void getNonExistingATM() throws Exception {
        // Get the aTM
        restATMMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingATM() throws Exception {
        // Initialize the database
        aTMRepository.saveAndFlush(aTM);

        int databaseSizeBeforeUpdate = aTMRepository.findAll().size();

        // Update the aTM
        ATM updatedATM = aTMRepository.findById(aTM.getId()).get();
        // Disconnect from session so that the updates on updatedATM are not directly saved in db
        em.detach(updatedATM);
        updatedATM.estado(UPDATED_ESTADO).tipo(UPDATED_TIPO).saldodisponible(UPDATED_SALDODISPONIBLE).ubicacion(UPDATED_UBICACION);
        ATMDTO aTMDTO = aTMMapper.toDto(updatedATM);

        restATMMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aTMDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aTMDTO))
            )
            .andExpect(status().isOk());

        // Validate the ATM in the database
        List<ATM> aTMList = aTMRepository.findAll();
        assertThat(aTMList).hasSize(databaseSizeBeforeUpdate);
        ATM testATM = aTMList.get(aTMList.size() - 1);
        assertThat(testATM.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testATM.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testATM.getSaldodisponible()).isEqualTo(UPDATED_SALDODISPONIBLE);
        assertThat(testATM.getUbicacion()).isEqualTo(UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void putNonExistingATM() throws Exception {
        int databaseSizeBeforeUpdate = aTMRepository.findAll().size();
        aTM.setId(count.incrementAndGet());

        // Create the ATM
        ATMDTO aTMDTO = aTMMapper.toDto(aTM);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restATMMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aTMDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aTMDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ATM in the database
        List<ATM> aTMList = aTMRepository.findAll();
        assertThat(aTMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchATM() throws Exception {
        int databaseSizeBeforeUpdate = aTMRepository.findAll().size();
        aTM.setId(count.incrementAndGet());

        // Create the ATM
        ATMDTO aTMDTO = aTMMapper.toDto(aTM);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restATMMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aTMDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ATM in the database
        List<ATM> aTMList = aTMRepository.findAll();
        assertThat(aTMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamATM() throws Exception {
        int databaseSizeBeforeUpdate = aTMRepository.findAll().size();
        aTM.setId(count.incrementAndGet());

        // Create the ATM
        ATMDTO aTMDTO = aTMMapper.toDto(aTM);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restATMMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aTMDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ATM in the database
        List<ATM> aTMList = aTMRepository.findAll();
        assertThat(aTMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateATMWithPatch() throws Exception {
        // Initialize the database
        aTMRepository.saveAndFlush(aTM);

        int databaseSizeBeforeUpdate = aTMRepository.findAll().size();

        // Update the aTM using partial update
        ATM partialUpdatedATM = new ATM();
        partialUpdatedATM.setId(aTM.getId());

        partialUpdatedATM.estado(UPDATED_ESTADO).tipo(UPDATED_TIPO).saldodisponible(UPDATED_SALDODISPONIBLE);

        restATMMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedATM.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedATM))
            )
            .andExpect(status().isOk());

        // Validate the ATM in the database
        List<ATM> aTMList = aTMRepository.findAll();
        assertThat(aTMList).hasSize(databaseSizeBeforeUpdate);
        ATM testATM = aTMList.get(aTMList.size() - 1);
        assertThat(testATM.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testATM.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testATM.getSaldodisponible()).isEqualTo(UPDATED_SALDODISPONIBLE);
        assertThat(testATM.getUbicacion()).isEqualTo(DEFAULT_UBICACION);
    }

    @Test
    @Transactional
    void fullUpdateATMWithPatch() throws Exception {
        // Initialize the database
        aTMRepository.saveAndFlush(aTM);

        int databaseSizeBeforeUpdate = aTMRepository.findAll().size();

        // Update the aTM using partial update
        ATM partialUpdatedATM = new ATM();
        partialUpdatedATM.setId(aTM.getId());

        partialUpdatedATM.estado(UPDATED_ESTADO).tipo(UPDATED_TIPO).saldodisponible(UPDATED_SALDODISPONIBLE).ubicacion(UPDATED_UBICACION);

        restATMMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedATM.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedATM))
            )
            .andExpect(status().isOk());

        // Validate the ATM in the database
        List<ATM> aTMList = aTMRepository.findAll();
        assertThat(aTMList).hasSize(databaseSizeBeforeUpdate);
        ATM testATM = aTMList.get(aTMList.size() - 1);
        assertThat(testATM.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testATM.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testATM.getSaldodisponible()).isEqualTo(UPDATED_SALDODISPONIBLE);
        assertThat(testATM.getUbicacion()).isEqualTo(UPDATED_UBICACION);
    }

    @Test
    @Transactional
    void patchNonExistingATM() throws Exception {
        int databaseSizeBeforeUpdate = aTMRepository.findAll().size();
        aTM.setId(count.incrementAndGet());

        // Create the ATM
        ATMDTO aTMDTO = aTMMapper.toDto(aTM);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restATMMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aTMDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aTMDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ATM in the database
        List<ATM> aTMList = aTMRepository.findAll();
        assertThat(aTMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchATM() throws Exception {
        int databaseSizeBeforeUpdate = aTMRepository.findAll().size();
        aTM.setId(count.incrementAndGet());

        // Create the ATM
        ATMDTO aTMDTO = aTMMapper.toDto(aTM);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restATMMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aTMDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ATM in the database
        List<ATM> aTMList = aTMRepository.findAll();
        assertThat(aTMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamATM() throws Exception {
        int databaseSizeBeforeUpdate = aTMRepository.findAll().size();
        aTM.setId(count.incrementAndGet());

        // Create the ATM
        ATMDTO aTMDTO = aTMMapper.toDto(aTM);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restATMMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aTMDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ATM in the database
        List<ATM> aTMList = aTMRepository.findAll();
        assertThat(aTMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteATM() throws Exception {
        // Initialize the database
        aTMRepository.saveAndFlush(aTM);

        int databaseSizeBeforeDelete = aTMRepository.findAll().size();

        // Delete the aTM
        restATMMockMvc.perform(delete(ENTITY_API_URL_ID, aTM.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ATM> aTMList = aTMRepository.findAll();
        assertThat(aTMList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
