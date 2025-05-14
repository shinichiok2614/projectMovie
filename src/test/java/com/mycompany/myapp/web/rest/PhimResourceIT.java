package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.PhimAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Phim;
import com.mycompany.myapp.domain.enumeration.TheLoai;
import com.mycompany.myapp.repository.PhimRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PhimResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PhimResourceIT {

    private static final String DEFAULT_TEN_PHIM = "AAAAAAAAAA";
    private static final String UPDATED_TEN_PHIM = "BBBBBBBBBB";

    private static final Integer DEFAULT_THOI_LUONG = 1;
    private static final Integer UPDATED_THOI_LUONG = 2;

    private static final String DEFAULT_GIOI_THIEU = "AAAAAAAAAA";
    private static final String UPDATED_GIOI_THIEU = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_NGAY_CONG_CHIEU = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NGAY_CONG_CHIEU = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LINK_TRAILER = "AAAAAAAAAA";
    private static final String UPDATED_LINK_TRAILER = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DO_TUOI = "AAAAAAAAAA";
    private static final String UPDATED_DO_TUOI = "BBBBBBBBBB";

    private static final TheLoai DEFAULT_THE_LOAI = TheLoai.KINH_DI;
    private static final TheLoai UPDATED_THE_LOAI = TheLoai.TAM_LY;

    private static final String DEFAULT_DINH_DANG = "AAAAAAAAAA";
    private static final String UPDATED_DINH_DANG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/phims";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PhimRepository phimRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhimMockMvc;

    private Phim phim;

    private Phim insertedPhim;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phim createEntity(EntityManager em) {
        Phim phim = new Phim()
            .tenPhim(DEFAULT_TEN_PHIM)
            .thoiLuong(DEFAULT_THOI_LUONG)
            .gioiThieu(DEFAULT_GIOI_THIEU)
            .ngayCongChieu(DEFAULT_NGAY_CONG_CHIEU)
            .linkTrailer(DEFAULT_LINK_TRAILER)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .doTuoi(DEFAULT_DO_TUOI)
            .theLoai(DEFAULT_THE_LOAI)
            .dinhDang(DEFAULT_DINH_DANG);
        return phim;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phim createUpdatedEntity(EntityManager em) {
        Phim phim = new Phim()
            .tenPhim(UPDATED_TEN_PHIM)
            .thoiLuong(UPDATED_THOI_LUONG)
            .gioiThieu(UPDATED_GIOI_THIEU)
            .ngayCongChieu(UPDATED_NGAY_CONG_CHIEU)
            .linkTrailer(UPDATED_LINK_TRAILER)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .doTuoi(UPDATED_DO_TUOI)
            .theLoai(UPDATED_THE_LOAI)
            .dinhDang(UPDATED_DINH_DANG);
        return phim;
    }

    @BeforeEach
    public void initTest() {
        phim = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPhim != null) {
            phimRepository.delete(insertedPhim);
            insertedPhim = null;
        }
    }

    @Test
    @Transactional
    void createPhim() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Phim
        var returnedPhim = om.readValue(
            restPhimMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phim)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Phim.class
        );

        // Validate the Phim in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPhimUpdatableFieldsEquals(returnedPhim, getPersistedPhim(returnedPhim));

        insertedPhim = returnedPhim;
    }

    @Test
    @Transactional
    void createPhimWithExistingId() throws Exception {
        // Create the Phim with an existing ID
        phim.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phim)))
            .andExpect(status().isBadRequest());

        // Validate the Phim in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTenPhimIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        phim.setTenPhim(null);

        // Create the Phim, which fails.

        restPhimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phim)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkThoiLuongIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        phim.setThoiLuong(null);

        // Create the Phim, which fails.

        restPhimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phim)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTheLoaiIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        phim.setTheLoai(null);

        // Create the Phim, which fails.

        restPhimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phim)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPhims() throws Exception {
        // Initialize the database
        insertedPhim = phimRepository.saveAndFlush(phim);

        // Get all the phimList
        restPhimMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phim.getId().intValue())))
            .andExpect(jsonPath("$.[*].tenPhim").value(hasItem(DEFAULT_TEN_PHIM)))
            .andExpect(jsonPath("$.[*].thoiLuong").value(hasItem(DEFAULT_THOI_LUONG)))
            .andExpect(jsonPath("$.[*].gioiThieu").value(hasItem(DEFAULT_GIOI_THIEU)))
            .andExpect(jsonPath("$.[*].ngayCongChieu").value(hasItem(DEFAULT_NGAY_CONG_CHIEU.toString())))
            .andExpect(jsonPath("$.[*].linkTrailer").value(hasItem(DEFAULT_LINK_TRAILER)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].doTuoi").value(hasItem(DEFAULT_DO_TUOI)))
            .andExpect(jsonPath("$.[*].theLoai").value(hasItem(DEFAULT_THE_LOAI.toString())))
            .andExpect(jsonPath("$.[*].dinhDang").value(hasItem(DEFAULT_DINH_DANG)));
    }

    @Test
    @Transactional
    void getPhim() throws Exception {
        // Initialize the database
        insertedPhim = phimRepository.saveAndFlush(phim);

        // Get the phim
        restPhimMockMvc
            .perform(get(ENTITY_API_URL_ID, phim.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(phim.getId().intValue()))
            .andExpect(jsonPath("$.tenPhim").value(DEFAULT_TEN_PHIM))
            .andExpect(jsonPath("$.thoiLuong").value(DEFAULT_THOI_LUONG))
            .andExpect(jsonPath("$.gioiThieu").value(DEFAULT_GIOI_THIEU))
            .andExpect(jsonPath("$.ngayCongChieu").value(DEFAULT_NGAY_CONG_CHIEU.toString()))
            .andExpect(jsonPath("$.linkTrailer").value(DEFAULT_LINK_TRAILER))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64.getEncoder().encodeToString(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.doTuoi").value(DEFAULT_DO_TUOI))
            .andExpect(jsonPath("$.theLoai").value(DEFAULT_THE_LOAI.toString()))
            .andExpect(jsonPath("$.dinhDang").value(DEFAULT_DINH_DANG));
    }

    @Test
    @Transactional
    void getNonExistingPhim() throws Exception {
        // Get the phim
        restPhimMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPhim() throws Exception {
        // Initialize the database
        insertedPhim = phimRepository.saveAndFlush(phim);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the phim
        Phim updatedPhim = phimRepository.findById(phim.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPhim are not directly saved in db
        em.detach(updatedPhim);
        updatedPhim
            .tenPhim(UPDATED_TEN_PHIM)
            .thoiLuong(UPDATED_THOI_LUONG)
            .gioiThieu(UPDATED_GIOI_THIEU)
            .ngayCongChieu(UPDATED_NGAY_CONG_CHIEU)
            .linkTrailer(UPDATED_LINK_TRAILER)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .doTuoi(UPDATED_DO_TUOI)
            .theLoai(UPDATED_THE_LOAI)
            .dinhDang(UPDATED_DINH_DANG);

        restPhimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPhim.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPhim))
            )
            .andExpect(status().isOk());

        // Validate the Phim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPhimToMatchAllProperties(updatedPhim);
    }

    @Test
    @Transactional
    void putNonExistingPhim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phim.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhimMockMvc
            .perform(put(ENTITY_API_URL_ID, phim.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phim)))
            .andExpect(status().isBadRequest());

        // Validate the Phim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPhim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phim.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(phim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPhim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phim.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhimMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phim)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Phim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePhimWithPatch() throws Exception {
        // Initialize the database
        insertedPhim = phimRepository.saveAndFlush(phim);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the phim using partial update
        Phim partialUpdatedPhim = new Phim();
        partialUpdatedPhim.setId(phim.getId());

        partialUpdatedPhim
            .thoiLuong(UPDATED_THOI_LUONG)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .doTuoi(UPDATED_DO_TUOI)
            .theLoai(UPDATED_THE_LOAI)
            .dinhDang(UPDATED_DINH_DANG);

        restPhimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhim.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPhim))
            )
            .andExpect(status().isOk());

        // Validate the Phim in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPhimUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPhim, phim), getPersistedPhim(phim));
    }

    @Test
    @Transactional
    void fullUpdatePhimWithPatch() throws Exception {
        // Initialize the database
        insertedPhim = phimRepository.saveAndFlush(phim);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the phim using partial update
        Phim partialUpdatedPhim = new Phim();
        partialUpdatedPhim.setId(phim.getId());

        partialUpdatedPhim
            .tenPhim(UPDATED_TEN_PHIM)
            .thoiLuong(UPDATED_THOI_LUONG)
            .gioiThieu(UPDATED_GIOI_THIEU)
            .ngayCongChieu(UPDATED_NGAY_CONG_CHIEU)
            .linkTrailer(UPDATED_LINK_TRAILER)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .doTuoi(UPDATED_DO_TUOI)
            .theLoai(UPDATED_THE_LOAI)
            .dinhDang(UPDATED_DINH_DANG);

        restPhimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhim.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPhim))
            )
            .andExpect(status().isOk());

        // Validate the Phim in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPhimUpdatableFieldsEquals(partialUpdatedPhim, getPersistedPhim(partialUpdatedPhim));
    }

    @Test
    @Transactional
    void patchNonExistingPhim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phim.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhimMockMvc
            .perform(patch(ENTITY_API_URL_ID, phim.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(phim)))
            .andExpect(status().isBadRequest());

        // Validate the Phim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPhim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phim.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(phim))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPhim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phim.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhimMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(phim)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Phim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePhim() throws Exception {
        // Initialize the database
        insertedPhim = phimRepository.saveAndFlush(phim);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the phim
        restPhimMockMvc
            .perform(delete(ENTITY_API_URL_ID, phim.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return phimRepository.count();
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

    protected Phim getPersistedPhim(Phim phim) {
        return phimRepository.findById(phim.getId()).orElseThrow();
    }

    protected void assertPersistedPhimToMatchAllProperties(Phim expectedPhim) {
        assertPhimAllPropertiesEquals(expectedPhim, getPersistedPhim(expectedPhim));
    }

    protected void assertPersistedPhimToMatchUpdatableProperties(Phim expectedPhim) {
        assertPhimAllUpdatablePropertiesEquals(expectedPhim, getPersistedPhim(expectedPhim));
    }
}
