package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class DanhSachGheAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDanhSachGheAllPropertiesEquals(DanhSachGhe expected, DanhSachGhe actual) {
        assertDanhSachGheAutoGeneratedPropertiesEquals(expected, actual);
        assertDanhSachGheAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDanhSachGheAllUpdatablePropertiesEquals(DanhSachGhe expected, DanhSachGhe actual) {
        assertDanhSachGheUpdatableFieldsEquals(expected, actual);
        assertDanhSachGheUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDanhSachGheAutoGeneratedPropertiesEquals(DanhSachGhe expected, DanhSachGhe actual) {
        assertThat(expected)
            .as("Verify DanhSachGhe auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDanhSachGheUpdatableFieldsEquals(DanhSachGhe expected, DanhSachGhe actual) {
        assertThat(expected)
            .as("Verify DanhSachGhe relevant properties")
            .satisfies(e -> assertThat(e.getSoDienThoai()).as("check soDienThoai").isEqualTo(actual.getSoDienThoai()))
            .satisfies(e -> assertThat(e.getTenGhe()).as("check tenGhe").isEqualTo(actual.getTenGhe()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDanhSachGheUpdatableRelationshipsEquals(DanhSachGhe expected, DanhSachGhe actual) {}
}
