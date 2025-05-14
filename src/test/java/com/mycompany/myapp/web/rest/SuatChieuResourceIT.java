package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SuatChieuAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SuatChieu;
import com.mycompany.myapp.repository.SuatChieuRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SuatChieuResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SuatChieuResourceIT {

    private static final LocalDate DEFAULT_NGAY_CHIEU = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NGAY_CHIEU = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_GIO_CHIEU = "AAAAAAAAAA";
    private static final String UPDATED_GIO_CHIEU = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/suat-chieus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SuatChieuRepository suatChieuRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSuatChieuMockMvc;

    private SuatChieu suatChieu;

    private SuatChieu insertedSuatChieu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuatChieu createEntity(EntityManager em) {
        SuatChieu suatChieu = new SuatChieu().ngayChieu(DEFAULT_NGAY_CHIEU).gioChieu(DEFAULT_GIO_CHIEU);
        return suatChieu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SuatChieu createUpdatedEntity(EntityManager em) {
        SuatChieu suatChieu = new SuatChieu().ngayChieu(UPDATED_NGAY_CHIEU).gioChieu(UPDATED_GIO_CHIEU);
        return suatChieu;
    }

    @BeforeEach
    public void initTest() {
        suatChieu = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSuatChieu != null) {
            suatChieuRepository.delete(insertedSuatChieu);
            insertedSuatChieu = null;
        }
    }

    @Test
    @Transactional
    void createSuatChieu() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SuatChieu
        var returnedSuatChieu = om.readValue(
            restSuatChieuMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(suatChieu)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SuatChieu.class
        );

        // Validate the SuatChieu in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSuatChieuUpdatableFieldsEquals(returnedSuatChieu, getPersistedSuatChieu(returnedSuatChieu));

        insertedSuatChieu = returnedSuatChieu;
    }

    @Test
    @Transactional
    void createSuatChieuWithExistingId() throws Exception {
        // Create the SuatChieu with an existing ID
        suatChieu.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuatChieuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(suatChieu)))
            .andExpect(status().isBadRequest());

        // Validate the SuatChieu in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNgayChieuIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        suatChieu.setNgayChieu(null);

        // Create the SuatChieu, which fails.

        restSuatChieuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(suatChieu)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGioChieuIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        suatChieu.setGioChieu(null);

        // Create the SuatChieu, which fails.

        restSuatChieuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(suatChieu)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSuatChieus() throws Exception {
        // Initialize the database
        insertedSuatChieu = suatChieuRepository.saveAndFlush(suatChieu);

        // Get all the suatChieuList
        restSuatChieuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suatChieu.getId().intValue())))
            .andExpect(jsonPath("$.[*].ngayChieu").value(hasItem(DEFAULT_NGAY_CHIEU.toString())))
            .andExpect(jsonPath("$.[*].gioChieu").value(hasItem(DEFAULT_GIO_CHIEU)));
    }

    @Test
    @Transactional
    void getSuatChieu() throws Exception {
        // Initialize the database
        insertedSuatChieu = suatChieuRepository.saveAndFlush(suatChieu);

        // Get the suatChieu
        restSuatChieuMockMvc
            .perform(get(ENTITY_API_URL_ID, suatChieu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(suatChieu.getId().intValue()))
            .andExpect(jsonPath("$.ngayChieu").value(DEFAULT_NGAY_CHIEU.toString()))
            .andExpect(jsonPath("$.gioChieu").value(DEFAULT_GIO_CHIEU));
    }

    @Test
    @Transactional
    void getNonExistingSuatChieu() throws Exception {
        // Get the suatChieu
        restSuatChieuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSuatChieu() throws Exception {
        // Initialize the database
        insertedSuatChieu = suatChieuRepository.saveAndFlush(suatChieu);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the suatChieu
        SuatChieu updatedSuatChieu = suatChieuRepository.findById(suatChieu.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSuatChieu are not directly saved in db
        em.detach(updatedSuatChieu);
        updatedSuatChieu.ngayChieu(UPDATED_NGAY_CHIEU).gioChieu(UPDATED_GIO_CHIEU);

        restSuatChieuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSuatChieu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSuatChieu))
            )
            .andExpect(status().isOk());

        // Validate the SuatChieu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSuatChieuToMatchAllProperties(updatedSuatChieu);
    }

    @Test
    @Transactional
    void putNonExistingSuatChieu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        suatChieu.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuatChieuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, suatChieu.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(suatChieu))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuatChieu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSuatChieu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        suatChieu.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuatChieuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(suatChieu))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuatChieu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSuatChieu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        suatChieu.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuatChieuMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(suatChieu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SuatChieu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSuatChieuWithPatch() throws Exception {
        // Initialize the database
        insertedSuatChieu = suatChieuRepository.saveAndFlush(suatChieu);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the suatChieu using partial update
        SuatChieu partialUpdatedSuatChieu = new SuatChieu();
        partialUpdatedSuatChieu.setId(suatChieu.getId());

        partialUpdatedSuatChieu.gioChieu(UPDATED_GIO_CHIEU);

        restSuatChieuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuatChieu.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSuatChieu))
            )
            .andExpect(status().isOk());

        // Validate the SuatChieu in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSuatChieuUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSuatChieu, suatChieu),
            getPersistedSuatChieu(suatChieu)
        );
    }

    @Test
    @Transactional
    void fullUpdateSuatChieuWithPatch() throws Exception {
        // Initialize the database
        insertedSuatChieu = suatChieuRepository.saveAndFlush(suatChieu);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the suatChieu using partial update
        SuatChieu partialUpdatedSuatChieu = new SuatChieu();
        partialUpdatedSuatChieu.setId(suatChieu.getId());

        partialUpdatedSuatChieu.ngayChieu(UPDATED_NGAY_CHIEU).gioChieu(UPDATED_GIO_CHIEU);

        restSuatChieuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSuatChieu.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSuatChieu))
            )
            .andExpect(status().isOk());

        // Validate the SuatChieu in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSuatChieuUpdatableFieldsEquals(partialUpdatedSuatChieu, getPersistedSuatChieu(partialUpdatedSuatChieu));
    }

    @Test
    @Transactional
    void patchNonExistingSuatChieu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        suatChieu.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuatChieuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, suatChieu.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(suatChieu))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuatChieu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSuatChieu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        suatChieu.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuatChieuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(suatChieu))
            )
            .andExpect(status().isBadRequest());

        // Validate the SuatChieu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSuatChieu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        suatChieu.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSuatChieuMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(suatChieu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SuatChieu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSuatChieu() throws Exception {
        // Initialize the database
        insertedSuatChieu = suatChieuRepository.saveAndFlush(suatChieu);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the suatChieu
        restSuatChieuMockMvc
            .perform(delete(ENTITY_API_URL_ID, suatChieu.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return suatChieuRepository.count();
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

    protected SuatChieu getPersistedSuatChieu(SuatChieu suatChieu) {
        return suatChieuRepository.findById(suatChieu.getId()).orElseThrow();
    }

    protected void assertPersistedSuatChieuToMatchAllProperties(SuatChieu expectedSuatChieu) {
        assertSuatChieuAllPropertiesEquals(expectedSuatChieu, getPersistedSuatChieu(expectedSuatChieu));
    }

    protected void assertPersistedSuatChieuToMatchUpdatableProperties(SuatChieu expectedSuatChieu) {
        assertSuatChieuAllUpdatablePropertiesEquals(expectedSuatChieu, getPersistedSuatChieu(expectedSuatChieu));
    }
}
