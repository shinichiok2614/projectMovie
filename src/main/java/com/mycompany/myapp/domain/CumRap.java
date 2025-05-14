package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A CumRap.
 */
@Entity
@Table(name = "cum_rap")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CumRap implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ten_cum_rap", nullable = false)
    private String tenCumRap;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cumRap")
    @JsonIgnoreProperties(value = { "phongs", "cumRap" }, allowSetters = true)
    private Set<Rap> raps = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CumRap id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenCumRap() {
        return this.tenCumRap;
    }

    public CumRap tenCumRap(String tenCumRap) {
        this.setTenCumRap(tenCumRap);
        return this;
    }

    public void setTenCumRap(String tenCumRap) {
        this.tenCumRap = tenCumRap;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public CumRap logo(byte[] logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public CumRap logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public Set<Rap> getRaps() {
        return this.raps;
    }

    public void setRaps(Set<Rap> raps) {
        if (this.raps != null) {
            this.raps.forEach(i -> i.setCumRap(null));
        }
        if (raps != null) {
            raps.forEach(i -> i.setCumRap(this));
        }
        this.raps = raps;
    }

    public CumRap raps(Set<Rap> raps) {
        this.setRaps(raps);
        return this;
    }

    public CumRap addRaps(Rap rap) {
        this.raps.add(rap);
        rap.setCumRap(this);
        return this;
    }

    public CumRap removeRaps(Rap rap) {
        this.raps.remove(rap);
        rap.setCumRap(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CumRap)) {
            return false;
        }
        return getId() != null && getId().equals(((CumRap) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CumRap{" +
            "id=" + getId() +
            ", tenCumRap='" + getTenCumRap() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            "}";
    }
}
