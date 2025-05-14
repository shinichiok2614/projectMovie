package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.DanhSachGheAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.DanhSachGhe;
import com.mycompany.myapp.repository.DanhSachGheRepository;
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
 * Integration tests for the {@link DanhSachGheResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DanhSachGheResourceIT {

    private static final String DEFAULT_SO_DIEN_THOAI = "AAAAAAAAAA";
    private static final String UPDATED_SO_DIEN_THOAI = "BBBBBBBBBB";

    private static final String DEFAULT_TEN_GHE = "AAAAAAAAAA";
    private static final String UPDATED_TEN_GHE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/danh-sach-ghes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DanhSachGheRepository danhSachGheRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDanhSachGheMockMvc;

    private DanhSachGhe danhSachGhe;

    private DanhSachGhe insertedDanhSachGhe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DanhSachGhe createEntity(EntityManager em) {
        DanhSachGhe danhSachGhe = new DanhSachGhe().soDienThoai(DEFAULT_SO_DIEN_THOAI).tenGhe(DEFAULT_TEN_GHE);
        return danhSachGhe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DanhSachGhe createUpdatedEntity(EntityManager em) {
        DanhSachGhe danhSachGhe = new DanhSachGhe().soDienThoai(UPDATED_SO_DIEN_THOAI).tenGhe(UPDATED_TEN_GHE);
        return danhSachGhe;
    }

    @BeforeEach
    public void initTest() {
        danhSachGhe = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDanhSachGhe != null) {
            danhSachGheRepository.delete(insertedDanhSachGhe);
            insertedDanhSachGhe = null;
        }
    }

    @Test
    @Transactional
    void createDanhSachGhe() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DanhSachGhe
        var returnedDanhSachGhe = om.readValue(
            restDanhSachGheMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(danhSachGhe)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DanhSachGhe.class
        );

        // Validate the DanhSachGhe in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDanhSachGheUpdatableFieldsEquals(returnedDanhSachGhe, getPersistedDanhSachGhe(returnedDanhSachGhe));

        insertedDanhSachGhe = returnedDanhSachGhe;
    }

    @Test
    @Transactional
    void createDanhSachGheWithExistingId() throws Exception {
        // Create the DanhSachGhe with an existing ID
        danhSachGhe.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDanhSachGheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(danhSachGhe)))
            .andExpect(status().isBadRequest());

        // Validate the DanhSachGhe in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSoDienThoaiIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        danhSachGhe.setSoDienThoai(null);

        // Create the DanhSachGhe, which fails.

        restDanhSachGheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(danhSachGhe)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTenGheIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        danhSachGhe.setTenGhe(null);

        // Create the DanhSachGhe, which fails.

        restDanhSachGheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(danhSachGhe)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDanhSachGhes() throws Exception {
        // Initialize the database
        insertedDanhSachGhe = danhSachGheRepository.saveAndFlush(danhSachGhe);

        // Get all the danhSachGheList
        restDanhSachGheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(danhSachGhe.getId().intValue())))
            .andExpect(jsonPath("$.[*].soDienThoai").value(hasItem(DEFAULT_SO_DIEN_THOAI)))
            .andExpect(jsonPath("$.[*].tenGhe").value(hasItem(DEFAULT_TEN_GHE)));
    }

    @Test
    @Transactional
    void getDanhSachGhe() throws Exception {
        // Initialize the database
        insertedDanhSachGhe = danhSachGheRepository.saveAndFlush(danhSachGhe);

        // Get the danhSachGhe
        restDanhSachGheMockMvc
            .perform(get(ENTITY_API_URL_ID, danhSachGhe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(danhSachGhe.getId().intValue()))
            .andExpect(jsonPath("$.soDienThoai").value(DEFAULT_SO_DIEN_THOAI))
            .andExpect(jsonPath("$.tenGhe").value(DEFAULT_TEN_GHE));
    }

    @Test
    @Transactional
    void getNonExistingDanhSachGhe() throws Exception {
        // Get the danhSachGhe
        restDanhSachGheMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDanhSachGhe() throws Exception {
        // Initialize the database
        insertedDanhSachGhe = danhSachGheRepository.saveAndFlush(danhSachGhe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the danhSachGhe
        DanhSachGhe updatedDanhSachGhe = danhSachGheRepository.findById(danhSachGhe.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDanhSachGhe are not directly saved in db
        em.detach(updatedDanhSachGhe);
        updatedDanhSachGhe.soDienThoai(UPDATED_SO_DIEN_THOAI).tenGhe(UPDATED_TEN_GHE);

        restDanhSachGheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDanhSachGhe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDanhSachGhe))
            )
            .andExpect(status().isOk());

        // Validate the DanhSachGhe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDanhSachGheToMatchAllProperties(updatedDanhSachGhe);
    }

    @Test
    @Transactional
    void putNonExistingDanhSachGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhSachGhe.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDanhSachGheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, danhSachGhe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(danhSachGhe))
            )
            .andExpect(status().isBadRequest());

        // Validate the DanhSachGhe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDanhSachGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhSachGhe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDanhSachGheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(danhSachGhe))
            )
            .andExpect(status().isBadRequest());

        // Validate the DanhSachGhe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDanhSachGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhSachGhe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDanhSachGheMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(danhSachGhe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DanhSachGhe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDanhSachGheWithPatch() throws Exception {
        // Initialize the database
        insertedDanhSachGhe = danhSachGheRepository.saveAndFlush(danhSachGhe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the danhSachGhe using partial update
        DanhSachGhe partialUpdatedDanhSachGhe = new DanhSachGhe();
        partialUpdatedDanhSachGhe.setId(danhSachGhe.getId());

        partialUpdatedDanhSachGhe.soDienThoai(UPDATED_SO_DIEN_THOAI).tenGhe(UPDATED_TEN_GHE);

        restDanhSachGheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDanhSachGhe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDanhSachGhe))
            )
            .andExpect(status().isOk());

        // Validate the DanhSachGhe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDanhSachGheUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDanhSachGhe, danhSachGhe),
            getPersistedDanhSachGhe(danhSachGhe)
        );
    }

    @Test
    @Transactional
    void fullUpdateDanhSachGheWithPatch() throws Exception {
        // Initialize the database
        insertedDanhSachGhe = danhSachGheRepository.saveAndFlush(danhSachGhe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the danhSachGhe using partial update
        DanhSachGhe partialUpdatedDanhSachGhe = new DanhSachGhe();
        partialUpdatedDanhSachGhe.setId(danhSachGhe.getId());

        partialUpdatedDanhSachGhe.soDienThoai(UPDATED_SO_DIEN_THOAI).tenGhe(UPDATED_TEN_GHE);

        restDanhSachGheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDanhSachGhe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDanhSachGhe))
            )
            .andExpect(status().isOk());

        // Validate the DanhSachGhe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDanhSachGheUpdatableFieldsEquals(partialUpdatedDanhSachGhe, getPersistedDanhSachGhe(partialUpdatedDanhSachGhe));
    }

    @Test
    @Transactional
    void patchNonExistingDanhSachGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhSachGhe.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDanhSachGheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, danhSachGhe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(danhSachGhe))
            )
            .andExpect(status().isBadRequest());

        // Validate the DanhSachGhe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDanhSachGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhSachGhe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDanhSachGheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(danhSachGhe))
            )
            .andExpect(status().isBadRequest());

        // Validate the DanhSachGhe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDanhSachGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhSachGhe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDanhSachGheMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(danhSachGhe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DanhSachGhe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDanhSachGhe() throws Exception {
        // Initialize the database
        insertedDanhSachGhe = danhSachGheRepository.saveAndFlush(danhSachGhe);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the danhSachGhe
        restDanhSachGheMockMvc
            .perform(delete(ENTITY_API_URL_ID, danhSachGhe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return danhSachGheRepository.count();
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

    protected DanhSachGhe getPersistedDanhSachGhe(DanhSachGhe danhSachGhe) {
        return danhSachGheRepository.findById(danhSachGhe.getId()).orElseThrow();
    }

    protected void assertPersistedDanhSachGheToMatchAllProperties(DanhSachGhe expectedDanhSachGhe) {
        assertDanhSachGheAllPropertiesEquals(expectedDanhSachGhe, getPersistedDanhSachGhe(expectedDanhSachGhe));
    }

    protected void assertPersistedDanhSachGheToMatchUpdatableProperties(DanhSachGhe expectedDanhSachGhe) {
        assertDanhSachGheAllUpdatablePropertiesEquals(expectedDanhSachGhe, getPersistedDanhSachGhe(expectedDanhSachGhe));
    }
}
