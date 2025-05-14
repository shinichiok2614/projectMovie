package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A DanhSachGhe.
 */
@Entity
@Table(name = "danh_sach_ghe")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DanhSachGhe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "so_dien_thoai", nullable = false)
    private String soDienThoai;

    @NotNull
    @Column(name = "ten_ghe", nullable = false)
    private String tenGhe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DanhSachGhe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSoDienThoai() {
        return this.soDienThoai;
    }

    public DanhSachGhe soDienThoai(String soDienThoai) {
        this.setSoDienThoai(soDienThoai);
        return this;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getTenGhe() {
        return this.tenGhe;
    }

    public DanhSachGhe tenGhe(String tenGhe) {
        this.setTenGhe(tenGhe);
        return this;
    }

    public void setTenGhe(String tenGhe) {
        this.tenGhe = tenGhe;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DanhSachGhe)) {
            return false;
        }
        return getId() != null && getId().equals(((DanhSachGhe) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DanhSachGhe{" +
            "id=" + getId() +
            ", soDienThoai='" + getSoDienThoai() + "'" +
            ", tenGhe='" + getTenGhe() + "'" +
            "}";
    }
}
