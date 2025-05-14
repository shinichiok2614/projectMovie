import phim from 'app/entities/phim/phim.reducer';
import suatChieu from 'app/entities/suat-chieu/suat-chieu.reducer';
import cumRap from 'app/entities/cum-rap/cum-rap.reducer';
import rap from 'app/entities/rap/rap.reducer';
import phong from 'app/entities/phong/phong.reducer';
import ghe from 'app/entities/ghe/ghe.reducer';
import loaiGhe from 'app/entities/loai-ghe/loai-ghe.reducer';
import bapNuoc from 'app/entities/bap-nuoc/bap-nuoc.reducer';
import danhSachGhe from 'app/entities/danh-sach-ghe/danh-sach-ghe.reducer';
import danhSachBapNuoc from 'app/entities/danh-sach-bap-nuoc/danh-sach-bap-nuoc.reducer';
import ve from 'app/entities/ve/ve.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  phim,
  suatChieu,
  cumRap,
  rap,
  phong,
  ghe,
  loaiGhe,
  bapNuoc,
  danhSachGhe,
  danhSachBapNuoc,
  ve,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
