package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A SuatChieu.
 */
@Entity
@Table(name = "suat_chieu")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SuatChieu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ngay_chieu", nullable = false)
    private LocalDate ngayChieu;

    @NotNull
    @Column(name = "gio_chieu", nullable = false)
    private String gioChieu;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "suatChieu")
    @JsonIgnoreProperties(value = { "ghesVes", "danhSachBapNuocs", "suatChieu" }, allowSetters = true)
    private Set<Ve> ves = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ghesPhongs", "suatChieusPhongs", "rap" }, allowSetters = true)
    private Phong phong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "suatChieusPhims" }, allowSetters = true)
    private Phim phim;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SuatChieu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getNgayChieu() {
        return this.ngayChieu;
    }

    public SuatChieu ngayChieu(LocalDate ngayChieu) {
        this.setNgayChieu(ngayChieu);
        return this;
    }

    public void setNgayChieu(LocalDate ngayChieu) {
        this.ngayChieu = ngayChieu;
    }

    public String getGioChieu() {
        return this.gioChieu;
    }

    public SuatChieu gioChieu(String gioChieu) {
        this.setGioChieu(gioChieu);
        return this;
    }

    public void setGioChieu(String gioChieu) {
        this.gioChieu = gioChieu;
    }

    public Set<Ve> getVes() {
        return this.ves;
    }

    public void setVes(Set<Ve> ves) {
        if (this.ves != null) {
            this.ves.forEach(i -> i.setSuatChieu(null));
        }
        if (ves != null) {
            ves.forEach(i -> i.setSuatChieu(this));
        }
        this.ves = ves;
    }

    public SuatChieu ves(Set<Ve> ves) {
        this.setVes(ves);
        return this;
    }

    public SuatChieu addVes(Ve ve) {
        this.ves.add(ve);
        ve.setSuatChieu(this);
        return this;
    }

    public SuatChieu removeVes(Ve ve) {
        this.ves.remove(ve);
        ve.setSuatChieu(null);
        return this;
    }

    public Phong getPhong() {
        return this.phong;
    }

    public void setPhong(Phong phong) {
        this.phong = phong;
    }

    public SuatChieu phong(Phong phong) {
        this.setPhong(phong);
        return this;
    }

    public Phim getPhim() {
        return this.phim;
    }

    public void setPhim(Phim phim) {
        this.phim = phim;
    }

    public SuatChieu phim(Phim phim) {
        this.setPhim(phim);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SuatChieu)) {
            return false;
        }
        return getId() != null && getId().equals(((SuatChieu) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SuatChieu{" +
            "id=" + getId() +
            ", ngayChieu='" + getNgayChieu() + "'" +
            ", gioChieu='" + getGioChieu() + "'" +
            "}";
    }
}
