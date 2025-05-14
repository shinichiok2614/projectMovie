package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.DanhSachBapNuocTestSamples.*;
import static com.mycompany.myapp.domain.GheTestSamples.*;
import static com.mycompany.myapp.domain.SuatChieuTestSamples.*;
import static com.mycompany.myapp.domain.VeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ve.class);
        Ve ve1 = getVeSample1();
        Ve ve2 = new Ve();
        assertThat(ve1).isNotEqualTo(ve2);

        ve2.setId(ve1.getId());
        assertThat(ve1).isEqualTo(ve2);

        ve2 = getVeSample2();
        assertThat(ve1).isNotEqualTo(ve2);
    }

    @Test
    void ghesVeTest() {
        Ve ve = getVeRandomSampleGenerator();
        Ghe gheBack = getGheRandomSampleGenerator();

        ve.addGhesVe(gheBack);
        assertThat(ve.getGhesVes()).containsOnly(gheBack);
        assertThat(gheBack.getVe()).isEqualTo(ve);

        ve.removeGhesVe(gheBack);
        assertThat(ve.getGhesVes()).doesNotContain(gheBack);
        assertThat(gheBack.getVe()).isNull();

        ve.ghesVes(new HashSet<>(Set.of(gheBack)));
        assertThat(ve.getGhesVes()).containsOnly(gheBack);
        assertThat(gheBack.getVe()).isEqualTo(ve);

        ve.setGhesVes(new HashSet<>());
        assertThat(ve.getGhesVes()).doesNotContain(gheBack);
        assertThat(gheBack.getVe()).isNull();
    }

    @Test
    void danhSachBapNuocTest() {
        Ve ve = getVeRandomSampleGenerator();
        DanhSachBapNuoc danhSachBapNuocBack = getDanhSachBapNuocRandomSampleGenerator();

        ve.addDanhSachBapNuoc(danhSachBapNuocBack);
        assertThat(ve.getDanhSachBapNuocs()).containsOnly(danhSachBapNuocBack);
        assertThat(danhSachBapNuocBack.getVe()).isEqualTo(ve);

        ve.removeDanhSachBapNuoc(danhSachBapNuocBack);
        assertThat(ve.getDanhSachBapNuocs()).doesNotContain(danhSachBapNuocBack);
        assertThat(danhSachBapNuocBack.getVe()).isNull();

        ve.danhSachBapNuocs(new HashSet<>(Set.of(danhSachBapNuocBack)));
        assertThat(ve.getDanhSachBapNuocs()).containsOnly(danhSachBapNuocBack);
        assertThat(danhSachBapNuocBack.getVe()).isEqualTo(ve);

        ve.setDanhSachBapNuocs(new HashSet<>());
        assertThat(ve.getDanhSachBapNuocs()).doesNotContain(danhSachBapNuocBack);
        assertThat(danhSachBapNuocBack.getVe()).isNull();
    }

    @Test
    void suatChieuTest() {
        Ve ve = getVeRandomSampleGenerator();
        SuatChieu suatChieuBack = getSuatChieuRandomSampleGenerator();

        ve.setSuatChieu(suatChieuBack);
        assertThat(ve.getSuatChieu()).isEqualTo(suatChieuBack);

        ve.suatChieu(null);
        assertThat(ve.getSuatChieu()).isNull();
    }
}
