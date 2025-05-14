package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.GheAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Ghe;
import com.mycompany.myapp.domain.enumeration.TinhTrangGhe;
import com.mycompany.myapp.repository.GheRepository;
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
 * Integration tests for the {@link GheResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GheResourceIT {

    private static final String DEFAULT_TEN_GHE = "AAAAAAAAAA";
    private static final String UPDATED_TEN_GHE = "BBBBBBBBBB";

    private static final TinhTrangGhe DEFAULT_TINH_TRANG = TinhTrangGhe.TRONG;
    private static final TinhTrangGhe UPDATED_TINH_TRANG = TinhTrangGhe.DA_DAT;

    private static final String ENTITY_API_URL = "/api/ghes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GheRepository gheRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGheMockMvc;

    private Ghe ghe;

    private Ghe insertedGhe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ghe createEntity(EntityManager em) {
        Ghe ghe = new Ghe().tenGhe(DEFAULT_TEN_GHE).tinhTrang(DEFAULT_TINH_TRANG);
        return ghe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ghe createUpdatedEntity(EntityManager em) {
        Ghe ghe = new Ghe().tenGhe(UPDATED_TEN_GHE).tinhTrang(UPDATED_TINH_TRANG);
        return ghe;
    }

    @BeforeEach
    public void initTest() {
        ghe = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedGhe != null) {
            gheRepository.delete(insertedGhe);
            insertedGhe = null;
        }
    }

    @Test
    @Transactional
    void createGhe() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Ghe
        var returnedGhe = om.readValue(
            restGheMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ghe)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Ghe.class
        );

        // Validate the Ghe in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertGheUpdatableFieldsEquals(returnedGhe, getPersistedGhe(returnedGhe));

        insertedGhe = returnedGhe;
    }

    @Test
    @Transactional
    void createGheWithExistingId() throws Exception {
        // Create the Ghe with an existing ID
        ghe.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ghe)))
            .andExpect(status().isBadRequest());

        // Validate the Ghe in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTenGheIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ghe.setTenGhe(null);

        // Create the Ghe, which fails.

        restGheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ghe)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTinhTrangIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ghe.setTinhTrang(null);

        // Create the Ghe, which fails.

        restGheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ghe)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGhes() throws Exception {
        // Initialize the database
        insertedGhe = gheRepository.saveAndFlush(ghe);

        // Get all the gheList
        restGheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ghe.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenGhe").value(hasItem(DEFAULT_TEN_GHE)))
            .andExpect(jsonPath("$.[*].tinhTrang").value(hasItem(DEFAULT_TINH_TRANG.toString())));
    }

    @Test
    @Transactional
    void getGhe() throws Exception {
        // Initialize the database
        insertedGhe = gheRepository.saveAndFlush(ghe);

        // Get the ghe
        restGheMockMvc
            .perform(get(ENTITY_API_URL_ID, ghe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ghe.getId().intValue()))
            .andExpect(jsonPath("$.tenGhe").value(DEFAULT_TEN_GHE))
            .andExpect(jsonPath("$.tinhTrang").value(DEFAULT_TINH_TRANG.toString()));
    }

    @Test
    @Transactional
    void getNonExistingGhe() throws Exception {
        // Get the ghe
        restGheMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGhe() throws Exception {
        // Initialize the database
        insertedGhe = gheRepository.saveAndFlush(ghe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ghe
        Ghe updatedGhe = gheRepository.findById(ghe.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGhe are not directly saved in db
        em.detach(updatedGhe);
        updatedGhe.tenGhe(UPDATED_TEN_GHE).tinhTrang(UPDATED_TINH_TRANG);

        restGheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGhe.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedGhe))
            )
            .andExpect(status().isOk());

        // Validate the Ghe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGheToMatchAllProperties(updatedGhe);
    }

    @Test
    @Transactional
    void putNonExistingGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ghe.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGheMockMvc
            .perform(put(ENTITY_API_URL_ID, ghe.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ghe)))
            .andExpect(status().isBadRequest());

        // Validate the Ghe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ghe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ghe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ghe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ghe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGheMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ghe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ghe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGheWithPatch() throws Exception {
        // Initialize the database
        insertedGhe = gheRepository.saveAndFlush(ghe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ghe using partial update
        Ghe partialUpdatedGhe = new Ghe();
        partialUpdatedGhe.setId(ghe.getId());

        partialUpdatedGhe.tenGhe(UPDATED_TEN_GHE);

        restGheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGhe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGhe))
            )
            .andExpect(status().isOk());

        // Validate the Ghe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGheUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedGhe, ghe), getPersistedGhe(ghe));
    }

    @Test
    @Transactional
    void fullUpdateGheWithPatch() throws Exception {
        // Initialize the database
        insertedGhe = gheRepository.saveAndFlush(ghe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ghe using partial update
        Ghe partialUpdatedGhe = new Ghe();
        partialUpdatedGhe.setId(ghe.getId());

        partialUpdatedGhe.tenGhe(UPDATED_TEN_GHE).tinhTrang(UPDATED_TINH_TRANG);

        restGheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGhe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGhe))
            )
            .andExpect(status().isOk());

        // Validate the Ghe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGheUpdatableFieldsEquals(partialUpdatedGhe, getPersistedGhe(partialUpdatedGhe));
    }

    @Test
    @Transactional
    void patchNonExistingGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ghe.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGheMockMvc
            .perform(patch(ENTITY_API_URL_ID, ghe.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ghe)))
            .andExpect(status().isBadRequest());

        // Validate the Ghe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ghe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ghe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ghe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGhe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ghe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGheMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ghe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ghe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGhe() throws Exception {
        // Initialize the database
        insertedGhe = gheRepository.saveAndFlush(ghe);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ghe
        restGheMockMvc.perform(delete(ENTITY_API_URL_ID, ghe.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return gheRepository.count();
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

    protected Ghe getPersistedGhe(Ghe ghe) {
        return gheRepository.findById(ghe.getId()).orElseThrow();
    }

    protected void assertPersistedGheToMatchAllProperties(Ghe expectedGhe) {
        assertGheAllPropertiesEquals(expectedGhe, getPersistedGhe(expectedGhe));
    }

    protected void assertPersistedGheToMatchUpdatableProperties(Ghe expectedGhe) {
        assertGheAllUpdatablePropertiesEquals(expectedGhe, getPersistedGhe(expectedGhe));
    }
}
