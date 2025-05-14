package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CumRapTestSamples.*;
import static com.mycompany.myapp.domain.PhongTestSamples.*;
import static com.mycompany.myapp.domain.RapTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RapTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rap.class);
        Rap rap1 = getRapSample1();
        Rap rap2 = new Rap();
        assertThat(rap1).isNotEqualTo(rap2);

        rap2.setId(rap1.getId());
        assertThat(rap1).isEqualTo(rap2);

        rap2 = getRapSample2();
        assertThat(rap1).isNotEqualTo(rap2);
    }

    @Test
    void phongsTest() {
        Rap rap = getRapRandomSampleGenerator();
        Phong phongBack = getPhongRandomSampleGenerator();

        rap.addPhongs(phongBack);
        assertThat(rap.getPhongs()).containsOnly(phongBack);
        assertThat(phongBack.getRap()).isEqualTo(rap);

        rap.removePhongs(phongBack);
        assertThat(rap.getPhongs()).doesNotContain(phongBack);
        assertThat(phongBack.getRap()).isNull();

        rap.phongs(new HashSet<>(Set.of(phongBack)));
        assertThat(rap.getPhongs()).containsOnly(phongBack);
        assertThat(phongBack.getRap()).isEqualTo(rap);

        rap.setPhongs(new HashSet<>());
        assertThat(rap.getPhongs()).doesNotContain(phongBack);
        assertThat(phongBack.getRap()).isNull();
    }

    @Test
    void cumRapTest() {
        Rap rap = getRapRandomSampleGenerator();
        CumRap cumRapBack = getCumRapRandomSampleGenerator();

        rap.setCumRap(cumRapBack);
        assertThat(rap.getCumRap()).isEqualTo(cumRapBack);

        rap.cumRap(null);
        assertThat(rap.getCumRap()).isNull();
    }
}
