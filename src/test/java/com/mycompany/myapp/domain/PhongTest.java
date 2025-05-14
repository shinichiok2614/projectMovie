package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.GheTestSamples.*;
import static com.mycompany.myapp.domain.PhongTestSamples.*;
import static com.mycompany.myapp.domain.RapTestSamples.*;
import static com.mycompany.myapp.domain.SuatChieuTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PhongTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Phong.class);
        Phong phong1 = getPhongSample1();
        Phong phong2 = new Phong();
        assertThat(phong1).isNotEqualTo(phong2);

        phong2.setId(phong1.getId());
        assertThat(phong1).isEqualTo(phong2);

        phong2 = getPhongSample2();
        assertThat(phong1).isNotEqualTo(phong2);
    }

    @Test
    void ghesPhongTest() {
        Phong phong = getPhongRandomSampleGenerator();
        Ghe gheBack = getGheRandomSampleGenerator();

        phong.addGhesPhong(gheBack);
        assertThat(phong.getGhesPhongs()).containsOnly(gheBack);
        assertThat(gheBack.getPhong()).isEqualTo(phong);

        phong.removeGhesPhong(gheBack);
        assertThat(phong.getGhesPhongs()).doesNotContain(gheBack);
        assertThat(gheBack.getPhong()).isNull();

        phong.ghesPhongs(new HashSet<>(Set.of(gheBack)));
        assertThat(phong.getGhesPhongs()).containsOnly(gheBack);
        assertThat(gheBack.getPhong()).isEqualTo(phong);

        phong.setGhesPhongs(new HashSet<>());
        assertThat(phong.getGhesPhongs()).doesNotContain(gheBack);
        assertThat(gheBack.getPhong()).isNull();
    }

    @Test
    void suatChieusPhongTest() {
        Phong phong = getPhongRandomSampleGenerator();
        SuatChieu suatChieuBack = getSuatChieuRandomSampleGenerator();

        phong.addSuatChieusPhong(suatChieuBack);
        assertThat(phong.getSuatChieusPhongs()).containsOnly(suatChieuBack);
        assertThat(suatChieuBack.getPhong()).isEqualTo(phong);

        phong.removeSuatChieusPhong(suatChieuBack);
        assertThat(phong.getSuatChieusPhongs()).doesNotContain(suatChieuBack);
        assertThat(suatChieuBack.getPhong()).isNull();

        phong.suatChieusPhongs(new HashSet<>(Set.of(suatChieuBack)));
        assertThat(phong.getSuatChieusPhongs()).containsOnly(suatChieuBack);
        assertThat(suatChieuBack.getPhong()).isEqualTo(phong);

        phong.setSuatChieusPhongs(new HashSet<>());
        assertThat(phong.getSuatChieusPhongs()).doesNotContain(suatChieuBack);
        assertThat(suatChieuBack.getPhong()).isNull();
    }

    @Test
    void rapTest() {
        Phong phong = getPhongRandomSampleGenerator();
        Rap rapBack = getRapRandomSampleGenerator();

        phong.setRap(rapBack);
        assertThat(phong.getRap()).isEqualTo(rapBack);

        phong.rap(null);
        assertThat(phong.getRap()).isNull();
    }
}
