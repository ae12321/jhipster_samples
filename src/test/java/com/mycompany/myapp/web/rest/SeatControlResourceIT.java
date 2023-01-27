package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SeatControl;
import com.mycompany.myapp.repository.SeatControlRepository;
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
 * Integration tests for the {@link SeatControlResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SeatControlResourceIT {

    private static final Long DEFAULT_SEAT_ID = 1L;
    private static final Long UPDATED_SEAT_ID = 2L;

    private static final String ENTITY_API_URL = "/api/seat-controls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SeatControlRepository seatControlRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeatControlMockMvc;

    private SeatControl seatControl;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SeatControl createEntity(EntityManager em) {
        SeatControl seatControl = new SeatControl().seatId(DEFAULT_SEAT_ID);
        return seatControl;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SeatControl createUpdatedEntity(EntityManager em) {
        SeatControl seatControl = new SeatControl().seatId(UPDATED_SEAT_ID);
        return seatControl;
    }

    @BeforeEach
    public void initTest() {
        seatControl = createEntity(em);
    }

    @Test
    @Transactional
    void createSeatControl() throws Exception {
        int databaseSizeBeforeCreate = seatControlRepository.findAll().size();
        // Create the SeatControl
        restSeatControlMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seatControl)))
            .andExpect(status().isCreated());

        // Validate the SeatControl in the database
        List<SeatControl> seatControlList = seatControlRepository.findAll();
        assertThat(seatControlList).hasSize(databaseSizeBeforeCreate + 1);
        SeatControl testSeatControl = seatControlList.get(seatControlList.size() - 1);
        assertThat(testSeatControl.getSeatId()).isEqualTo(DEFAULT_SEAT_ID);
    }

    @Test
    @Transactional
    void createSeatControlWithExistingId() throws Exception {
        // Create the SeatControl with an existing ID
        seatControl.setId(1L);

        int databaseSizeBeforeCreate = seatControlRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeatControlMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seatControl)))
            .andExpect(status().isBadRequest());

        // Validate the SeatControl in the database
        List<SeatControl> seatControlList = seatControlRepository.findAll();
        assertThat(seatControlList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSeatIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = seatControlRepository.findAll().size();
        // set the field null
        seatControl.setSeatId(null);

        // Create the SeatControl, which fails.

        restSeatControlMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seatControl)))
            .andExpect(status().isBadRequest());

        List<SeatControl> seatControlList = seatControlRepository.findAll();
        assertThat(seatControlList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSeatControls() throws Exception {
        // Initialize the database
        seatControlRepository.saveAndFlush(seatControl);

        // Get all the seatControlList
        restSeatControlMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seatControl.getId().intValue())))
            .andExpect(jsonPath("$.[*].seatId").value(hasItem(DEFAULT_SEAT_ID.intValue())));
    }

    @Test
    @Transactional
    void getSeatControl() throws Exception {
        // Initialize the database
        seatControlRepository.saveAndFlush(seatControl);

        // Get the seatControl
        restSeatControlMockMvc
            .perform(get(ENTITY_API_URL_ID, seatControl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(seatControl.getId().intValue()))
            .andExpect(jsonPath("$.seatId").value(DEFAULT_SEAT_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSeatControl() throws Exception {
        // Get the seatControl
        restSeatControlMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSeatControl() throws Exception {
        // Initialize the database
        seatControlRepository.saveAndFlush(seatControl);

        int databaseSizeBeforeUpdate = seatControlRepository.findAll().size();

        // Update the seatControl
        SeatControl updatedSeatControl = seatControlRepository.findById(seatControl.getId()).get();
        // Disconnect from session so that the updates on updatedSeatControl are not directly saved in db
        em.detach(updatedSeatControl);
        updatedSeatControl.seatId(UPDATED_SEAT_ID);

        restSeatControlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSeatControl.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSeatControl))
            )
            .andExpect(status().isOk());

        // Validate the SeatControl in the database
        List<SeatControl> seatControlList = seatControlRepository.findAll();
        assertThat(seatControlList).hasSize(databaseSizeBeforeUpdate);
        SeatControl testSeatControl = seatControlList.get(seatControlList.size() - 1);
        assertThat(testSeatControl.getSeatId()).isEqualTo(UPDATED_SEAT_ID);
    }

    @Test
    @Transactional
    void putNonExistingSeatControl() throws Exception {
        int databaseSizeBeforeUpdate = seatControlRepository.findAll().size();
        seatControl.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatControlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seatControl.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatControl))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatControl in the database
        List<SeatControl> seatControlList = seatControlRepository.findAll();
        assertThat(seatControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeatControl() throws Exception {
        int databaseSizeBeforeUpdate = seatControlRepository.findAll().size();
        seatControl.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatControlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatControl))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatControl in the database
        List<SeatControl> seatControlList = seatControlRepository.findAll();
        assertThat(seatControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeatControl() throws Exception {
        int databaseSizeBeforeUpdate = seatControlRepository.findAll().size();
        seatControl.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatControlMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seatControl)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SeatControl in the database
        List<SeatControl> seatControlList = seatControlRepository.findAll();
        assertThat(seatControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeatControlWithPatch() throws Exception {
        // Initialize the database
        seatControlRepository.saveAndFlush(seatControl);

        int databaseSizeBeforeUpdate = seatControlRepository.findAll().size();

        // Update the seatControl using partial update
        SeatControl partialUpdatedSeatControl = new SeatControl();
        partialUpdatedSeatControl.setId(seatControl.getId());

        restSeatControlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeatControl.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeatControl))
            )
            .andExpect(status().isOk());

        // Validate the SeatControl in the database
        List<SeatControl> seatControlList = seatControlRepository.findAll();
        assertThat(seatControlList).hasSize(databaseSizeBeforeUpdate);
        SeatControl testSeatControl = seatControlList.get(seatControlList.size() - 1);
        assertThat(testSeatControl.getSeatId()).isEqualTo(DEFAULT_SEAT_ID);
    }

    @Test
    @Transactional
    void fullUpdateSeatControlWithPatch() throws Exception {
        // Initialize the database
        seatControlRepository.saveAndFlush(seatControl);

        int databaseSizeBeforeUpdate = seatControlRepository.findAll().size();

        // Update the seatControl using partial update
        SeatControl partialUpdatedSeatControl = new SeatControl();
        partialUpdatedSeatControl.setId(seatControl.getId());

        partialUpdatedSeatControl.seatId(UPDATED_SEAT_ID);

        restSeatControlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeatControl.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeatControl))
            )
            .andExpect(status().isOk());

        // Validate the SeatControl in the database
        List<SeatControl> seatControlList = seatControlRepository.findAll();
        assertThat(seatControlList).hasSize(databaseSizeBeforeUpdate);
        SeatControl testSeatControl = seatControlList.get(seatControlList.size() - 1);
        assertThat(testSeatControl.getSeatId()).isEqualTo(UPDATED_SEAT_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSeatControl() throws Exception {
        int databaseSizeBeforeUpdate = seatControlRepository.findAll().size();
        seatControl.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatControlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seatControl.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seatControl))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatControl in the database
        List<SeatControl> seatControlList = seatControlRepository.findAll();
        assertThat(seatControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeatControl() throws Exception {
        int databaseSizeBeforeUpdate = seatControlRepository.findAll().size();
        seatControl.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatControlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seatControl))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatControl in the database
        List<SeatControl> seatControlList = seatControlRepository.findAll();
        assertThat(seatControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeatControl() throws Exception {
        int databaseSizeBeforeUpdate = seatControlRepository.findAll().size();
        seatControl.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatControlMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(seatControl))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SeatControl in the database
        List<SeatControl> seatControlList = seatControlRepository.findAll();
        assertThat(seatControlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeatControl() throws Exception {
        // Initialize the database
        seatControlRepository.saveAndFlush(seatControl);

        int databaseSizeBeforeDelete = seatControlRepository.findAll().size();

        // Delete the seatControl
        restSeatControlMockMvc
            .perform(delete(ENTITY_API_URL_ID, seatControl.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SeatControl> seatControlList = seatControlRepository.findAll();
        assertThat(seatControlList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
