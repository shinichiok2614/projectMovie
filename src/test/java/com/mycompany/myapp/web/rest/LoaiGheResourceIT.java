package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.LoaiGheAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LoaiGhe;
import com.mycompany.myapp.repository.LoaiGheRepository;
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
 * Integration tests for the {@link LoaiGheResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LoaiGheResourceIT {

    private static final String DEFAULT_TEN_LOAI = "AAAAAAAAAA";
    private static final String UPDATED_TEN_LOAI = "BBBBBBBBBB";

    private static final Integer DEFAULT_GIA_TIEN = 1;
    private static final Integer UPDATED_GIA_TIEN = 2;

    private static final String ENTITY_API_URL = "/api/loai-ghes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LoaiGheRepository loaiGheRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLoaiGheMockMvc;

    private LoaiGhe loaiGhe;

    private LoaiGhe insertedLoaiGhe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoaiGhe createEntity(EntityManager em) {
        LoaiGhe loaiGhe = new LoaiGhe().tenLoai(DEFAULT_TEN_LOAI).giaTien(DEFAULT_GIA_TIEN);
        return loaiGhe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoaiGhe createUpdatedEntity(EntityManager em) {
        LoaiGhe loaiGhe = new LoaiGhe().tenLoai(UPDATED_TEN_LOAI).giaTien(UPDATED_GIA_TIEN);
        return loaiGhe;
    }

    @BeforeEach
    public void initTest() {
        loaiGhe = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedLoaiGhe != null) {
            loaiGheRepository.delete(insertedLoaiGhe);
            insertedLoaiGhe = null;
        }
    }

    @Test
    @Transactional
    void createLoaiGhe() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LoaiGhe
        var returnedLoaiGhe = om.readValue(
            restLoaiGheMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loaiGhe)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LoaiGhe.class
        );

        // Validate the LoaiGhe in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLoaiGheUpdatableFieldsEquals(returnedLoaiGhe, getPersistedLoaiGhe(returnedLoaiGhe));

        insertedLoaiGhe = returnedLoaiGhe;
    }

    @Test
    @Transactional
    void createLoaiGheWithExistingId() throws Exception {
        // Create the LoaiGhe with an existing ID
        loaiGhe.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoaiGheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loaiGhe)))
            .andExpect(status().isBadRequest());

        // Validate the LoaiGhe in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTenLoaiIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        loaiGhe.setTenLoai(null);

        // Create the LoaiGhe, which fails.

        restLoaiGheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loaiGhe)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGiaTienIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        loaiGhe.setGiaTien(null);

        // Create the LoaiGhe, which fails.

        restLoaiGheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loaiGhe)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLoaiGhes() throws Exception {
        // Initialize the database
        insertedLoaiGhe = loaiGheRepository.saveAndFlush(loaiGhe);

        // Get all the loaiGheList
        restLoaiGheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loaiGhe.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenLoai").value(hasItem(DEFAULT_TEN_LOAI)))
            .andExpect(jsonPath("$.[*].giaTien").value(hasItem(DEFAULT_GIA_TIEN)));
    }

    @Test
    @Transactional
    void getLoaiGhe() throws Exception {
        // Initialize the database
        insertedLoaiGhe = loaiGheRepository.saveAndFlush(loaiGhe);

        // Get the loaiGhe
        restLoaiGheMockMvc
            .perform(get(ENTITY_API_URL_ID, loaiGhe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(loaiGhe.getId().intValue()))
            .andExpect(jsonPath("$.tenLoai").value(DEFAULT_TEN_LOAI))
            .andExpect(jsonPath("$.giaTien").value(DEFAULT_GIA_TIEN));
    }

    @Test
    @Transactional
    void getNonExistingLoaiGhe() throws Exception {
        // Get the loaiGhe
        restLoaiGheMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLoaiGhe() throws Exception {
        // Initialize the database
        insertedLoaiGhe = loaiGheRepository.saveAndFlush(loaiGhe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loaiGhe
        LoaiGhe updatedLoaiGhe = loaiGheRepository.findById(loaiGhe.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLoaiGhe are not directly saved in db
        em.detach(updatedLoaiGhe);
        updatedLoaiGhe.tenLoai(UPDATED_TEN_LOAI).giaTien(UPDATED_GIA_TIEN);

        restLoaiGheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLoaiGhe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLoaiGhe))
            )
            .andExpect(status().isOk());

        // Validate the LoaiGhe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLoaiGheToMatchAllProperties(updatedLoaiGhe);
    }

    @Test
    @Transactional
    void putNonExistingLoaiGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loaiGhe.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoaiGheMockMvc
            .perform(put(ENTITY_API_URL_ID, loaiGhe.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loaiGhe)))
            .andExpect(status().isBadRequest());

        // Validate the LoaiGhe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLoaiGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loaiGhe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoaiGheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(loaiGhe))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoaiGhe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLoaiGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loaiGhe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoaiGheMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(loaiGhe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoaiGhe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLoaiGheWithPatch() throws Exception {
        // Initialize the database
        insertedLoaiGhe = loaiGheRepository.saveAndFlush(loaiGhe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loaiGhe using partial update
        LoaiGhe partialUpdatedLoaiGhe = new LoaiGhe();
        partialUpdatedLoaiGhe.setId(loaiGhe.getId());

        restLoaiGheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoaiGhe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLoaiGhe))
            )
            .andExpect(status().isOk());

        // Validate the LoaiGhe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLoaiGheUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLoaiGhe, loaiGhe), getPersistedLoaiGhe(loaiGhe));
    }

    @Test
    @Transactional
    void fullUpdateLoaiGheWithPatch() throws Exception {
        // Initialize the database
        insertedLoaiGhe = loaiGheRepository.saveAndFlush(loaiGhe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the loaiGhe using partial update
        LoaiGhe partialUpdatedLoaiGhe = new LoaiGhe();
        partialUpdatedLoaiGhe.setId(loaiGhe.getId());

        partialUpdatedLoaiGhe.tenLoai(UPDATED_TEN_LOAI).giaTien(UPDATED_GIA_TIEN);

        restLoaiGheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLoaiGhe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLoaiGhe))
            )
            .andExpect(status().isOk());

        // Validate the LoaiGhe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLoaiGheUpdatableFieldsEquals(partialUpdatedLoaiGhe, getPersistedLoaiGhe(partialUpdatedLoaiGhe));
    }

    @Test
    @Transactional
    void patchNonExistingLoaiGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loaiGhe.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoaiGheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, loaiGhe.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(loaiGhe))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoaiGhe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLoaiGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loaiGhe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoaiGheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(loaiGhe))
            )
            .andExpect(status().isBadRequest());

        // Validate the LoaiGhe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLoaiGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        loaiGhe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLoaiGheMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(loaiGhe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LoaiGhe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLoaiGhe() throws Exception {
        // Initialize the database
        insertedLoaiGhe = loaiGheRepository.saveAndFlush(loaiGhe);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the loaiGhe
        restLoaiGheMockMvc
            .perform(delete(ENTITY_API_URL_ID, loaiGhe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return loaiGheRepository.count();
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

    protected LoaiGhe getPersistedLoaiGhe(LoaiGhe loaiGhe) {
        return loaiGheRepository.findById(loaiGhe.getId()).orElseThrow();
    }

    protected void assertPersistedLoaiGheToMatchAllProperties(LoaiGhe expectedLoaiGhe) {
        assertLoaiGheAllPropertiesEquals(expectedLoaiGhe, getPersistedLoaiGhe(expectedLoaiGhe));
    }

    protected void assertPersistedLoaiGheToMatchUpdatableProperties(LoaiGhe expectedLoaiGhe) {
        assertLoaiGheAllUpdatablePropertiesEquals(expectedLoaiGhe, getPersistedLoaiGhe(expectedLoaiGhe));
    }
}
