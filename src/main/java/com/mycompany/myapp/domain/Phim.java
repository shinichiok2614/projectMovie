package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.TheLoai;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Phim.
 */
@Entity
@Table(name = "phim")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Phim implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "ten_phim", nullable = false)
    private String tenPhim;

    @NotNull
    @Column(name = "thoi_luong", nullable = false)
    private Integer thoiLuong;

    @Column(name = "gioi_thieu")
    private String gioiThieu;

    @Column(name = "ngay_cong_chieu")
    private LocalDate ngayCongChieu;

    @Column(name = "link_trailer")
    private String linkTrailer;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @Column(name = "do_tuoi")
    private String doTuoi;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "the_loai", nullable = false)
    private TheLoai theLoai;

    @Column(name = "dinh_dang")
    private String dinhDang;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "phim")
    @JsonIgnoreProperties(value = { "ves", "phong", "phim" }, allowSetters = true)
    private Set<SuatChieu> suatChieusPhims = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Phim id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenPhim() {
        return this.tenPhim;
    }

    public Phim tenPhim(String tenPhim) {
        this.setTenPhim(tenPhim);
        return this;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public Integer getThoiLuong() {
        return this.thoiLuong;
    }

    public Phim thoiLuong(Integer thoiLuong) {
        this.setThoiLuong(thoiLuong);
        return this;
    }

    public void setThoiLuong(Integer thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getGioiThieu() {
        return this.gioiThieu;
    }

    public Phim gioiThieu(String gioiThieu) {
        this.setGioiThieu(gioiThieu);
        return this;
    }

    public void setGioiThieu(String gioiThieu) {
        this.gioiThieu = gioiThieu;
    }

    public LocalDate getNgayCongChieu() {
        return this.ngayCongChieu;
    }

    public Phim ngayCongChieu(LocalDate ngayCongChieu) {
        this.setNgayCongChieu(ngayCongChieu);
        return this;
    }

    public void setNgayCongChieu(LocalDate ngayCongChieu) {
        this.ngayCongChieu = ngayCongChieu;
    }

    public String getLinkTrailer() {
        return this.linkTrailer;
    }

    public Phim linkTrailer(String linkTrailer) {
        this.setLinkTrailer(linkTrailer);
        return this;
    }

    public void setLinkTrailer(String linkTrailer) {
        this.linkTrailer = linkTrailer;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Phim logo(byte[] logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public Phim logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public String getDoTuoi() {
        return this.doTuoi;
    }

    public Phim doTuoi(String doTuoi) {
        this.setDoTuoi(doTuoi);
        return this;
    }

    public void setDoTuoi(String doTuoi) {
        this.doTuoi = doTuoi;
    }

    public TheLoai getTheLoai() {
        return this.theLoai;
    }

    public Phim theLoai(TheLoai theLoai) {
        this.setTheLoai(theLoai);
        return this;
    }

    public void setTheLoai(TheLoai theLoai) {
        this.theLoai = theLoai;
    }

    public String getDinhDang() {
        return this.dinhDang;
    }

    public Phim dinhDang(String dinhDang) {
        this.setDinhDang(dinhDang);
        return this;
    }

    public void setDinhDang(String dinhDang) {
        this.dinhDang = dinhDang;
    }

    public Set<SuatChieu> getSuatChieusPhims() {
        return this.suatChieusPhims;
    }

    public void setSuatChieusPhims(Set<SuatChieu> suatChieus) {
        if (this.suatChieusPhims != null) {
            this.suatChieusPhims.forEach(i -> i.setPhim(null));
        }
        if (suatChieus != null) {
            suatChieus.forEach(i -> i.setPhim(this));
        }
        this.suatChieusPhims = suatChieus;
    }

    public Phim suatChieusPhims(Set<SuatChieu> suatChieus) {
        this.setSuatChieusPhims(suatChieus);
        return this;
    }

    public Phim addSuatChieusPhim(SuatChieu suatChieu) {
        this.suatChieusPhims.add(suatChieu);
        suatChieu.setPhim(this);
        return this;
    }

    public Phim removeSuatChieusPhim(SuatChieu suatChieu) {
        this.suatChieusPhims.remove(suatChieu);
        suatChieu.setPhim(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Phim)) {
            return false;
        }
        return getId() != null && getId().equals(((Phim) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Phim{" +
            "id=" + getId() +
            ", tenPhim='" + getTenPhim() + "'" +
            ", thoiLuong=" + getThoiLuong() +
            ", gioiThieu='" + getGioiThieu() + "'" +
            ", ngayCongChieu='" + getNgayCongChieu() + "'" +
            ", linkTrailer='" + getLinkTrailer() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            ", doTuoi='" + getDoTuoi() + "'" +
            ", theLoai='" + getTheLoai() + "'" +
            ", dinhDang='" + getDinhDang() + "'" +
            "}";
    }
}
