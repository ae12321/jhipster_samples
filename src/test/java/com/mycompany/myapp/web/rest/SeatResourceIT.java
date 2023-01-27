package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Seat;
import com.mycompany.myapp.domain.SeatGroup;
import com.mycompany.myapp.repository.SeatRepository;
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
 * Integration tests for the {@link SeatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SeatResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CAN_SIT = false;
    private static final Boolean UPDATED_CAN_SIT = true;

    private static final String DEFAULT_TOP = "AAAAAAAAAA";
    private static final String UPDATED_TOP = "BBBBBBBBBB";

    private static final String DEFAULT_LEFT = "AAAAAAAAAA";
    private static final String UPDATED_LEFT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/seats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeatMockMvc;

    private Seat seat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seat createEntity(EntityManager em) {
        Seat seat = new Seat().name(DEFAULT_NAME).canSit(DEFAULT_CAN_SIT).top(DEFAULT_TOP).left(DEFAULT_LEFT);
        // Add required entity
        SeatGroup seatGroup;
        if (TestUtil.findAll(em, SeatGroup.class).isEmpty()) {
            seatGroup = SeatGroupResourceIT.createEntity(em);
            em.persist(seatGroup);
            em.flush();
        } else {
            seatGroup = TestUtil.findAll(em, SeatGroup.class).get(0);
        }
        seat.setSeatGroup(seatGroup);
        return seat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seat createUpdatedEntity(EntityManager em) {
        Seat seat = new Seat().name(UPDATED_NAME).canSit(UPDATED_CAN_SIT).top(UPDATED_TOP).left(UPDATED_LEFT);
        // Add required entity
        SeatGroup seatGroup;
        if (TestUtil.findAll(em, SeatGroup.class).isEmpty()) {
            seatGroup = SeatGroupResourceIT.createUpdatedEntity(em);
            em.persist(seatGroup);
            em.flush();
        } else {
            seatGroup = TestUtil.findAll(em, SeatGroup.class).get(0);
        }
        seat.setSeatGroup(seatGroup);
        return seat;
    }

    @BeforeEach
    public void initTest() {
        seat = createEntity(em);
    }

    @Test
    @Transactional
    void createSeat() throws Exception {
        int databaseSizeBeforeCreate = seatRepository.findAll().size();
        // Create the Seat
        restSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seat)))
            .andExpect(status().isCreated());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeCreate + 1);
        Seat testSeat = seatList.get(seatList.size() - 1);
        assertThat(testSeat.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSeat.getCanSit()).isEqualTo(DEFAULT_CAN_SIT);
        assertThat(testSeat.getTop()).isEqualTo(DEFAULT_TOP);
        assertThat(testSeat.getLeft()).isEqualTo(DEFAULT_LEFT);
    }

    @Test
    @Transactional
    void createSeatWithExistingId() throws Exception {
        // Create the Seat with an existing ID
        seat.setId(1L);

        int databaseSizeBeforeCreate = seatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seat)))
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = seatRepository.findAll().size();
        // set the field null
        seat.setName(null);

        // Create the Seat, which fails.

        restSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seat)))
            .andExpect(status().isBadRequest());

        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCanSitIsRequired() throws Exception {
        int databaseSizeBeforeTest = seatRepository.findAll().size();
        // set the field null
        seat.setCanSit(null);

        // Create the Seat, which fails.

        restSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seat)))
            .andExpect(status().isBadRequest());

        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTopIsRequired() throws Exception {
        int databaseSizeBeforeTest = seatRepository.findAll().size();
        // set the field null
        seat.setTop(null);

        // Create the Seat, which fails.

        restSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seat)))
            .andExpect(status().isBadRequest());

        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLeftIsRequired() throws Exception {
        int databaseSizeBeforeTest = seatRepository.findAll().size();
        // set the field null
        seat.setLeft(null);

        // Create the Seat, which fails.

        restSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seat)))
            .andExpect(status().isBadRequest());

        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSeats() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList
        restSeatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seat.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].canSit").value(hasItem(DEFAULT_CAN_SIT.booleanValue())))
            .andExpect(jsonPath("$.[*].top").value(hasItem(DEFAULT_TOP)))
            .andExpect(jsonPath("$.[*].left").value(hasItem(DEFAULT_LEFT)));
    }

    @Test
    @Transactional
    void getSeat() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get the seat
        restSeatMockMvc
            .perform(get(ENTITY_API_URL_ID, seat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(seat.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.canSit").value(DEFAULT_CAN_SIT.booleanValue()))
            .andExpect(jsonPath("$.top").value(DEFAULT_TOP))
            .andExpect(jsonPath("$.left").value(DEFAULT_LEFT));
    }

    @Test
    @Transactional
    void getNonExistingSeat() throws Exception {
        // Get the seat
        restSeatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSeat() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        int databaseSizeBeforeUpdate = seatRepository.findAll().size();

        // Update the seat
        Seat updatedSeat = seatRepository.findById(seat.getId()).get();
        // Disconnect from session so that the updates on updatedSeat are not directly saved in db
        em.detach(updatedSeat);
        updatedSeat.name(UPDATED_NAME).canSit(UPDATED_CAN_SIT).top(UPDATED_TOP).left(UPDATED_LEFT);

        restSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSeat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSeat))
            )
            .andExpect(status().isOk());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
        Seat testSeat = seatList.get(seatList.size() - 1);
        assertThat(testSeat.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSeat.getCanSit()).isEqualTo(UPDATED_CAN_SIT);
        assertThat(testSeat.getTop()).isEqualTo(UPDATED_TOP);
        assertThat(testSeat.getLeft()).isEqualTo(UPDATED_LEFT);
    }

    @Test
    @Transactional
    void putNonExistingSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeatWithPatch() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        int databaseSizeBeforeUpdate = seatRepository.findAll().size();

        // Update the seat using partial update
        Seat partialUpdatedSeat = new Seat();
        partialUpdatedSeat.setId(seat.getId());

        partialUpdatedSeat.name(UPDATED_NAME).canSit(UPDATED_CAN_SIT).top(UPDATED_TOP).left(UPDATED_LEFT);

        restSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeat))
            )
            .andExpect(status().isOk());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
        Seat testSeat = seatList.get(seatList.size() - 1);
        assertThat(testSeat.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSeat.getCanSit()).isEqualTo(UPDATED_CAN_SIT);
        assertThat(testSeat.getTop()).isEqualTo(UPDATED_TOP);
        assertThat(testSeat.getLeft()).isEqualTo(UPDATED_LEFT);
    }

    @Test
    @Transactional
    void fullUpdateSeatWithPatch() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        int databaseSizeBeforeUpdate = seatRepository.findAll().size();

        // Update the seat using partial update
        Seat partialUpdatedSeat = new Seat();
        partialUpdatedSeat.setId(seat.getId());

        partialUpdatedSeat.name(UPDATED_NAME).canSit(UPDATED_CAN_SIT).top(UPDATED_TOP).left(UPDATED_LEFT);

        restSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeat))
            )
            .andExpect(status().isOk());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
        Seat testSeat = seatList.get(seatList.size() - 1);
        assertThat(testSeat.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSeat.getCanSit()).isEqualTo(UPDATED_CAN_SIT);
        assertThat(testSeat.getTop()).isEqualTo(UPDATED_TOP);
        assertThat(testSeat.getLeft()).isEqualTo(UPDATED_LEFT);
    }

    @Test
    @Transactional
    void patchNonExistingSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(seat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeat() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        int databaseSizeBeforeDelete = seatRepository.findAll().size();

        // Delete the seat
        restSeatMockMvc
            .perform(delete(ENTITY_API_URL_ID, seat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
