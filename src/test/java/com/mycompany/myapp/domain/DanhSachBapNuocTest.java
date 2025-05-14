package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.DanhSachBapNuocTestSamples.*;
import static com.mycompany.myapp.domain.VeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DanhSachBapNuocTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DanhSachBapNuoc.class);
        DanhSachBapNuoc danhSachBapNuoc1 = getDanhSachBapNuocSample1();
        DanhSachBapNuoc danhSachBapNuoc2 = new DanhSachBapNuoc();
        assertThat(danhSachBapNuoc1).isNotEqualTo(danhSachBapNuoc2);

        danhSachBapNuoc2.setId(danhSachBapNuoc1.getId());
        assertThat(danhSachBapNuoc1).isEqualTo(danhSachBapNuoc2);

        danhSachBapNuoc2 = getDanhSachBapNuocSample2();
        assertThat(danhSachBapNuoc1).isNotEqualTo(danhSachBapNuoc2);
    }

    @Test
    void veTest() {
        DanhSachBapNuoc danhSachBapNuoc = getDanhSachBapNuocRandomSampleGenerator();
        Ve veBack = getVeRandomSampleGenerator();

        danhSachBapNuoc.setVe(veBack);
        assertThat(danhSachBapNuoc.getVe()).isEqualTo(veBack);

        danhSachBapNuoc.ve(null);
        assertThat(danhSachBapNuoc.getVe()).isNull();
    }
}
