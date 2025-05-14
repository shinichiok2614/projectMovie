package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.GheTestSamples.*;
import static com.mycompany.myapp.domain.LoaiGheTestSamples.*;
import static com.mycompany.myapp.domain.PhongTestSamples.*;
import static com.mycompany.myapp.domain.VeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GheTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ghe.class);
        Ghe ghe1 = getGheSample1();
        Ghe ghe2 = new Ghe();
        assertThat(ghe1).isNotEqualTo(ghe2);

        ghe2.setId(ghe1.getId());
        assertThat(ghe1).isEqualTo(ghe2);

        ghe2 = getGheSample2();
        assertThat(ghe1).isNotEqualTo(ghe2);
    }

    @Test
    void phongTest() {
        Ghe ghe = getGheRandomSampleGenerator();
        Phong phongBack = getPhongRandomSampleGenerator();

        ghe.setPhong(phongBack);
        assertThat(ghe.getPhong()).isEqualTo(phongBack);

        ghe.phong(null);
        assertThat(ghe.getPhong()).isNull();
    }

    @Test
    void veTest() {
        Ghe ghe = getGheRandomSampleGenerator();
        Ve veBack = getVeRandomSampleGenerator();

        ghe.setVe(veBack);
        assertThat(ghe.getVe()).isEqualTo(veBack);

        ghe.ve(null);
        assertThat(ghe.getVe()).isNull();
    }

    @Test
    void loaiGheTest() {
        Ghe ghe = getGheRandomSampleGenerator();
        LoaiGhe loaiGheBack = getLoaiGheRandomSampleGenerator();

        ghe.setLoaiGhe(loaiGheBack);
        assertThat(ghe.getLoaiGhe()).isEqualTo(loaiGheBack);

        ghe.loaiGhe(null);
        assertThat(ghe.getLoaiGhe()).isNull();
    }
}
