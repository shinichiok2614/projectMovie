package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.TinhTrangGhe;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Ghe.
 */
@Entity
@Table(name = "ghe")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ghe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ten_ghe", nullable = false)
    private String tenGhe;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tinh_trang", nullable = false)
    private TinhTrangGhe tinhTrang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ghesPhongs", "suatChieusPhongs", "rap" }, allowSetters = true)
    private Phong phong;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "ghesVes", "danhSachBapNuocs", "suatChieu" }, allowSetters = true)
    private Ve ve;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "gheLists" }, allowSetters = true)
    private LoaiGhe loaiGhe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ghe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenGhe() {
        return this.tenGhe;
    }

    public Ghe tenGhe(String tenGhe) {
        this.setTenGhe(tenGhe);
        return this;
    }

    public void setTenGhe(String tenGhe) {
        this.tenGhe = tenGhe;
    }

    public TinhTrangGhe getTinhTrang() {
        return this.tinhTrang;
    }

    public Ghe tinhTrang(TinhTrangGhe tinhTrang) {
        this.setTinhTrang(tinhTrang);
        return this;
    }

    public void setTinhTrang(TinhTrangGhe tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public Phong getPhong() {
        return this.phong;
    }

    public void setPhong(Phong phong) {
        this.phong = phong;
    }

    public Ghe phong(Phong phong) {
        this.setPhong(phong);
        return this;
    }

    public Ve getVe() {
        return this.ve;
    }

    public void setVe(Ve ve) {
        this.ve = ve;
    }

    public Ghe ve(Ve ve) {
        this.setVe(ve);
        return this;
    }

    public LoaiGhe getLoaiGhe() {
        return this.loaiGhe;
    }

    public void setLoaiGhe(LoaiGhe loaiGhe) {
        this.loaiGhe = loaiGhe;
    }

    public Ghe loaiGhe(LoaiGhe loaiGhe) {
        this.setLoaiGhe(loaiGhe);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ghe)) {
            return false;
        }
        return getId() != null && getId().equals(((Ghe) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ghe{" +
            "id=" + getId() +
            ", tenGhe='" + getTenGhe() + "'" +
            ", tinhTrang='" + getTinhTrang() + "'" +
            "}";
    }
}
