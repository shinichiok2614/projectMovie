package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.VeAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Ve;
import com.mycompany.myapp.domain.enumeration.TinhTrangVe;
import com.mycompany.myapp.repository.VeRepository;
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
 * Integration tests for the {@link VeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VeResourceIT {

    private static final String DEFAULT_SO_DIEN_THOAI = "AAAAAAAAAA";
    private static final String UPDATED_SO_DIEN_THOAI = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_GIA_TIEN = 1;
    private static final Integer UPDATED_GIA_TIEN = 2;

    private static final TinhTrangVe DEFAULT_TINH_TRANG = TinhTrangVe.CHUA_THANH_TOAN;
    private static final TinhTrangVe UPDATED_TINH_TRANG = TinhTrangVe.DA_THANH_TOAN;

    private static final String ENTITY_API_URL = "/api/ves";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VeRepository veRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVeMockMvc;

    private Ve ve;

    private Ve insertedVe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ve createEntity(EntityManager em) {
        Ve ve = new Ve().soDienThoai(DEFAULT_SO_DIEN_THOAI).email(DEFAULT_EMAIL).giaTien(DEFAULT_GIA_TIEN).tinhTrang(DEFAULT_TINH_TRANG);
        return ve;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ve createUpdatedEntity(EntityManager em) {
        Ve ve = new Ve().soDienThoai(UPDATED_SO_DIEN_THOAI).email(UPDATED_EMAIL).giaTien(UPDATED_GIA_TIEN).tinhTrang(UPDATED_TINH_TRANG);
        return ve;
    }

    @BeforeEach
    public void initTest() {
        ve = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedVe != null) {
            veRepository.delete(insertedVe);
            insertedVe = null;
        }
    }

    @Test
    @Transactional
    void createVe() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Ve
        var returnedVe = om.readValue(
            restVeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ve)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Ve.class
        );

        // Validate the Ve in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVeUpdatableFieldsEquals(returnedVe, getPersistedVe(returnedVe));

        insertedVe = returnedVe;
    }

    @Test
    @Transactional
    void createVeWithExistingId() throws Exception {
        // Create the Ve with an existing ID
        ve.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ve)))
            .andExpect(status().isBadRequest());

        // Validate the Ve in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSoDienThoaiIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ve.setSoDienThoai(null);

        // Create the Ve, which fails.

        restVeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ve)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGiaTienIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ve.setGiaTien(null);

        // Create the Ve, which fails.

        restVeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ve)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTinhTrangIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ve.setTinhTrang(null);

        // Create the Ve, which fails.

        restVeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ve)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVes() throws Exception {
        // Initialize the database
        insertedVe = veRepository.saveAndFlush(ve);

        // Get all the veList
        restVeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ve.getId().intValue())))
            .andExpect(jsonPath("$.[*].soDienThoai").value(hasItem(DEFAULT_SO_DIEN_THOAI)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].giaTien").value(hasItem(DEFAULT_GIA_TIEN)))
            .andExpect(jsonPath("$.[*].tinhTrang").value(hasItem(DEFAULT_TINH_TRANG.toString())));
    }

    @Test
    @Transactional
    void getVe() throws Exception {
        // Initialize the database
        insertedVe = veRepository.saveAndFlush(ve);

        // Get the ve
        restVeMockMvc
            .perform(get(ENTITY_API_URL_ID, ve.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ve.getId().intValue()))
            .andExpect(jsonPath("$.soDienThoai").value(DEFAULT_SO_DIEN_THOAI))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.giaTien").value(DEFAULT_GIA_TIEN))
            .andExpect(jsonPath("$.tinhTrang").value(DEFAULT_TINH_TRANG.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVe() throws Exception {
        // Get the ve
        restVeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVe() throws Exception {
        // Initialize the database
        insertedVe = veRepository.saveAndFlush(ve);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ve
        Ve updatedVe = veRepository.findById(ve.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVe are not directly saved in db
        em.detach(updatedVe);
        updatedVe.soDienThoai(UPDATED_SO_DIEN_THOAI).email(UPDATED_EMAIL).giaTien(UPDATED_GIA_TIEN).tinhTrang(UPDATED_TINH_TRANG);

        restVeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVe.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedVe))
            )
            .andExpect(status().isOk());

        // Validate the Ve in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVeToMatchAllProperties(updatedVe);
    }

    @Test
    @Transactional
    void putNonExistingVe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ve.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVeMockMvc
            .perform(put(ENTITY_API_URL_ID, ve.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ve)))
            .andExpect(status().isBadRequest());

        // Validate the Ve in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ve.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ve))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ve in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ve.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ve)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ve in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVeWithPatch() throws Exception {
        // Initialize the database
        insertedVe = veRepository.saveAndFlush(ve);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ve using partial update
        Ve partialUpdatedVe = new Ve();
        partialUpdatedVe.setId(ve.getId());

        partialUpdatedVe.email(UPDATED_EMAIL).giaTien(UPDATED_GIA_TIEN);

        restVeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVe))
            )
            .andExpect(status().isOk());

        // Validate the Ve in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVe, ve), getPersistedVe(ve));
    }

    @Test
    @Transactional
    void fullUpdateVeWithPatch() throws Exception {
        // Initialize the database
        insertedVe = veRepository.saveAndFlush(ve);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ve using partial update
        Ve partialUpdatedVe = new Ve();
        partialUpdatedVe.setId(ve.getId());

        partialUpdatedVe.soDienThoai(UPDATED_SO_DIEN_THOAI).email(UPDATED_EMAIL).giaTien(UPDATED_GIA_TIEN).tinhTrang(UPDATED_TINH_TRANG);

        restVeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVe))
            )
            .andExpect(status().isOk());

        // Validate the Ve in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVeUpdatableFieldsEquals(partialUpdatedVe, getPersistedVe(partialUpdatedVe));
    }

    @Test
    @Transactional
    void patchNonExistingVe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ve.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVeMockMvc
            .perform(patch(ENTITY_API_URL_ID, ve.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ve)))
            .andExpect(status().isBadRequest());

        // Validate the Ve in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ve.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ve))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ve in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ve.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ve)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ve in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVe() throws Exception {
        // Initialize the database
        insertedVe = veRepository.saveAndFlush(ve);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ve
        restVeMockMvc.perform(delete(ENTITY_API_URL_ID, ve.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return veRepository.count();
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

    protected Ve getPersistedVe(Ve ve) {
        return veRepository.findById(ve.getId()).orElseThrow();
    }

    protected void assertPersistedVeToMatchAllProperties(Ve expectedVe) {
        assertVeAllPropertiesEquals(expectedVe, getPersistedVe(expectedVe));
    }

    protected void assertPersistedVeToMatchUpdatableProperties(Ve expectedVe) {
        assertVeAllUpdatablePropertiesEquals(expectedVe, getPersistedVe(expectedVe));
    }
}
