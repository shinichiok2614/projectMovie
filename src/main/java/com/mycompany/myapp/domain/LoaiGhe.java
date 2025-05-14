package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A LoaiGhe.
 */
@Entity
@Table(name = "loai_ghe")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoaiGhe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ten_loai", nullable = false)
    private String tenLoai;

    @NotNull
    @Column(name = "gia_tien", nullable = false)
    private Integer giaTien;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "loaiGhe")
    @JsonIgnoreProperties(value = { "phong", "ve", "loaiGhe" }, allowSetters = true)
    private Set<Ghe> gheLists = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LoaiGhe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenLoai() {
        return this.tenLoai;
    }

    public LoaiGhe tenLoai(String tenLoai) {
        this.setTenLoai(tenLoai);
        return this;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public Integer getGiaTien() {
        return this.giaTien;
    }

    public LoaiGhe giaTien(Integer giaTien) {
        this.setGiaTien(giaTien);
        return this;
    }

    public void setGiaTien(Integer giaTien) {
        this.giaTien = giaTien;
    }

    public Set<Ghe> getGheLists() {
        return this.gheLists;
    }

    public void setGheLists(Set<Ghe> ghes) {
        if (this.gheLists != null) {
            this.gheLists.forEach(i -> i.setLoaiGhe(null));
        }
        if (ghes != null) {
            ghes.forEach(i -> i.setLoaiGhe(this));
        }
        this.gheLists = ghes;
    }

    public LoaiGhe gheLists(Set<Ghe> ghes) {
        this.setGheLists(ghes);
        return this;
    }

    public LoaiGhe addGheList(Ghe ghe) {
        this.gheLists.add(ghe);
        ghe.setLoaiGhe(this);
        return this;
    }

    public LoaiGhe removeGheList(Ghe ghe) {
        this.gheLists.remove(ghe);
        ghe.setLoaiGhe(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoaiGhe)) {
            return false;
        }
        return getId() != null && getId().equals(((LoaiGhe) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoaiGhe{" +
            "id=" + getId() +
            ", tenLoai='" + getTenLoai() + "'" +
            ", giaTien=" + getGiaTien() +
            "}";
    }
}
