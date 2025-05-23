package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class VeAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVeAllPropertiesEquals(Ve expected, Ve actual) {
        assertVeAutoGeneratedPropertiesEquals(expected, actual);
        assertVeAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVeAllUpdatablePropertiesEquals(Ve expected, Ve actual) {
        assertVeUpdatableFieldsEquals(expected, actual);
        assertVeUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVeAutoGeneratedPropertiesEquals(Ve expected, Ve actual) {
        assertThat(expected)
            .as("Verify Ve auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVeUpdatableFieldsEquals(Ve expected, Ve actual) {
        assertThat(expected)
            .as("Verify Ve relevant properties")
            .satisfies(e -> assertThat(e.getSoDienThoai()).as("check soDienThoai").isEqualTo(actual.getSoDienThoai()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getGiaTien()).as("check giaTien").isEqualTo(actual.getGiaTien()))
            .satisfies(e -> assertThat(e.getTinhTrang()).as("check tinhTrang").isEqualTo(actual.getTinhTrang()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertVeUpdatableRelationshipsEquals(Ve expected, Ve actual) {
        assertThat(expected)
            .as("Verify Ve relationships")
            .satisfies(e -> assertThat(e.getSuatChieu()).as("check suatChieu").isEqualTo(actual.getSuatChieu()));
    }
}
