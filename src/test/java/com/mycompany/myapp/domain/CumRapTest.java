package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CumRapTestSamples.*;
import static com.mycompany.myapp.domain.RapTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CumRapTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CumRap.class);
        CumRap cumRap1 = getCumRapSample1();
        CumRap cumRap2 = new CumRap();
        assertThat(cumRap1).isNotEqualTo(cumRap2);

        cumRap2.setId(cumRap1.getId());
        assertThat(cumRap1).isEqualTo(cumRap2);

        cumRap2 = getCumRapSample2();
        assertThat(cumRap1).isNotEqualTo(cumRap2);
    }

    @Test
    void rapsTest() {
        CumRap cumRap = getCumRapRandomSampleGenerator();
        Rap rapBack = getRapRandomSampleGenerator();

        cumRap.addRaps(rapBack);
        assertThat(cumRap.getRaps()).containsOnly(rapBack);
        assertThat(rapBack.getCumRap()).isEqualTo(cumRap);

        cumRap.removeRaps(rapBack);
        assertThat(cumRap.getRaps()).doesNotContain(rapBack);
        assertThat(rapBack.getCumRap()).isNull();

        cumRap.raps(new HashSet<>(Set.of(rapBack)));
        assertThat(cumRap.getRaps()).containsOnly(rapBack);
        assertThat(rapBack.getCumRap()).isEqualTo(cumRap);

        cumRap.setRaps(new HashSet<>());
        assertThat(cumRap.getRaps()).doesNotContain(rapBack);
        assertThat(rapBack.getCumRap()).isNull();
    }
}
