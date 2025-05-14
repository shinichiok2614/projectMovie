package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.BapNuocAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.BapNuoc;
import com.mycompany.myapp.repository.BapNuocRepository;
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
 * Integration tests for the {@link BapNuocResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BapNuocResourceIT {

    private static final String DEFAULT_TEN_BAP_NUOC = "AAAAAAAAAA";
    private static final String UPDATED_TEN_BAP_NUOC = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_GIA_TIEN = 1;
    private static final Integer UPDATED_GIA_TIEN = 2;

    private static final String ENTITY_API_URL = "/api/bap-nuocs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BapNuocRepository bapNuocRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBapNuocMockMvc;

    private BapNuoc bapNuoc;

    private BapNuoc insertedBapNuoc;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BapNuoc createEntity(EntityManager em) {
        BapNuoc bapNuoc = new BapNuoc()
            .tenBapNuoc(DEFAULT_TEN_BAP_NUOC)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .giaTien(DEFAULT_GIA_TIEN);
        return bapNuoc;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BapNuoc createUpdatedEntity(EntityManager em) {
        BapNuoc bapNuoc = new BapNuoc()
            .tenBapNuoc(UPDATED_TEN_BAP_NUOC)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .giaTien(UPDATED_GIA_TIEN);
        return bapNuoc;
    }

    @BeforeEach
    public void initTest() {
        bapNuoc = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedBapNuoc != null) {
            bapNuocRepository.delete(insertedBapNuoc);
            insertedBapNuoc = null;
        }
    }

    @Test
    @Transactional
    void createBapNuoc() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BapNuoc
        var returnedBapNuoc = om.readValue(
            restBapNuocMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bapNuoc)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BapNuoc.class
        );

        // Validate the BapNuoc in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertBapNuocUpdatableFieldsEquals(returnedBapNuoc, getPersistedBapNuoc(returnedBapNuoc));

        insertedBapNuoc = returnedBapNuoc;
    }

    @Test
    @Transactional
    void createBapNuocWithExistingId() throws Exception {
        // Create the BapNuoc with an existing ID
        bapNuoc.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBapNuocMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bapNuoc)))
            .andExpect(status().isBadRequest());

        // Validate the BapNuoc in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTenBapNuocIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bapNuoc.setTenBapNuoc(null);

        // Create the BapNuoc, which fails.

        restBapNuocMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bapNuoc)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGiaTienIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bapNuoc.setGiaTien(null);

        // Create the BapNuoc, which fails.

        restBapNuocMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bapNuoc)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBapNuocs() throws Exception {
        // Initialize the database
        insertedBapNuoc = bapNuocRepository.saveAndFlush(bapNuoc);

        // Get all the bapNuocList
        restBapNuocMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bapNuoc.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenBapNuoc").value(hasItem(DEFAULT_TEN_BAP_NUOC)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].giaTien").value(hasItem(DEFAULT_GIA_TIEN)));
    }

    @Test
    @Transactional
    void getBapNuoc() throws Exception {
        // Initialize the database
        insertedBapNuoc = bapNuocRepository.saveAndFlush(bapNuoc);

        // Get the bapNuoc
        restBapNuocMockMvc
            .perform(get(ENTITY_API_URL_ID, bapNuoc.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bapNuoc.getId().intValue()))
            .andExpect(jsonPath("$.tenBapNuoc").value(DEFAULT_TEN_BAP_NUOC))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64.getEncoder().encodeToString(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.giaTien").value(DEFAULT_GIA_TIEN));
    }

    @Test
    @Transactional
    void getNonExistingBapNuoc() throws Exception {
        // Get the bapNuoc
        restBapNuocMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBapNuoc() throws Exception {
        // Initialize the database
        insertedBapNuoc = bapNuocRepository.saveAndFlush(bapNuoc);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bapNuoc
        BapNuoc updatedBapNuoc = bapNuocRepository.findById(bapNuoc.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBapNuoc are not directly saved in db
        em.detach(updatedBapNuoc);
        updatedBapNuoc
            .tenBapNuoc(UPDATED_TEN_BAP_NUOC)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .giaTien(UPDATED_GIA_TIEN);

        restBapNuocMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBapNuoc.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedBapNuoc))
            )
            .andExpect(status().isOk());

        // Validate the BapNuoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBapNuocToMatchAllProperties(updatedBapNuoc);
    }

    @Test
    @Transactional
    void putNonExistingBapNuoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bapNuoc.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBapNuocMockMvc
            .perform(put(ENTITY_API_URL_ID, bapNuoc.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bapNuoc)))
            .andExpect(status().isBadRequest());

        // Validate the BapNuoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBapNuoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bapNuoc.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBapNuocMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bapNuoc))
            )
            .andExpect(status().isBadRequest());

        // Validate the BapNuoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBapNuoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bapNuoc.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBapNuocMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bapNuoc)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BapNuoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBapNuocWithPatch() throws Exception {
        // Initialize the database
        insertedBapNuoc = bapNuocRepository.saveAndFlush(bapNuoc);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bapNuoc using partial update
        BapNuoc partialUpdatedBapNuoc = new BapNuoc();
        partialUpdatedBapNuoc.setId(bapNuoc.getId());

        partialUpdatedBapNuoc.tenBapNuoc(UPDATED_TEN_BAP_NUOC).logo(UPDATED_LOGO).logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restBapNuocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBapNuoc.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBapNuoc))
            )
            .andExpect(status().isOk());

        // Validate the BapNuoc in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBapNuocUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBapNuoc, bapNuoc), getPersistedBapNuoc(bapNuoc));
    }

    @Test
    @Transactional
    void fullUpdateBapNuocWithPatch() throws Exception {
        // Initialize the database
        insertedBapNuoc = bapNuocRepository.saveAndFlush(bapNuoc);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bapNuoc using partial update
        BapNuoc partialUpdatedBapNuoc = new BapNuoc();
        partialUpdatedBapNuoc.setId(bapNuoc.getId());

        partialUpdatedBapNuoc
            .tenBapNuoc(UPDATED_TEN_BAP_NUOC)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .giaTien(UPDATED_GIA_TIEN);

        restBapNuocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBapNuoc.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBapNuoc))
            )
            .andExpect(status().isOk());

        // Validate the BapNuoc in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBapNuocUpdatableFieldsEquals(partialUpdatedBapNuoc, getPersistedBapNuoc(partialUpdatedBapNuoc));
    }

    @Test
    @Transactional
    void patchNonExistingBapNuoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bapNuoc.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBapNuocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bapNuoc.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bapNuoc))
            )
            .andExpect(status().isBadRequest());

        // Validate the BapNuoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBapNuoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bapNuoc.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBapNuocMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bapNuoc))
            )
            .andExpect(status().isBadRequest());

        // Validate the BapNuoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBapNuoc() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bapNuoc.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBapNuocMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bapNuoc)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BapNuoc in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBapNuoc() throws Exception {
        // Initialize the database
        insertedBapNuoc = bapNuocRepository.saveAndFlush(bapNuoc);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bapNuoc
        restBapNuocMockMvc
            .perform(delete(ENTITY_API_URL_ID, bapNuoc.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bapNuocRepository.count();
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

    protected BapNuoc getPersistedBapNuoc(BapNuoc bapNuoc) {
        return bapNuocRepository.findById(bapNuoc.getId()).orElseThrow();
    }

    protected void assertPersistedBapNuocToMatchAllProperties(BapNuoc expectedBapNuoc) {
        assertBapNuocAllPropertiesEquals(expectedBapNuoc, getPersistedBapNuoc(expectedBapNuoc));
    }

    protected void assertPersistedBapNuocToMatchUpdatableProperties(BapNuoc expectedBapNuoc) {
        assertBapNuocAllUpdatablePropertiesEquals(expectedBapNuoc, getPersistedBapNuoc(expectedBapNuoc));
    }
}
