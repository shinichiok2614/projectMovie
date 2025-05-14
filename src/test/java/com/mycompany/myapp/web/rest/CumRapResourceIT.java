package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.CumRapAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CumRap;
import com.mycompany.myapp.repository.CumRapRepository;
import jakarta.persistence.EntityManager;
import java.util.Base64;
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
 * Integration tests for the {@link CumRapResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CumRapResourceIT {

    private static final String DEFAULT_TEN_CUM_RAP = "AAAAAAAAAA";
    private static final String UPDATED_TEN_CUM_RAP = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/cum-raps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CumRapRepository cumRapRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCumRapMockMvc;

    private CumRap cumRap;

    private CumRap insertedCumRap;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CumRap createEntity(EntityManager em) {
        CumRap cumRap = new CumRap().tenCumRap(DEFAULT_TEN_CUM_RAP).logo(DEFAULT_LOGO).logoContentType(DEFAULT_LOGO_CONTENT_TYPE);
        return cumRap;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CumRap createUpdatedEntity(EntityManager em) {
        CumRap cumRap = new CumRap().tenCumRap(UPDATED_TEN_CUM_RAP).logo(UPDATED_LOGO).logoContentType(UPDATED_LOGO_CONTENT_TYPE);
        return cumRap;
    }

    @BeforeEach
    public void initTest() {
        cumRap = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedCumRap != null) {
            cumRapRepository.delete(insertedCumRap);
            insertedCumRap = null;
        }
    }

    @Test
    @Transactional
    void createCumRap() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CumRap
        var returnedCumRap = om.readValue(
            restCumRapMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cumRap)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CumRap.class
        );

        // Validate the CumRap in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCumRapUpdatableFieldsEquals(returnedCumRap, getPersistedCumRap(returnedCumRap));

        insertedCumRap = returnedCumRap;
    }

    @Test
    @Transactional
    void createCumRapWithExistingId() throws Exception {
        // Create the CumRap with an existing ID
        cumRap.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCumRapMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cumRap)))
            .andExpect(status().isBadRequest());

        // Validate the CumRap in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTenCumRapIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cumRap.setTenCumRap(null);

        // Create the CumRap, which fails.

        restCumRapMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cumRap)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCumRaps() throws Exception {
        // Initialize the database
        insertedCumRap = cumRapRepository.saveAndFlush(cumRap);

        // Get all the cumRapList
        restCumRapMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cumRap.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenCumRap").value(hasItem(DEFAULT_TEN_CUM_RAP)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_LOGO))));
    }

    @Test
    @Transactional
    void getCumRap() throws Exception {
        // Initialize the database
        insertedCumRap = cumRapRepository.saveAndFlush(cumRap);

        // Get the cumRap
        restCumRapMockMvc
            .perform(get(ENTITY_API_URL_ID, cumRap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cumRap.getId().intValue()))
            .andExpect(jsonPath("$.tenCumRap").value(DEFAULT_TEN_CUM_RAP))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64.getEncoder().encodeToString(DEFAULT_LOGO)));
    }

    @Test
    @Transactional
    void getNonExistingCumRap() throws Exception {
        // Get the cumRap
        restCumRapMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCumRap() throws Exception {
        // Initialize the database
        insertedCumRap = cumRapRepository.saveAndFlush(cumRap);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cumRap
        CumRap updatedCumRap = cumRapRepository.findById(cumRap.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCumRap are not directly saved in db
        em.detach(updatedCumRap);
        updatedCumRap.tenCumRap(UPDATED_TEN_CUM_RAP).logo(UPDATED_LOGO).logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restCumRapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCumRap.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCumRap))
            )
            .andExpect(status().isOk());

        // Validate the CumRap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCumRapToMatchAllProperties(updatedCumRap);
    }

    @Test
    @Transactional
    void putNonExistingCumRap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cumRap.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCumRapMockMvc
            .perform(put(ENTITY_API_URL_ID, cumRap.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cumRap)))
            .andExpect(status().isBadRequest());

        // Validate the CumRap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCumRap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cumRap.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCumRapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cumRap))
            )
            .andExpect(status().isBadRequest());

        // Validate the CumRap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCumRap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cumRap.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCumRapMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cumRap)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CumRap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCumRapWithPatch() throws Exception {
        // Initialize the database
        insertedCumRap = cumRapRepository.saveAndFlush(cumRap);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cumRap using partial update
        CumRap partialUpdatedCumRap = new CumRap();
        partialUpdatedCumRap.setId(cumRap.getId());

        partialUpdatedCumRap.tenCumRap(UPDATED_TEN_CUM_RAP);

        restCumRapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCumRap.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCumRap))
            )
            .andExpect(status().isOk());

        // Validate the CumRap in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCumRapUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCumRap, cumRap), getPersistedCumRap(cumRap));
    }

    @Test
    @Transactional
    void fullUpdateCumRapWithPatch() throws Exception {
        // Initialize the database
        insertedCumRap = cumRapRepository.saveAndFlush(cumRap);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cumRap using partial update
        CumRap partialUpdatedCumRap = new CumRap();
        partialUpdatedCumRap.setId(cumRap.getId());

        partialUpdatedCumRap.tenCumRap(UPDATED_TEN_CUM_RAP).logo(UPDATED_LOGO).logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restCumRapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCumRap.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCumRap))
            )
            .andExpect(status().isOk());

        // Validate the CumRap in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCumRapUpdatableFieldsEquals(partialUpdatedCumRap, getPersistedCumRap(partialUpdatedCumRap));
    }

    @Test
    @Transactional
    void patchNonExistingCumRap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cumRap.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCumRapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cumRap.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cumRap))
            )
            .andExpect(status().isBadRequest());

        // Validate the CumRap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCumRap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cumRap.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCumRapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cumRap))
            )
            .andExpect(status().isBadRequest());

        // Validate the CumRap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCumRap() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cumRap.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCumRapMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cumRap)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CumRap in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCumRap() throws Exception {
        // Initialize the database
        insertedCumRap = cumRapRepository.saveAndFlush(cumRap);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cumRap
        restCumRapMockMvc
            .perform(delete(ENTITY_API_URL_ID, cumRap.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cumRapRepository.count();
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

    protected CumRap getPersistedCumRap(CumRap cumRap) {
        return cumRapRepository.findById(cumRap.getId()).orElseThrow();
    }

    protected void assertPersistedCumRapToMatchAllProperties(CumRap expectedCumRap) {
        assertCumRapAllPropertiesEquals(expectedCumRap, getPersistedCumRap(expectedCumRap));
    }

    protected void assertPersistedCumRapToMatchUpdatableProperties(CumRap expectedCumRap) {
        assertCumRapAllUpdatablePropertiesEquals(expectedCumRap, getPersistedCumRap(expectedCumRap));
    }
}
