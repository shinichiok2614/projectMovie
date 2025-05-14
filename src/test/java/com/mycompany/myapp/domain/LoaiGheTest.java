package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.GheTestSamples.*;
import static com.mycompany.myapp.domain.LoaiGheTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LoaiGheTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoaiGhe.class);
        LoaiGhe loaiGhe1 = getLoaiGheSample1();
        LoaiGhe loaiGhe2 = new LoaiGhe();
        assertThat(loaiGhe1).isNotEqualTo(loaiGhe2);

        loaiGhe2.setId(loaiGhe1.getId());
        assertThat(loaiGhe1).isEqualTo(loaiGhe2);

        loaiGhe2 = getLoaiGheSample2();
        assertThat(loaiGhe1).isNotEqualTo(loaiGhe2);
    }

    @Test
    void gheListTest() {
        LoaiGhe loaiGhe = getLoaiGheRandomSampleGenerator();
        Ghe gheBack = getGheRandomSampleGenerator();

        loaiGhe.addGheList(gheBack);
        assertThat(loaiGhe.getGheLists()).containsOnly(gheBack);
        assertThat(gheBack.getLoaiGhe()).isEqualTo(loaiGhe);

        loaiGhe.removeGheList(gheBack);
        assertThat(loaiGhe.getGheLists()).doesNotContain(gheBack);
        assertThat(gheBack.getLoaiGhe()).isNull();

        loaiGhe.gheLists(new HashSet<>(Set.of(gheBack)));
        assertThat(loaiGhe.getGheLists()).containsOnly(gheBack);
        assertThat(gheBack.getLoaiGhe()).isEqualTo(loaiGhe);

        loaiGhe.setGheLists(new HashSet<>());
        assertThat(loaiGhe.getGheLists()).doesNotContain(gheBack);
        assertThat(gheBack.getLoaiGhe()).isNull();
    }
}
