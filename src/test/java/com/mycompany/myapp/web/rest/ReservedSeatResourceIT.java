package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Representative;
import com.mycompany.myapp.domain.ReservedSeat;
import com.mycompany.myapp.domain.Seat;
import com.mycompany.myapp.repository.ReservedSeatRepository;
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
 * Integration tests for the {@link ReservedSeatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReservedSeatResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reserved-seats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReservedSeatRepository reservedSeatRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReservedSeatMockMvc;

    private ReservedSeat reservedSeat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReservedSeat createEntity(EntityManager em) {
        ReservedSeat reservedSeat = new ReservedSeat().name(DEFAULT_NAME);
        // Add required entity
        Representative representative;
        if (TestUtil.findAll(em, Representative.class).isEmpty()) {
            representative = RepresentativeResourceIT.createEntity(em);
            em.persist(representative);
            em.flush();
        } else {
            representative = TestUtil.findAll(em, Representative.class).get(0);
        }
        reservedSeat.setRepresentative(representative);
        // Add required entity
        Seat seat;
        if (TestUtil.findAll(em, Seat.class).isEmpty()) {
            seat = SeatResourceIT.createEntity(em);
            em.persist(seat);
            em.flush();
        } else {
            seat = TestUtil.findAll(em, Seat.class).get(0);
        }
        reservedSeat.setSeat(seat);
        return reservedSeat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReservedSeat createUpdatedEntity(EntityManager em) {
        ReservedSeat reservedSeat = new ReservedSeat().name(UPDATED_NAME);
        // Add required entity
        Representative representative;
        if (TestUtil.findAll(em, Representative.class).isEmpty()) {
            representative = RepresentativeResourceIT.createUpdatedEntity(em);
            em.persist(representative);
            em.flush();
        } else {
            representative = TestUtil.findAll(em, Representative.class).get(0);
        }
        reservedSeat.setRepresentative(representative);
        // Add required entity
        Seat seat;
        if (TestUtil.findAll(em, Seat.class).isEmpty()) {
            seat = SeatResourceIT.createUpdatedEntity(em);
            em.persist(seat);
            em.flush();
        } else {
            seat = TestUtil.findAll(em, Seat.class).get(0);
        }
        reservedSeat.setSeat(seat);
        return reservedSeat;
    }

    @BeforeEach
    public void initTest() {
        reservedSeat = createEntity(em);
    }

    @Test
    @Transactional
    void createReservedSeat() throws Exception {
        int databaseSizeBeforeCreate = reservedSeatRepository.findAll().size();
        // Create the ReservedSeat
        restReservedSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservedSeat)))
            .andExpect(status().isCreated());

        // Validate the ReservedSeat in the database
        List<ReservedSeat> reservedSeatList = reservedSeatRepository.findAll();
        assertThat(reservedSeatList).hasSize(databaseSizeBeforeCreate + 1);
        ReservedSeat testReservedSeat = reservedSeatList.get(reservedSeatList.size() - 1);
        assertThat(testReservedSeat.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createReservedSeatWithExistingId() throws Exception {
        // Create the ReservedSeat with an existing ID
        reservedSeat.setId(1L);

        int databaseSizeBeforeCreate = reservedSeatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservedSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservedSeat)))
            .andExpect(status().isBadRequest());

        // Validate the ReservedSeat in the database
        List<ReservedSeat> reservedSeatList = reservedSeatRepository.findAll();
        assertThat(reservedSeatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservedSeatRepository.findAll().size();
        // set the field null
        reservedSeat.setName(null);

        // Create the ReservedSeat, which fails.

        restReservedSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservedSeat)))
            .andExpect(status().isBadRequest());

        List<ReservedSeat> reservedSeatList = reservedSeatRepository.findAll();
        assertThat(reservedSeatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReservedSeats() throws Exception {
        // Initialize the database
        reservedSeatRepository.saveAndFlush(reservedSeat);

        // Get all the reservedSeatList
        restReservedSeatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservedSeat.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getReservedSeat() throws Exception {
        // Initialize the database
        reservedSeatRepository.saveAndFlush(reservedSeat);

        // Get the reservedSeat
        restReservedSeatMockMvc
            .perform(get(ENTITY_API_URL_ID, reservedSeat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reservedSeat.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingReservedSeat() throws Exception {
        // Get the reservedSeat
        restReservedSeatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReservedSeat() throws Exception {
        // Initialize the database
        reservedSeatRepository.saveAndFlush(reservedSeat);

        int databaseSizeBeforeUpdate = reservedSeatRepository.findAll().size();

        // Update the reservedSeat
        ReservedSeat updatedReservedSeat = reservedSeatRepository.findById(reservedSeat.getId()).get();
        // Disconnect from session so that the updates on updatedReservedSeat are not directly saved in db
        em.detach(updatedReservedSeat);
        updatedReservedSeat.name(UPDATED_NAME);

        restReservedSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReservedSeat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReservedSeat))
            )
            .andExpect(status().isOk());

        // Validate the ReservedSeat in the database
        List<ReservedSeat> reservedSeatList = reservedSeatRepository.findAll();
        assertThat(reservedSeatList).hasSize(databaseSizeBeforeUpdate);
        ReservedSeat testReservedSeat = reservedSeatList.get(reservedSeatList.size() - 1);
        assertThat(testReservedSeat.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingReservedSeat() throws Exception {
        int databaseSizeBeforeUpdate = reservedSeatRepository.findAll().size();
        reservedSeat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservedSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reservedSeat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservedSeat))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReservedSeat in the database
        List<ReservedSeat> reservedSeatList = reservedSeatRepository.findAll();
        assertThat(reservedSeatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReservedSeat() throws Exception {
        int databaseSizeBeforeUpdate = reservedSeatRepository.findAll().size();
        reservedSeat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservedSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reservedSeat))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReservedSeat in the database
        List<ReservedSeat> reservedSeatList = reservedSeatRepository.findAll();
        assertThat(reservedSeatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReservedSeat() throws Exception {
        int databaseSizeBeforeUpdate = reservedSeatRepository.findAll().size();
        reservedSeat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservedSeatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reservedSeat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReservedSeat in the database
        List<ReservedSeat> reservedSeatList = reservedSeatRepository.findAll();
        assertThat(reservedSeatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReservedSeatWithPatch() throws Exception {
        // Initialize the database
        reservedSeatRepository.saveAndFlush(reservedSeat);

        int databaseSizeBeforeUpdate = reservedSeatRepository.findAll().size();

        // Update the reservedSeat using partial update
        ReservedSeat partialUpdatedReservedSeat = new ReservedSeat();
        partialUpdatedReservedSeat.setId(reservedSeat.getId());

        partialUpdatedReservedSeat.name(UPDATED_NAME);

        restReservedSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservedSeat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReservedSeat))
            )
            .andExpect(status().isOk());

        // Validate the ReservedSeat in the database
        List<ReservedSeat> reservedSeatList = reservedSeatRepository.findAll();
        assertThat(reservedSeatList).hasSize(databaseSizeBeforeUpdate);
        ReservedSeat testReservedSeat = reservedSeatList.get(reservedSeatList.size() - 1);
        assertThat(testReservedSeat.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateReservedSeatWithPatch() throws Exception {
        // Initialize the database
        reservedSeatRepository.saveAndFlush(reservedSeat);

        int databaseSizeBeforeUpdate = reservedSeatRepository.findAll().size();

        // Update the reservedSeat using partial update
        ReservedSeat partialUpdatedReservedSeat = new ReservedSeat();
        partialUpdatedReservedSeat.setId(reservedSeat.getId());

        partialUpdatedReservedSeat.name(UPDATED_NAME);

        restReservedSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReservedSeat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReservedSeat))
            )
            .andExpect(status().isOk());

        // Validate the ReservedSeat in the database
        List<ReservedSeat> reservedSeatList = reservedSeatRepository.findAll();
        assertThat(reservedSeatList).hasSize(databaseSizeBeforeUpdate);
        ReservedSeat testReservedSeat = reservedSeatList.get(reservedSeatList.size() - 1);
        assertThat(testReservedSeat.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingReservedSeat() throws Exception {
        int databaseSizeBeforeUpdate = reservedSeatRepository.findAll().size();
        reservedSeat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservedSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reservedSeat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reservedSeat))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReservedSeat in the database
        List<ReservedSeat> reservedSeatList = reservedSeatRepository.findAll();
        assertThat(reservedSeatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReservedSeat() throws Exception {
        int databaseSizeBeforeUpdate = reservedSeatRepository.findAll().size();
        reservedSeat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservedSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reservedSeat))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReservedSeat in the database
        List<ReservedSeat> reservedSeatList = reservedSeatRepository.findAll();
        assertThat(reservedSeatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReservedSeat() throws Exception {
        int databaseSizeBeforeUpdate = reservedSeatRepository.findAll().size();
        reservedSeat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReservedSeatMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reservedSeat))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReservedSeat in the database
        List<ReservedSeat> reservedSeatList = reservedSeatRepository.findAll();
        assertThat(reservedSeatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReservedSeat() throws Exception {
        // Initialize the database
        reservedSeatRepository.saveAndFlush(reservedSeat);

        int databaseSizeBeforeDelete = reservedSeatRepository.findAll().size();

        // Delete the reservedSeat
        restReservedSeatMockMvc
            .perform(delete(ENTITY_API_URL_ID, reservedSeat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReservedSeat> reservedSeatList = reservedSeatRepository.findAll();
        assertThat(reservedSeatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
