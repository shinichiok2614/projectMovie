package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.DanhSachGheTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DanhSachGheTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DanhSachGhe.class);
        DanhSachGhe danhSachGhe1 = getDanhSachGheSample1();
        DanhSachGhe danhSachGhe2 = new DanhSachGhe();
        assertThat(danhSachGhe1).isNotEqualTo(danhSachGhe2);

        danhSachGhe2.setId(danhSachGhe1.getId());
        assertThat(danhSachGhe1).isEqualTo(danhSachGhe2);

        danhSachGhe2 = getDanhSachGheSample2();
        assertThat(danhSachGhe1).isNotEqualTo(danhSachGhe2);
    }
}
