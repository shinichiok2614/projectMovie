package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Phong.
 */
@Entity
@Table(name = "phong")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Phong implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ten_phong", nullable = false)
    private String tenPhong;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "phong")
    @JsonIgnoreProperties(value = { "phong", "ve", "loaiGhe" }, allowSetters = true)
    private Set<Ghe> ghesPhongs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "phong")
    @JsonIgnoreProperties(value = { "ves", "phong", "phim" }, allowSetters = true)
    private Set<SuatChieu> suatChieusPhongs = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "phongs", "cumRap" }, allowSetters = true)
    private Rap rap;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Phong id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenPhong() {
        return this.tenPhong;
    }

    public Phong tenPhong(String tenPhong) {
        this.setTenPhong(tenPhong);
        return this;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public Set<Ghe> getGhesPhongs() {
        return this.ghesPhongs;
    }

    public void setGhesPhongs(Set<Ghe> ghes) {
        if (this.ghesPhongs != null) {
            this.ghesPhongs.forEach(i -> i.setPhong(null));
        }
        if (ghes != null) {
            ghes.forEach(i -> i.setPhong(this));
        }
        this.ghesPhongs = ghes;
    }

    public Phong ghesPhongs(Set<Ghe> ghes) {
        this.setGhesPhongs(ghes);
        return this;
    }

    public Phong addGhesPhong(Ghe ghe) {
        this.ghesPhongs.add(ghe);
        ghe.setPhong(this);
        return this;
    }

    public Phong removeGhesPhong(Ghe ghe) {
        this.ghesPhongs.remove(ghe);
        ghe.setPhong(null);
        return this;
    }

    public Set<SuatChieu> getSuatChieusPhongs() {
        return this.suatChieusPhongs;
    }

    public void setSuatChieusPhongs(Set<SuatChieu> suatChieus) {
        if (this.suatChieusPhongs != null) {
            this.suatChieusPhongs.forEach(i -> i.setPhong(null));
        }
        if (suatChieus != null) {
            suatChieus.forEach(i -> i.setPhong(this));
        }
        this.suatChieusPhongs = suatChieus;
    }

    public Phong suatChieusPhongs(Set<SuatChieu> suatChieus) {
        this.setSuatChieusPhongs(suatChieus);
        return this;
    }

    public Phong addSuatChieusPhong(SuatChieu suatChieu) {
        this.suatChieusPhongs.add(suatChieu);
        suatChieu.setPhong(this);
        return this;
    }

    public Phong removeSuatChieusPhong(SuatChieu suatChieu) {
        this.suatChieusPhongs.remove(suatChieu);
        suatChieu.setPhong(null);
        return this;
    }

    public Rap getRap() {
        return this.rap;
    }

    public void setRap(Rap rap) {
        this.rap = rap;
    }

    public Phong rap(Rap rap) {
        this.setRap(rap);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Phong)) {
            return false;
        }
        return getId() != null && getId().equals(((Phong) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Phong{" +
            "id=" + getId() +
            ", tenPhong='" + getTenPhong() + "'" +
            "}";
    }
}
