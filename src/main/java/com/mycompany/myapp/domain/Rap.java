package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Rap.
 */
@Entity
@Table(name = "rap")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Rap implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ten_rap", nullable = false)
    private String tenRap;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "thanh_pho")
    private String thanhPho;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rap")
    @JsonIgnoreProperties(value = { "ghesPhongs", "suatChieusPhongs", "rap" }, allowSetters = true)
    private Set<Phong> phongs = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "raps" }, allowSetters = true)
    private CumRap cumRap;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rap id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenRap() {
        return this.tenRap;
    }

    public Rap tenRap(String tenRap) {
        this.setTenRap(tenRap);
        return this;
    }

    public void setTenRap(String tenRap) {
        this.tenRap = tenRap;
    }

    public String getDiaChi() {
        return this.diaChi;
    }

    public Rap diaChi(String diaChi) {
        this.setDiaChi(diaChi);
        return this;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getThanhPho() {
        return this.thanhPho;
    }

    public Rap thanhPho(String thanhPho) {
        this.setThanhPho(thanhPho);
        return this;
    }

    public void setThanhPho(String thanhPho) {
        this.thanhPho = thanhPho;
    }

    public Set<Phong> getPhongs() {
        return this.phongs;
    }

    public void setPhongs(Set<Phong> phongs) {
        if (this.phongs != null) {
            this.phongs.forEach(i -> i.setRap(null));
        }
        if (phongs != null) {
            phongs.forEach(i -> i.setRap(this));
        }
        this.phongs = phongs;
    }

    public Rap phongs(Set<Phong> phongs) {
        this.setPhongs(phongs);
        return this;
    }

    public Rap addPhongs(Phong phong) {
        this.phongs.add(phong);
        phong.setRap(this);
        return this;
    }

    public Rap removePhongs(Phong phong) {
        this.phongs.remove(phong);
        phong.setRap(null);
        return this;
    }

    public CumRap getCumRap() {
        return this.cumRap;
    }

    public void setCumRap(CumRap cumRap) {
        this.cumRap = cumRap;
    }

    public Rap cumRap(CumRap cumRap) {
        this.setCumRap(cumRap);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rap)) {
            return false;
        }
        return getId() != null && getId().equals(((Rap) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rap{" +
            "id=" + getId() +
            ", tenRap='" + getTenRap() + "'" +
            ", diaChi='" + getDiaChi() + "'" +
            ", thanhPho='" + getThanhPho() + "'" +
            "}";
    }
}
