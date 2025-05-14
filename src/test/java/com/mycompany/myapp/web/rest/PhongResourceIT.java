package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.PhongAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Phong;
import com.mycompany.myapp.repository.PhongRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PhongResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PhongResourceIT {

    private static final String DEFAULT_TEN_PHONG = "AAAAAAAAAA";
    private static final String UPDATED_TEN_PHONG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/phongs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PhongRepository phongRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhongMockMvc;

    private Phong phong;

    private Phong insertedPhong;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phong createEntity(EntityManager em) {
        Phong phong = new Phong().tenPhong(DEFAULT_TEN_PHONG);
        return phong;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phong createUpdatedEntity(EntityManager em) {
        Phong phong = new Phong().tenPhong(UPDATED_TEN_PHONG);
        return phong;
    }

    @BeforeEach
    public void initTest() {
        phong = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPhong != null) {
            phongRepository.delete(insertedPhong);
            insertedPhong = null;
        }
    }

    @Test
    @Transactional
    void createPhong() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Phong
        var returnedPhong = om.readValue(
            restPhongMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phong)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Phong.class
        );

        // Validate the Phong in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPhongUpdatableFieldsEquals(returnedPhong, getPersistedPhong(returnedPhong));

        insertedPhong = returnedPhong;
    }

    @Test
    @Transactional
    void createPhongWithExistingId() throws Exception {
        // Create the Phong with an existing ID
        phong.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phong)))
            .andExpect(status().isBadRequest());

        // Validate the Phong in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTenPhongIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        phong.setTenPhong(null);

        // Create the Phong, which fails.

        restPhongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phong)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPhongs() throws Exception {
        // Initialize the database
        insertedPhong = phongRepository.saveAndFlush(phong);

        // Get all the phongList
        restPhongMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phong.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenPhong").value(hasItem(DEFAULT_TEN_PHONG)));
    }

    @Test
    @Transactional
    void getPhong() throws Exception {
        // Initialize the database
        insertedPhong = phongRepository.saveAndFlush(phong);

        // Get the phong
        restPhongMockMvc
            .perform(get(ENTITY_API_URL_ID, phong.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(phong.getId().intValue()))
            .andExpect(jsonPath("$.tenPhong").value(DEFAULT_TEN_PHONG));
    }

    @Test
    @Transactional
    void getNonExistingPhong() throws Exception {
        // Get the phong
        restPhongMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPhong() throws Exception {
        // Initialize the database
        insertedPhong = phongRepository.saveAndFlush(phong);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the phong
        Phong updatedPhong = phongRepository.findById(phong.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPhong are not directly saved in db
        em.detach(updatedPhong);
        updatedPhong.tenPhong(UPDATED_TEN_PHONG);

        restPhongMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPhong.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPhong))
            )
            .andExpect(status().isOk());

        // Validate the Phong in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPhongToMatchAllProperties(updatedPhong);
    }

    @Test
    @Transactional
    void putNonExistingPhong() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phong.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhongMockMvc
            .perform(put(ENTITY_API_URL_ID, phong.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phong)))
            .andExpect(status().isBadRequest());

        // Validate the Phong in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPhong() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phong.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhongMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(phong))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phong in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPhong() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phong.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhongMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phong)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Phong in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePhongWithPatch() throws Exception {
        // Initialize the database
        insertedPhong = phongRepository.saveAndFlush(phong);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the phong using partial update
        Phong partialUpdatedPhong = new Phong();
        partialUpdatedPhong.setId(phong.getId());

        restPhongMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhong.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPhong))
            )
            .andExpect(status().isOk());

        // Validate the Phong in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPhongUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPhong, phong), getPersistedPhong(phong));
    }

    @Test
    @Transactional
    void fullUpdatePhongWithPatch() throws Exception {
        // Initialize the database
        insertedPhong = phongRepository.saveAndFlush(phong);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the phong using partial update
        Phong partialUpdatedPhong = new Phong();
        partialUpdatedPhong.setId(phong.getId());

        partialUpdatedPhong.tenPhong(UPDATED_TEN_PHONG);

        restPhongMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhong.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPhong))
            )
            .andExpect(status().isOk());

        // Validate the Phong in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPhongUpdatableFieldsEquals(partialUpdatedPhong, getPersistedPhong(partialUpdatedPhong));
    }

    @Test
    @Transactional
    void patchNonExistingPhong() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phong.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhongMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, phong.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(phong))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phong in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPhong() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phong.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhongMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(phong))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phong in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPhong() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phong.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhongMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(phong)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Phong in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePhong() throws Exception {
        // Initialize the database
        insertedPhong = phongRepository.saveAndFlush(phong);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the phong
        restPhongMockMvc
            .perform(delete(ENTITY_API_URL_ID, phong.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return phongRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Phong getPersistedPhong(Phong phong) {
        return phongRepository.findById(phong.getId()).orElseThrow();
    }

    protected void assertPersistedPhongToMatchAllProperties(Phong expectedPhong) {
        assertPhongAllPropertiesEquals(expectedPhong, getPersistedPhong(expectedPhong));
    }

    protected void assertPersistedPhongToMatchUpdatableProperties(Phong expectedPhong) {
        assertPhongAllUpdatablePropertiesEquals(expectedPhong, getPersistedPhong(expectedPhong));
    }
}
