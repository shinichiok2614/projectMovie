package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.DanhSachBapNuocAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.DanhSachBapNuoc;
import com.mycompany.myapp.repository.DanhSachBapNuocRepository;
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
 * Integration tests for the {@link DanhSachBapNuocResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DanhSachBapNuocResourceIT {

    private static final String DEFAULT_SO_DIEN_THOAI = "AAAAAAAAAA";
    private static final String UPDATED_SO_DIEN_THOAI = "BBBBBBBBBB";

    private static final String DEFAULT_TEN_BAP_NUOC = "AAAAAAAAAA";
    private static final String UPDATED_TEN_BAP_NUOC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/danh-sach-bap-nuocs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DanhSachBapNuocRepository danhSachBapNuocRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDanhSachBapNuocMockMvc;

    private DanhSachBapNuoc danhSachBapNuoc;

    private DanhSachBapNuoc insertedDanhSachBapNuoc;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DanhSachBapNuoc createEntity(EntityManager em) {
        DanhSachBapNuoc danhSachBapNuoc = new DanhSachBapNuoc().soDienThoai(DEFAULT_SO_DIEN_THOAI).tenBapNuoc(DEFAULT_TEN_BAP_NUOC);
        return danhSachBapNuoc;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DanhSachBapNuoc createUpdatedEntity(EntityManager em) {
        DanhSachBapNuoc danhSachBapNuoc = new DanhSachBapNuoc().soDienThoai(UPDATED_SO_DIEN_THOAI).tenBapNuoc(UPDATED_TEN_BAP_NUOC);
        return danhSachBapNuoc;
    }

    @BeforeEach
    public void initTest() {
        danhSachBapNuoc = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDanhSachBapNuoc != null) {
            danhSachBapNuocRepository.delete(insertedDanhSachBapNuoc);
            insertedDanhSachBapNuoc = null;
        }
    }

    @Test
    @Transactional
    void createDanhSachBapNuoc() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DanhSachBapNuoc
        var returnedDanhSachBapNuoc = om.readValue(
            restDanhSachBapNuocMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(danhSachBapNuoc)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DanhSachBapNuoc.class
        );

        // Validate the DanhSachBapNuoc in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDanhSachBapNuocUpdatableFieldsEquals(returnedDanhSachBapNuoc, getPersistedDanhSachBapNuoc(returnedDanhSachBapNuoc));

        insertedDanhSachBapNuoc = returnedDanhSachBapNuoc;
    }

    @Test
    @Transactional
    void createDanhSachBapNuocWithExistingId() throws Exception {
        // Create the DanhSachBapNuoc with an existing ID
        danhSachBapNuoc.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDanhSachBapNuocMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(danhSachBapNuoc)))
            .andExpect(status().isBadRequest());

        // Validate the DanhSachBapNuoc in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSoDienThoaiIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        danhSachBapNuoc.setSoDienThoai(null);

        // Create the DanhSachBapNuoc, which fails.

        restDanhSachBapNuocMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(danhSachBapNuoc)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTenBapNuocIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        danhSachBapNuoc.setTenBapNuoc(null);

        // Create the DanhSachBapNuoc, which fails.

        restDanhSachBapNuocMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(danhSachBapNuoc)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDanhSachBapNuocs() throws Exception {
        // Initialize the database
        insertedDanhSachBapNuoc = danhSachBapNuocRepository.saveAndFlush(danhSachBapNuoc);

        // Get all the danhSachBapNuocList
        restDanhSachBapNuocMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(danhSachBapNuoc.getId().intValue())))
            .andExpect(jsonPath("$.[*].soDienThoai").value(hasItem(DEFAULT_SO_DIEN_THOAI)))
            .andExpect(jsonPath("$.[*].tenBapNuoc").value(hasItem(DEFAULT_TEN_BAP_NUOC)));
    }

    @Test
    @Transactional
    void getDanhSachBapNuoc() throws Exception {
        // Initialize the database
        insertedDanhSachBapNuoc = danhSachBapNuocRepository.saveAndFlush(danhSachBapNuoc);

        // Get the danhSachBapNuoc
        restDanhSachBapNuocMockMvc
            .perform(get(ENTITY_API_URL_ID, danhSachBapNuoc.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(danhSachBapNuoc.getId().intValue()))
            .andExpect(jsonPath("$.soDienThoai").value(DEFAULT_SO_DIEN_THOAI))
            .andExpect(jsonPath("$.tenBapNuoc").value(DEFAULT_TEN_BAP_NUOC));
    }

    @Test
    @Transactional
    void getNonExistingDanhSachBapNuoc() throws Exception {
        // Get the danhSachBapNuoc
        restDanhSachBapNuocMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDanhSachBapNuoc() throws Exception {
        // Initialize the database
        insertedDanhSachBapNuoc = danhSachBapNuocRepository.saveAndFlush(danhSachBapNuoc);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the danhSachBapNuoc
        DanhSachBapNuoc updatedDanhSachBapNuoc = danhSachBapNuocRepository.findById(danhSachBapNuoc.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDanhSachBapNuoc are not directly saved in db
        em.detach(updatedDanhSachBapNuoc);
        updatedDanhSachBapNuoc.soDienThoai(UPDATED_SO_DIEN_THOAI).tenBapNuoc(UPDATED_TEN_BAP_NUOC);

        restDanhSachBapNuocMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDanhSachBapNuoc.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDanhSachBapNuoc))
            )
            .andExpect(status().isOk());

        // Validate the DanhSachBapNuoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDanhSachBapNuocToMatchAllProperties(updatedDanhSachBapNuoc);
    }

    @Test
    @Transactional
    void putNonExistingDanhSachBapNuoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhSachBapNuoc.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDanhSachBapNuocMockMvc
            .perform(
                put(ENTITY_API_URL_ID, danhSachBapNuoc.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(danhSachBapNuoc))
            )
            .andExpect(status().isBadRequest());

        // Validate the DanhSachBapNuoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDanhSachBapNuoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhSachBapNuoc.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDanhSachBapNuocMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(danhSachBapNuoc))
            )
            .andExpect(status().isBadRequest());

        // Validate the DanhSachBapNuoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDanhSachBapNuoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhSachBapNuoc.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDanhSachBapNuocMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(danhSachBapNuoc)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DanhSachBapNuoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDanhSachBapNuocWithPatch() throws Exception {
        // Initialize the database
        insertedDanhSachBapNuoc = danhSachBapNuocRepository.saveAndFlush(danhSachBapNuoc);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the danhSachBapNuoc using partial update
        DanhSachBapNuoc partialUpdatedDanhSachBapNuoc = new DanhSachBapNuoc();
        partialUpdatedDanhSachBapNuoc.setId(danhSachBapNuoc.getId());

        partialUpdatedDanhSachBapNuoc.soDienThoai(UPDATED_SO_DIEN_THOAI).tenBapNuoc(UPDATED_TEN_BAP_NUOC);

        restDanhSachBapNuocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDanhSachBapNuoc.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDanhSachBapNuoc))
            )
            .andExpect(status().isOk());

        // Validate the DanhSachBapNuoc in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDanhSachBapNuocUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDanhSachBapNuoc, danhSachBapNuoc),
            getPersistedDanhSachBapNuoc(danhSachBapNuoc)
        );
    }

    @Test
    @Transactional
    void fullUpdateDanhSachBapNuocWithPatch() throws Exception {
        // Initialize the database
        insertedDanhSachBapNuoc = danhSachBapNuocRepository.saveAndFlush(danhSachBapNuoc);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the danhSachBapNuoc using partial update
        DanhSachBapNuoc partialUpdatedDanhSachBapNuoc = new DanhSachBapNuoc();
        partialUpdatedDanhSachBapNuoc.setId(danhSachBapNuoc.getId());

        partialUpdatedDanhSachBapNuoc.soDienThoai(UPDATED_SO_DIEN_THOAI).tenBapNuoc(UPDATED_TEN_BAP_NUOC);

        restDanhSachBapNuocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDanhSachBapNuoc.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDanhSachBapNuoc))
            )
            .andExpect(status().isOk());

        // Validate the DanhSachBapNuoc in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDanhSachBapNuocUpdatableFieldsEquals(
            partialUpdatedDanhSachBapNuoc,
            getPersistedDanhSachBapNuoc(partialUpdatedDanhSachBapNuoc)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDanhSachBapNuoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhSachBapNuoc.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDanhSachBapNuocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, danhSachBapNuoc.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(danhSachBapNuoc))
            )
            .andExpect(status().isBadRequest());

        // Validate the DanhSachBapNuoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDanhSachBapNuoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhSachBapNuoc.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDanhSachBapNuocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(danhSachBapNuoc))
            )
            .andExpect(status().isBadRequest());

        // Validate the DanhSachBapNuoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDanhSachBapNuoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        danhSachBapNuoc.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDanhSachBapNuocMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(danhSachBapNuoc)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DanhSachBapNuoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDanhSachBapNuoc() throws Exception {
        // Initialize the database
        insertedDanhSachBapNuoc = danhSachBapNuocRepository.saveAndFlush(danhSachBapNuoc);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the danhSachBapNuoc
        restDanhSachBapNuocMockMvc
            .perform(delete(ENTITY_API_URL_ID, danhSachBapNuoc.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return danhSachBapNuocRepository.count();
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

    protected DanhSachBapNuoc getPersistedDanhSachBapNuoc(DanhSachBapNuoc danhSachBapNuoc) {
        return danhSachBapNuocRepository.findById(danhSachBapNuoc.getId()).orElseThrow();
    }

    protected void assertPersistedDanhSachBapNuocToMatchAllProperties(DanhSachBapNuoc expectedDanhSachBapNuoc) {
        assertDanhSachBapNuocAllPropertiesEquals(expectedDanhSachBapNuoc, getPersistedDanhSachBapNuoc(expectedDanhSachBapNuoc));
    }

    protected void assertPersistedDanhSachBapNuocToMatchUpdatableProperties(DanhSachBapNuoc expectedDanhSachBapNuoc) {
        assertDanhSachBapNuocAllUpdatablePropertiesEquals(expectedDanhSachBapNuoc, getPersistedDanhSachBapNuoc(expectedDanhSachBapNuoc));
    }
}
