package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SeatGroup;
import com.mycompany.myapp.repository.SeatGroupRepository;
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
 * Integration tests for the {@link SeatGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SeatGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/seat-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SeatGroupRepository seatGroupRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeatGroupMockMvc;

    private SeatGroup seatGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SeatGroup createEntity(EntityManager em) {
        SeatGroup seatGroup = new SeatGroup().name(DEFAULT_NAME);
        return seatGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SeatGroup createUpdatedEntity(EntityManager em) {
        SeatGroup seatGroup = new SeatGroup().name(UPDATED_NAME);
        return seatGroup;
    }

    @BeforeEach
    public void initTest() {
        seatGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createSeatGroup() throws Exception {
        int databaseSizeBeforeCreate = seatGroupRepository.findAll().size();
        // Create the SeatGroup
        restSeatGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seatGroup)))
            .andExpect(status().isCreated());

        // Validate the SeatGroup in the database
        List<SeatGroup> seatGroupList = seatGroupRepository.findAll();
        assertThat(seatGroupList).hasSize(databaseSizeBeforeCreate + 1);
        SeatGroup testSeatGroup = seatGroupList.get(seatGroupList.size() - 1);
        assertThat(testSeatGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSeatGroupWithExistingId() throws Exception {
        // Create the SeatGroup with an existing ID
        seatGroup.setId(1L);

        int databaseSizeBeforeCreate = seatGroupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeatGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seatGroup)))
            .andExpect(status().isBadRequest());

        // Validate the SeatGroup in the database
        List<SeatGroup> seatGroupList = seatGroupRepository.findAll();
        assertThat(seatGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = seatGroupRepository.findAll().size();
        // set the field null
        seatGroup.setName(null);

        // Create the SeatGroup, which fails.

        restSeatGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seatGroup)))
            .andExpect(status().isBadRequest());

        List<SeatGroup> seatGroupList = seatGroupRepository.findAll();
        assertThat(seatGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSeatGroups() throws Exception {
        // Initialize the database
        seatGroupRepository.saveAndFlush(seatGroup);

        // Get all the seatGroupList
        restSeatGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seatGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSeatGroup() throws Exception {
        // Initialize the database
        seatGroupRepository.saveAndFlush(seatGroup);

        // Get the seatGroup
        restSeatGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, seatGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(seatGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSeatGroup() throws Exception {
        // Get the seatGroup
        restSeatGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSeatGroup() throws Exception {
        // Initialize the database
        seatGroupRepository.saveAndFlush(seatGroup);

        int databaseSizeBeforeUpdate = seatGroupRepository.findAll().size();

        // Update the seatGroup
        SeatGroup updatedSeatGroup = seatGroupRepository.findById(seatGroup.getId()).get();
        // Disconnect from session so that the updates on updatedSeatGroup are not directly saved in db
        em.detach(updatedSeatGroup);
        updatedSeatGroup.name(UPDATED_NAME);

        restSeatGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSeatGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSeatGroup))
            )
            .andExpect(status().isOk());

        // Validate the SeatGroup in the database
        List<SeatGroup> seatGroupList = seatGroupRepository.findAll();
        assertThat(seatGroupList).hasSize(databaseSizeBeforeUpdate);
        SeatGroup testSeatGroup = seatGroupList.get(seatGroupList.size() - 1);
        assertThat(testSeatGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSeatGroup() throws Exception {
        int databaseSizeBeforeUpdate = seatGroupRepository.findAll().size();
        seatGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seatGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatGroup in the database
        List<SeatGroup> seatGroupList = seatGroupRepository.findAll();
        assertThat(seatGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeatGroup() throws Exception {
        int databaseSizeBeforeUpdate = seatGroupRepository.findAll().size();
        seatGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatGroup in the database
        List<SeatGroup> seatGroupList = seatGroupRepository.findAll();
        assertThat(seatGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeatGroup() throws Exception {
        int databaseSizeBeforeUpdate = seatGroupRepository.findAll().size();
        seatGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seatGroup)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SeatGroup in the database
        List<SeatGroup> seatGroupList = seatGroupRepository.findAll();
        assertThat(seatGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeatGroupWithPatch() throws Exception {
        // Initialize the database
        seatGroupRepository.saveAndFlush(seatGroup);

        int databaseSizeBeforeUpdate = seatGroupRepository.findAll().size();

        // Update the seatGroup using partial update
        SeatGroup partialUpdatedSeatGroup = new SeatGroup();
        partialUpdatedSeatGroup.setId(seatGroup.getId());

        partialUpdatedSeatGroup.name(UPDATED_NAME);

        restSeatGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeatGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeatGroup))
            )
            .andExpect(status().isOk());

        // Validate the SeatGroup in the database
        List<SeatGroup> seatGroupList = seatGroupRepository.findAll();
        assertThat(seatGroupList).hasSize(databaseSizeBeforeUpdate);
        SeatGroup testSeatGroup = seatGroupList.get(seatGroupList.size() - 1);
        assertThat(testSeatGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSeatGroupWithPatch() throws Exception {
        // Initialize the database
        seatGroupRepository.saveAndFlush(seatGroup);

        int databaseSizeBeforeUpdate = seatGroupRepository.findAll().size();

        // Update the seatGroup using partial update
        SeatGroup partialUpdatedSeatGroup = new SeatGroup();
        partialUpdatedSeatGroup.setId(seatGroup.getId());

        partialUpdatedSeatGroup.name(UPDATED_NAME);

        restSeatGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeatGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeatGroup))
            )
            .andExpect(status().isOk());

        // Validate the SeatGroup in the database
        List<SeatGroup> seatGroupList = seatGroupRepository.findAll();
        assertThat(seatGroupList).hasSize(databaseSizeBeforeUpdate);
        SeatGroup testSeatGroup = seatGroupList.get(seatGroupList.size() - 1);
        assertThat(testSeatGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSeatGroup() throws Exception {
        int databaseSizeBeforeUpdate = seatGroupRepository.findAll().size();
        seatGroup.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seatGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seatGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatGroup in the database
        List<SeatGroup> seatGroupList = seatGroupRepository.findAll();
        assertThat(seatGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeatGroup() throws Exception {
        int databaseSizeBeforeUpdate = seatGroupRepository.findAll().size();
        seatGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seatGroup))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatGroup in the database
        List<SeatGroup> seatGroupList = seatGroupRepository.findAll();
        assertThat(seatGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeatGroup() throws Exception {
        int databaseSizeBeforeUpdate = seatGroupRepository.findAll().size();
        seatGroup.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(seatGroup))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SeatGroup in the database
        List<SeatGroup> seatGroupList = seatGroupRepository.findAll();
        assertThat(seatGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeatGroup() throws Exception {
        // Initialize the database
        seatGroupRepository.saveAndFlush(seatGroup);

        int databaseSizeBeforeDelete = seatGroupRepository.findAll().size();

        // Delete the seatGroup
        restSeatGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, seatGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SeatGroup> seatGroupList = seatGroupRepository.findAll();
        assertThat(seatGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
