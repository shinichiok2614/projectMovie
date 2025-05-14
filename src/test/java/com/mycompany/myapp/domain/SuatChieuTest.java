package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.PhimTestSamples.*;
import static com.mycompany.myapp.domain.PhongTestSamples.*;
import static com.mycompany.myapp.domain.SuatChieuTestSamples.*;
import static com.mycompany.myapp.domain.VeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SuatChieuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuatChieu.class);
        SuatChieu suatChieu1 = getSuatChieuSample1();
        SuatChieu suatChieu2 = new SuatChieu();
        assertThat(suatChieu1).isNotEqualTo(suatChieu2);

        suatChieu2.setId(suatChieu1.getId());
        assertThat(suatChieu1).isEqualTo(suatChieu2);

        suatChieu2 = getSuatChieuSample2();
        assertThat(suatChieu1).isNotEqualTo(suatChieu2);
    }

    @Test
    void vesTest() {
        SuatChieu suatChieu = getSuatChieuRandomSampleGenerator();
        Ve veBack = getVeRandomSampleGenerator();

        suatChieu.addVes(veBack);
        assertThat(suatChieu.getVes()).containsOnly(veBack);
        assertThat(veBack.getSuatChieu()).isEqualTo(suatChieu);

        suatChieu.removeVes(veBack);
        assertThat(suatChieu.getVes()).doesNotContain(veBack);
        assertThat(veBack.getSuatChieu()).isNull();

        suatChieu.ves(new HashSet<>(Set.of(veBack)));
        assertThat(suatChieu.getVes()).containsOnly(veBack);
        assertThat(veBack.getSuatChieu()).isEqualTo(suatChieu);

        suatChieu.setVes(new HashSet<>());
        assertThat(suatChieu.getVes()).doesNotContain(veBack);
        assertThat(veBack.getSuatChieu()).isNull();
    }

    @Test
    void phongTest() {
        SuatChieu suatChieu = getSuatChieuRandomSampleGenerator();
        Phong phongBack = getPhongRandomSampleGenerator();

        suatChieu.setPhong(phongBack);
        assertThat(suatChieu.getPhong()).isEqualTo(phongBack);

        suatChieu.phong(null);
        assertThat(suatChieu.getPhong()).isNull();
    }

    @Test
    void phimTest() {
        SuatChieu suatChieu = getSuatChieuRandomSampleGenerator();
        Phim phimBack = getPhimRandomSampleGenerator();

        suatChieu.setPhim(phimBack);
        assertThat(suatChieu.getPhim()).isEqualTo(phimBack);

        suatChieu.phim(null);
        assertThat(suatChieu.getPhim()).isNull();
    }
}
