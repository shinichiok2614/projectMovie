package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.TinhTrangVe;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Ve.
 */
@Entity
@Table(name = "ve")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ve implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "so_dien_thoai", nullable = false)
    private String soDienThoai;

    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "gia_tien", nullable = false)
    private Integer giaTien;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tinh_trang", nullable = false)
    private TinhTrangVe tinhTrang;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ve")
    @JsonIgnoreProperties(value = { "phong", "ve", "loaiGhe" }, allowSetters = true)
    private Set<Ghe> ghesVes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ve")
    @JsonIgnoreProperties(value = { "ve" }, allowSetters = true)
    private Set<DanhSachBapNuoc> danhSachBapNuocs = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ves", "phong", "phim" }, allowSetters = true)
    private SuatChieu suatChieu;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ve id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSoDienThoai() {
        return this.soDienThoai;
    }

    public Ve soDienThoai(String soDienThoai) {
        this.setSoDienThoai(soDienThoai);
        return this;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return this.email;
    }

    public Ve email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGiaTien() {
        return this.giaTien;
    }

    public Ve giaTien(Integer giaTien) {
        this.setGiaTien(giaTien);
        return this;
    }

    public void setGiaTien(Integer giaTien) {
        this.giaTien = giaTien;
    }

    public TinhTrangVe getTinhTrang() {
        return this.tinhTrang;
    }

    public Ve tinhTrang(TinhTrangVe tinhTrang) {
        this.setTinhTrang(tinhTrang);
        return this;
    }

    public void setTinhTrang(TinhTrangVe tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public Set<Ghe> getGhesVes() {
        return this.ghesVes;
    }

    public void setGhesVes(Set<Ghe> ghes) {
        if (this.ghesVes != null) {
            this.ghesVes.forEach(i -> i.setVe(null));
        }
        if (ghes != null) {
            ghes.forEach(i -> i.setVe(this));
        }
        this.ghesVes = ghes;
    }

    public Ve ghesVes(Set<Ghe> ghes) {
        this.setGhesVes(ghes);
        return this;
    }

    public Ve addGhesVe(Ghe ghe) {
        this.ghesVes.add(ghe);
        ghe.setVe(this);
        return this;
    }

    public Ve removeGhesVe(Ghe ghe) {
        this.ghesVes.remove(ghe);
        ghe.setVe(null);
        return this;
    }

    public Set<DanhSachBapNuoc> getDanhSachBapNuocs() {
        return this.danhSachBapNuocs;
    }

    public void setDanhSachBapNuocs(Set<DanhSachBapNuoc> danhSachBapNuocs) {
        if (this.danhSachBapNuocs != null) {
            this.danhSachBapNuocs.forEach(i -> i.setVe(null));
        }
        if (danhSachBapNuocs != null) {
            danhSachBapNuocs.forEach(i -> i.setVe(this));
        }
        this.danhSachBapNuocs = danhSachBapNuocs;
    }

    public Ve danhSachBapNuocs(Set<DanhSachBapNuoc> danhSachBapNuocs) {
        this.setDanhSachBapNuocs(danhSachBapNuocs);
        return this;
    }

    public Ve addDanhSachBapNuoc(DanhSachBapNuoc danhSachBapNuoc) {
        this.danhSachBapNuocs.add(danhSachBapNuoc);
        danhSachBapNuoc.setVe(this);
        return this;
    }

    public Ve removeDanhSachBapNuoc(DanhSachBapNuoc danhSachBapNuoc) {
        this.danhSachBapNuocs.remove(danhSachBapNuoc);
        danhSachBapNuoc.setVe(null);
        return this;
    }

    public SuatChieu getSuatChieu() {
        return this.suatChieu;
    }

    public void setSuatChieu(SuatChieu suatChieu) {
        this.suatChieu = suatChieu;
    }

    public Ve suatChieu(SuatChieu suatChieu) {
        this.setSuatChieu(suatChieu);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ve)) {
            return false;
        }
        return getId() != null && getId().equals(((Ve) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ve{" +
            "id=" + getId() +
            ", soDienThoai='" + getSoDienThoai() + "'" +
            ", email='" + getEmail() + "'" +
            ", giaTien=" + getGiaTien() +
            ", tinhTrang='" + getTinhTrang() + "'" +
            "}";
    }
}
