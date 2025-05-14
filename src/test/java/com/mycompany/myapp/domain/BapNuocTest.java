package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.BapNuocTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BapNuocTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BapNuoc.class);
        BapNuoc bapNuoc1 = getBapNuocSample1();
        BapNuoc bapNuoc2 = new BapNuoc();
        assertThat(bapNuoc1).isNotEqualTo(bapNuoc2);

        bapNuoc2.setId(bapNuoc1.getId());
        assertThat(bapNuoc1).isEqualTo(bapNuoc2);

        bapNuoc2 = getBapNuocSample2();
        assertThat(bapNuoc1).isNotEqualTo(bapNuoc2);
    }
}
