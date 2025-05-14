package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A BapNuoc.
 */
@Entity
@Table(name = "bap_nuoc")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BapNuoc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ten_bap_nuoc", nullable = false)
    private String tenBapNuoc;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @NotNull
    @Column(name = "gia_tien", nullable = false)
    private Integer giaTien;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BapNuoc id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenBapNuoc() {
        return this.tenBapNuoc;
    }

    public BapNuoc tenBapNuoc(String tenBapNuoc) {
        this.setTenBapNuoc(tenBapNuoc);
        return this;
    }

    public void setTenBapNuoc(String tenBapNuoc) {
        this.tenBapNuoc = tenBapNuoc;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public BapNuoc logo(byte[] logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public BapNuoc logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public Integer getGiaTien() {
        return this.giaTien;
    }

    public BapNuoc giaTien(Integer giaTien) {
        this.setGiaTien(giaTien);
        return this;
    }

    public void setGiaTien(Integer giaTien) {
        this.giaTien = giaTien;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BapNuoc)) {
            return false;
        }
        return getId() != null && getId().equals(((BapNuoc) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BapNuoc{" +
            "id=" + getId() +
            ", tenBapNuoc='" + getTenBapNuoc() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            ", giaTien=" + getGiaTien() +
            "}";
    }
}
