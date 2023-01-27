package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Representative;
import com.mycompany.myapp.repository.RepresentativeRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link RepresentativeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RepresentativeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_MAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FACE = "AAAAAAAAAA";
    private static final String UPDATED_FACE = "BBBBBBBBBB";

    private static final String DEFAULT_FACE_ID = "AAAAAAAAAA";
    private static final String UPDATED_FACE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_HASH = "AAAAAAAAAA";
    private static final String UPDATED_HASH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/representatives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RepresentativeRepository representativeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRepresentativeMockMvc;

    private Representative representative;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Representative createEntity(EntityManager em) {
        Representative representative = new Representative()
            .name(DEFAULT_NAME)
            .mail(DEFAULT_MAIL)
            .face(DEFAULT_FACE)
            .faceId(DEFAULT_FACE_ID)
            .hash(DEFAULT_HASH);
        return representative;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Representative createUpdatedEntity(EntityManager em) {
        Representative representative = new Representative()
            .name(UPDATED_NAME)
            .mail(UPDATED_MAIL)
            .face(UPDATED_FACE)
            .faceId(UPDATED_FACE_ID)
            .hash(UPDATED_HASH);
        return representative;
    }

    @BeforeEach
    public void initTest() {
        representative = createEntity(em);
    }

    @Test
    @Transactional
    void createRepresentative() throws Exception {
        int databaseSizeBeforeCreate = representativeRepository.findAll().size();
        // Create the Representative
        restRepresentativeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(representative))
            )
            .andExpect(status().isCreated());

        // Validate the Representative in the database
        List<Representative> representativeList = representativeRepository.findAll();
        assertThat(representativeList).hasSize(databaseSizeBeforeCreate + 1);
        Representative testRepresentative = representativeList.get(representativeList.size() - 1);
        assertThat(testRepresentative.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRepresentative.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testRepresentative.getFace()).isEqualTo(DEFAULT_FACE);
        assertThat(testRepresentative.getFaceId()).isEqualTo(DEFAULT_FACE_ID);
        assertThat(testRepresentative.getHash()).isEqualTo(DEFAULT_HASH);
    }

    @Test
    @Transactional
    void createRepresentativeWithExistingId() throws Exception {
        // Create the Representative with an existing ID
        representative.setId(1L);

        int databaseSizeBeforeCreate = representativeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRepresentativeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(representative))
            )
            .andExpect(status().isBadRequest());

        // Validate the Representative in the database
        List<Representative> representativeList = representativeRepository.findAll();
        assertThat(representativeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = representativeRepository.findAll().size();
        // set the field null
        representative.setName(null);

        // Create the Representative, which fails.

        restRepresentativeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(representative))
            )
            .andExpect(status().isBadRequest());

        List<Representative> representativeList = representativeRepository.findAll();
        assertThat(representativeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMailIsRequired() throws Exception {
        int databaseSizeBeforeTest = representativeRepository.findAll().size();
        // set the field null
        representative.setMail(null);

        // Create the Representative, which fails.

        restRepresentativeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(representative))
            )
            .andExpect(status().isBadRequest());

        List<Representative> representativeList = representativeRepository.findAll();
        assertThat(representativeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHashIsRequired() throws Exception {
        int databaseSizeBeforeTest = representativeRepository.findAll().size();
        // set the field null
        representative.setHash(null);

        // Create the Representative, which fails.

        restRepresentativeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(representative))
            )
            .andExpect(status().isBadRequest());

        List<Representative> representativeList = representativeRepository.findAll();
        assertThat(representativeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRepresentatives() throws Exception {
        // Initialize the database
        representativeRepository.saveAndFlush(representative);

        // Get all the representativeList
        restRepresentativeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(representative.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].face").value(hasItem(DEFAULT_FACE.toString())))
            .andExpect(jsonPath("$.[*].faceId").value(hasItem(DEFAULT_FACE_ID)))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH)));
    }

    @Test
    @Transactional
    void getRepresentative() throws Exception {
        // Initialize the database
        representativeRepository.saveAndFlush(representative);

        // Get the representative
        restRepresentativeMockMvc
            .perform(get(ENTITY_API_URL_ID, representative.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(representative.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL))
            .andExpect(jsonPath("$.face").value(DEFAULT_FACE.toString()))
            .andExpect(jsonPath("$.faceId").value(DEFAULT_FACE_ID))
            .andExpect(jsonPath("$.hash").value(DEFAULT_HASH));
    }

    @Test
    @Transactional
    void getNonExistingRepresentative() throws Exception {
        // Get the representative
        restRepresentativeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRepresentative() throws Exception {
        // Initialize the database
        representativeRepository.saveAndFlush(representative);

        int databaseSizeBeforeUpdate = representativeRepository.findAll().size();

        // Update the representative
        Representative updatedRepresentative = representativeRepository.findById(representative.getId()).get();
        // Disconnect from session so that the updates on updatedRepresentative are not directly saved in db
        em.detach(updatedRepresentative);
        updatedRepresentative.name(UPDATED_NAME).mail(UPDATED_MAIL).face(UPDATED_FACE).faceId(UPDATED_FACE_ID).hash(UPDATED_HASH);

        restRepresentativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRepresentative.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRepresentative))
            )
            .andExpect(status().isOk());

        // Validate the Representative in the database
        List<Representative> representativeList = representativeRepository.findAll();
        assertThat(representativeList).hasSize(databaseSizeBeforeUpdate);
        Representative testRepresentative = representativeList.get(representativeList.size() - 1);
        assertThat(testRepresentative.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRepresentative.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testRepresentative.getFace()).isEqualTo(UPDATED_FACE);
        assertThat(testRepresentative.getFaceId()).isEqualTo(UPDATED_FACE_ID);
        assertThat(testRepresentative.getHash()).isEqualTo(UPDATED_HASH);
    }

    @Test
    @Transactional
    void putNonExistingRepresentative() throws Exception {
        int databaseSizeBeforeUpdate = representativeRepository.findAll().size();
        representative.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepresentativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, representative.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(representative))
            )
            .andExpect(status().isBadRequest());

        // Validate the Representative in the database
        List<Representative> representativeList = representativeRepository.findAll();
        assertThat(representativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRepresentative() throws Exception {
        int databaseSizeBeforeUpdate = representativeRepository.findAll().size();
        representative.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepresentativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(representative))
            )
            .andExpect(status().isBadRequest());

        // Validate the Representative in the database
        List<Representative> representativeList = representativeRepository.findAll();
        assertThat(representativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRepresentative() throws Exception {
        int databaseSizeBeforeUpdate = representativeRepository.findAll().size();
        representative.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepresentativeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(representative)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Representative in the database
        List<Representative> representativeList = representativeRepository.findAll();
        assertThat(representativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRepresentativeWithPatch() throws Exception {
        // Initialize the database
        representativeRepository.saveAndFlush(representative);

        int databaseSizeBeforeUpdate = representativeRepository.findAll().size();

        // Update the representative using partial update
        Representative partialUpdatedRepresentative = new Representative();
        partialUpdatedRepresentative.setId(representative.getId());

        partialUpdatedRepresentative.name(UPDATED_NAME).mail(UPDATED_MAIL);

        restRepresentativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepresentative.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRepresentative))
            )
            .andExpect(status().isOk());

        // Validate the Representative in the database
        List<Representative> representativeList = representativeRepository.findAll();
        assertThat(representativeList).hasSize(databaseSizeBeforeUpdate);
        Representative testRepresentative = representativeList.get(representativeList.size() - 1);
        assertThat(testRepresentative.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRepresentative.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testRepresentative.getFace()).isEqualTo(DEFAULT_FACE);
        assertThat(testRepresentative.getFaceId()).isEqualTo(DEFAULT_FACE_ID);
        assertThat(testRepresentative.getHash()).isEqualTo(DEFAULT_HASH);
    }

    @Test
    @Transactional
    void fullUpdateRepresentativeWithPatch() throws Exception {
        // Initialize the database
        representativeRepository.saveAndFlush(representative);

        int databaseSizeBeforeUpdate = representativeRepository.findAll().size();

        // Update the representative using partial update
        Representative partialUpdatedRepresentative = new Representative();
        partialUpdatedRepresentative.setId(representative.getId());

        partialUpdatedRepresentative.name(UPDATED_NAME).mail(UPDATED_MAIL).face(UPDATED_FACE).faceId(UPDATED_FACE_ID).hash(UPDATED_HASH);

        restRepresentativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRepresentative.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRepresentative))
            )
            .andExpect(status().isOk());

        // Validate the Representative in the database
        List<Representative> representativeList = representativeRepository.findAll();
        assertThat(representativeList).hasSize(databaseSizeBeforeUpdate);
        Representative testRepresentative = representativeList.get(representativeList.size() - 1);
        assertThat(testRepresentative.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRepresentative.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testRepresentative.getFace()).isEqualTo(UPDATED_FACE);
        assertThat(testRepresentative.getFaceId()).isEqualTo(UPDATED_FACE_ID);
        assertThat(testRepresentative.getHash()).isEqualTo(UPDATED_HASH);
    }

    @Test
    @Transactional
    void patchNonExistingRepresentative() throws Exception {
        int databaseSizeBeforeUpdate = representativeRepository.findAll().size();
        representative.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRepresentativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, representative.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(representative))
            )
            .andExpect(status().isBadRequest());

        // Validate the Representative in the database
        List<Representative> representativeList = representativeRepository.findAll();
        assertThat(representativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRepresentative() throws Exception {
        int databaseSizeBeforeUpdate = representativeRepository.findAll().size();
        representative.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepresentativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(representative))
            )
            .andExpect(status().isBadRequest());

        // Validate the Representative in the database
        List<Representative> representativeList = representativeRepository.findAll();
        assertThat(representativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRepresentative() throws Exception {
        int databaseSizeBeforeUpdate = representativeRepository.findAll().size();
        representative.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRepresentativeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(representative))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Representative in the database
        List<Representative> representativeList = representativeRepository.findAll();
        assertThat(representativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRepresentative() throws Exception {
        // Initialize the database
        representativeRepository.saveAndFlush(representative);

        int databaseSizeBeforeDelete = representativeRepository.findAll().size();

        // Delete the representative
        restRepresentativeMockMvc
            .perform(delete(ENTITY_API_URL_ID, representative.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Representative> representativeList = representativeRepository.findAll();
        assertThat(representativeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
