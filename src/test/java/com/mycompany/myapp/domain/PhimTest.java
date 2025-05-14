package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.PhimTestSamples.*;
import static com.mycompany.myapp.domain.SuatChieuTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PhimTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Phim.class);
        Phim phim1 = getPhimSample1();
        Phim phim2 = new Phim();
        assertThat(phim1).isNotEqualTo(phim2);

        phim2.setId(phim1.getId());
        assertThat(phim1).isEqualTo(phim2);

        phim2 = getPhimSample2();
        assertThat(phim1).isNotEqualTo(phim2);
    }

    @Test
    void suatChieusPhimTest() {
        Phim phim = getPhimRandomSampleGenerator();
        SuatChieu suatChieuBack = getSuatChieuRandomSampleGenerator();

        phim.addSuatChieusPhim(suatChieuBack);
        assertThat(phim.getSuatChieusPhims()).containsOnly(suatChieuBack);
        assertThat(suatChieuBack.getPhim()).isEqualTo(phim);

        phim.removeSuatChieusPhim(suatChieuBack);
        assertThat(phim.getSuatChieusPhims()).doesNotContain(suatChieuBack);
        assertThat(suatChieuBack.getPhim()).isNull();

        phim.suatChieusPhims(new HashSet<>(Set.of(suatChieuBack)));
        assertThat(phim.getSuatChieusPhims()).containsOnly(suatChieuBack);
        assertThat(suatChieuBack.getPhim()).isEqualTo(phim);

        phim.setSuatChieusPhims(new HashSet<>());
        assertThat(phim.getSuatChieusPhims()).doesNotContain(suatChieuBack);
        assertThat(suatChieuBack.getPhim()).isNull();
    }
}
