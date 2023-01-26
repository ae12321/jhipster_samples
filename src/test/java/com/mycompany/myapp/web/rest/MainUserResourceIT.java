package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.MainUser;
import com.mycompany.myapp.repository.MainUserRepository;
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
 * Integration tests for the {@link MainUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MainUserResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/main-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MainUserRepository mainUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMainUserMockMvc;

    private MainUser mainUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MainUser createEntity(EntityManager em) {
        MainUser mainUser = new MainUser().name(DEFAULT_NAME);
        return mainUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MainUser createUpdatedEntity(EntityManager em) {
        MainUser mainUser = new MainUser().name(UPDATED_NAME);
        return mainUser;
    }

    @BeforeEach
    public void initTest() {
        mainUser = createEntity(em);
    }

    @Test
    @Transactional
    void createMainUser() throws Exception {
        int databaseSizeBeforeCreate = mainUserRepository.findAll().size();
        // Create the MainUser
        restMainUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mainUser)))
            .andExpect(status().isCreated());

        // Validate the MainUser in the database
        List<MainUser> mainUserList = mainUserRepository.findAll();
        assertThat(mainUserList).hasSize(databaseSizeBeforeCreate + 1);
        MainUser testMainUser = mainUserList.get(mainUserList.size() - 1);
        assertThat(testMainUser.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createMainUserWithExistingId() throws Exception {
        // Create the MainUser with an existing ID
        mainUser.setId(1L);

        int databaseSizeBeforeCreate = mainUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMainUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mainUser)))
            .andExpect(status().isBadRequest());

        // Validate the MainUser in the database
        List<MainUser> mainUserList = mainUserRepository.findAll();
        assertThat(mainUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMainUsers() throws Exception {
        // Initialize the database
        mainUserRepository.saveAndFlush(mainUser);

        // Get all the mainUserList
        restMainUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mainUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getMainUser() throws Exception {
        // Initialize the database
        mainUserRepository.saveAndFlush(mainUser);

        // Get the mainUser
        restMainUserMockMvc
            .perform(get(ENTITY_API_URL_ID, mainUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mainUser.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingMainUser() throws Exception {
        // Get the mainUser
        restMainUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMainUser() throws Exception {
        // Initialize the database
        mainUserRepository.saveAndFlush(mainUser);

        int databaseSizeBeforeUpdate = mainUserRepository.findAll().size();

        // Update the mainUser
        MainUser updatedMainUser = mainUserRepository.findById(mainUser.getId()).get();
        // Disconnect from session so that the updates on updatedMainUser are not directly saved in db
        em.detach(updatedMainUser);
        updatedMainUser.name(UPDATED_NAME);

        restMainUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMainUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMainUser))
            )
            .andExpect(status().isOk());

        // Validate the MainUser in the database
        List<MainUser> mainUserList = mainUserRepository.findAll();
        assertThat(mainUserList).hasSize(databaseSizeBeforeUpdate);
        MainUser testMainUser = mainUserList.get(mainUserList.size() - 1);
        assertThat(testMainUser.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingMainUser() throws Exception {
        int databaseSizeBeforeUpdate = mainUserRepository.findAll().size();
        mainUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMainUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mainUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mainUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the MainUser in the database
        List<MainUser> mainUserList = mainUserRepository.findAll();
        assertThat(mainUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMainUser() throws Exception {
        int databaseSizeBeforeUpdate = mainUserRepository.findAll().size();
        mainUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMainUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mainUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the MainUser in the database
        List<MainUser> mainUserList = mainUserRepository.findAll();
        assertThat(mainUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMainUser() throws Exception {
        int databaseSizeBeforeUpdate = mainUserRepository.findAll().size();
        mainUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMainUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mainUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MainUser in the database
        List<MainUser> mainUserList = mainUserRepository.findAll();
        assertThat(mainUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMainUserWithPatch() throws Exception {
        // Initialize the database
        mainUserRepository.saveAndFlush(mainUser);

        int databaseSizeBeforeUpdate = mainUserRepository.findAll().size();

        // Update the mainUser using partial update
        MainUser partialUpdatedMainUser = new MainUser();
        partialUpdatedMainUser.setId(mainUser.getId());

        partialUpdatedMainUser.name(UPDATED_NAME);

        restMainUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMainUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMainUser))
            )
            .andExpect(status().isOk());

        // Validate the MainUser in the database
        List<MainUser> mainUserList = mainUserRepository.findAll();
        assertThat(mainUserList).hasSize(databaseSizeBeforeUpdate);
        MainUser testMainUser = mainUserList.get(mainUserList.size() - 1);
        assertThat(testMainUser.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateMainUserWithPatch() throws Exception {
        // Initialize the database
        mainUserRepository.saveAndFlush(mainUser);

        int databaseSizeBeforeUpdate = mainUserRepository.findAll().size();

        // Update the mainUser using partial update
        MainUser partialUpdatedMainUser = new MainUser();
        partialUpdatedMainUser.setId(mainUser.getId());

        partialUpdatedMainUser.name(UPDATED_NAME);

        restMainUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMainUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMainUser))
            )
            .andExpect(status().isOk());

        // Validate the MainUser in the database
        List<MainUser> mainUserList = mainUserRepository.findAll();
        assertThat(mainUserList).hasSize(databaseSizeBeforeUpdate);
        MainUser testMainUser = mainUserList.get(mainUserList.size() - 1);
        assertThat(testMainUser.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingMainUser() throws Exception {
        int databaseSizeBeforeUpdate = mainUserRepository.findAll().size();
        mainUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMainUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mainUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mainUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the MainUser in the database
        List<MainUser> mainUserList = mainUserRepository.findAll();
        assertThat(mainUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMainUser() throws Exception {
        int databaseSizeBeforeUpdate = mainUserRepository.findAll().size();
        mainUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMainUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mainUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the MainUser in the database
        List<MainUser> mainUserList = mainUserRepository.findAll();
        assertThat(mainUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMainUser() throws Exception {
        int databaseSizeBeforeUpdate = mainUserRepository.findAll().size();
        mainUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMainUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mainUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MainUser in the database
        List<MainUser> mainUserList = mainUserRepository.findAll();
        assertThat(mainUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMainUser() throws Exception {
        // Initialize the database
        mainUserRepository.saveAndFlush(mainUser);

        int databaseSizeBeforeDelete = mainUserRepository.findAll().size();

        // Delete the mainUser
        restMainUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, mainUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MainUser> mainUserList = mainUserRepository.findAll();
        assertThat(mainUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
