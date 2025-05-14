package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.RapAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Rap;
import com.mycompany.myapp.repository.RapRepository;
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
 * Integration tests for the {@link RapResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RapResourceIT {

    private static final String DEFAULT_TEN_RAP = "AAAAAAAAAA";
    private static final String UPDATED_TEN_RAP = "BBBBBBBBBB";

    private static final String DEFAULT_DIA_CHI = "AAAAAAAAAA";
    private static final String UPDATED_DIA_CHI = "BBBBBBBBBB";

    private static final String DEFAULT_THANH_PHO = "AAAAAAAAAA";
    private static final String UPDATED_THANH_PHO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/raps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RapRepository rapRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRapMockMvc;

    private Rap rap;

    private Rap insertedRap;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rap createEntity(EntityManager em) {
        Rap rap = new Rap().tenRap(DEFAULT_TEN_RAP).diaChi(DEFAULT_DIA_CHI).thanhPho(DEFAULT_THANH_PHO);
        return rap;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rap createUpdatedEntity(EntityManager em) {
        Rap rap = new Rap().tenRap(UPDATED_TEN_RAP).diaChi(UPDATED_DIA_CHI).thanhPho(UPDATED_THANH_PHO);
        return rap;
    }

    @BeforeEach
    public void initTest() {
        rap = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedRap != null) {
            rapRepository.delete(insertedRap);
            insertedRap = null;
        }
    }

    @Test
    @Transactional
    void createRap() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Rap
        var returnedRap = om.readValue(
            restRapMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rap)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Rap.class
        );

        // Validate the Rap in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRapUpdatableFieldsEquals(returnedRap, getPersistedRap(returnedRap));

        insertedRap = returnedRap;
    }

    @Test
    @Transactional
    void createRapWithExistingId() throws Exception {
        // Create the Rap with an existing ID
        rap.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRapMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rap)))
            .andExpect(status().isBadRequest());

        // Validate the Rap in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTenRapIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rap.setTenRap(null);

        // Create the Rap, which fails.

        restRapMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rap)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRaps() throws Exception {
        // Initialize the database
        insertedRap = rapRepository.saveAndFlush(rap);

        // Get all the rapList
        restRapMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rap.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenRap").value(hasItem(DEFAULT_TEN_RAP)))
            .andExpect(jsonPath("$.[*].diaChi").value(hasItem(DEFAULT_DIA_CHI)))
            .andExpect(jsonPath("$.[*].thanhPho").value(hasItem(DEFAULT_THANH_PHO)));
    }

    @Test
    @Transactional
    void getRap() throws Exception {
        // Initialize the database
        insertedRap = rapRepository.saveAndFlush(rap);

        // Get the rap
        restRapMockMvc
            .perform(get(ENTITY_API_URL_ID, rap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rap.getId().intValue()))
            .andExpect(jsonPath("$.tenRap").value(DEFAULT_TEN_RAP))
            .andExpect(jsonPath("$.diaChi").value(DEFAULT_DIA_CHI))
            .andExpect(jsonPath("$.thanhPho").value(DEFAULT_THANH_PHO));
    }

    @Test
    @Transactional
    void getNonExistingRap() throws Exception {
        // Get the rap
        restRapMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRap() throws Exception {
        // Initialize the database
        insertedRap = rapRepository.saveAndFlush(rap);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rap
        Rap updatedRap = rapRepository.findById(rap.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRap are not directly saved in db
        em.detach(updatedRap);
        updatedRap.tenRap(UPDATED_TEN_RAP).diaChi(UPDATED_DIA_CHI).thanhPho(UPDATED_THANH_PHO);

        restRapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRap.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedRap))
            )
            .andExpect(status().isOk());

        // Validate the Rap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRapToMatchAllProperties(updatedRap);
    }

    @Test
    @Transactional
    void putNonExistingRap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rap.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRapMockMvc
            .perform(put(ENTITY_API_URL_ID, rap.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rap)))
            .andExpect(status().isBadRequest());

        // Validate the Rap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rap.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rap))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rap.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rap)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRapWithPatch() throws Exception {
        // Initialize the database
        insertedRap = rapRepository.saveAndFlush(rap);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rap using partial update
        Rap partialUpdatedRap = new Rap();
        partialUpdatedRap.setId(rap.getId());

        partialUpdatedRap.thanhPho(UPDATED_THANH_PHO);

        restRapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRap.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRap))
            )
            .andExpect(status().isOk());

        // Validate the Rap in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRapUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRap, rap), getPersistedRap(rap));
    }

    @Test
    @Transactional
    void fullUpdateRapWithPatch() throws Exception {
        // Initialize the database
        insertedRap = rapRepository.saveAndFlush(rap);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rap using partial update
        Rap partialUpdatedRap = new Rap();
        partialUpdatedRap.setId(rap.getId());

        partialUpdatedRap.tenRap(UPDATED_TEN_RAP).diaChi(UPDATED_DIA_CHI).thanhPho(UPDATED_THANH_PHO);

        restRapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRap.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRap))
            )
            .andExpect(status().isOk());

        // Validate the Rap in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRapUpdatableFieldsEquals(partialUpdatedRap, getPersistedRap(partialUpdatedRap));
    }

    @Test
    @Transactional
    void patchNonExistingRap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rap.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRapMockMvc
            .perform(patch(ENTITY_API_URL_ID, rap.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rap)))
            .andExpect(status().isBadRequest());

        // Validate the Rap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rap.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rap))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rap.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rap)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRap() throws Exception {
        // Initialize the database
        insertedRap = rapRepository.saveAndFlush(rap);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rap
        restRapMockMvc.perform(delete(ENTITY_API_URL_ID, rap.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rapRepository.count();
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

    protected Rap getPersistedRap(Rap rap) {
        return rapRepository.findById(rap.getId()).orElseThrow();
    }

    protected void assertPersistedRapToMatchAllProperties(Rap expectedRap) {
        assertRapAllPropertiesEquals(expectedRap, getPersistedRap(expectedRap));
    }

    protected void assertPersistedRapToMatchUpdatableProperties(Rap expectedRap) {
        assertRapAllUpdatablePropertiesEquals(expectedRap, getPersistedRap(expectedRap));
    }
}
