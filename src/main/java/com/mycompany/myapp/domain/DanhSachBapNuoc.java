package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DanhSachBapNuoc.
 */
@Entity
@Table(name = "danh_sach_bap_nuoc")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DanhSachBapNuoc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "so_dien_thoai", nullable = false)
    private String soDienThoai;

    @NotNull
    @Column(name = "ten_bap_nuoc", nullable = false)
    private String tenBapNuoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ghesVes", "danhSachBapNuocs", "suatChieu" }, allowSetters = true)
    private Ve ve;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DanhSachBapNuoc id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSoDienThoai() {
        return this.soDienThoai;
    }

    public DanhSachBapNuoc soDienThoai(String soDienThoai) {
        this.setSoDienThoai(soDienThoai);
        return this;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getTenBapNuoc() {
        return this.tenBapNuoc;
    }

    public DanhSachBapNuoc tenBapNuoc(String tenBapNuoc) {
        this.setTenBapNuoc(tenBapNuoc);
        return this;
    }

    public void setTenBapNuoc(String tenBapNuoc) {
        this.tenBapNuoc = tenBapNuoc;
    }

    public Ve getVe() {
        return this.ve;
    }

    public void setVe(Ve ve) {
        this.ve = ve;
    }

    public DanhSachBapNuoc ve(Ve ve) {
        this.setVe(ve);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DanhSachBapNuoc)) {
            return false;
        }
        return getId() != null && getId().equals(((DanhSachBapNuoc) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DanhSachBapNuoc{" +
            "id=" + getId() +
            ", soDienThoai='" + getSoDienThoai() + "'" +
            ", tenBapNuoc='" + getTenBapNuoc() + "'" +
            "}";
    }
}
