import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Phim from './phim';
import SuatChieu from './suat-chieu';
import CumRap from './cum-rap';
import Rap from './rap';
import Phong from './phong';
import Ghe from './ghe';
import LoaiGhe from './loai-ghe';
import BapNuoc from './bap-nuoc';
import DanhSachGhe from './danh-sach-ghe';
import DanhSachBapNuoc from './danh-sach-bap-nuoc';
import Ve from './ve';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="phim/*" element={<Phim />} />
        <Route path="suat-chieu/*" element={<SuatChieu />} />
        <Route path="cum-rap/*" element={<CumRap />} />
        <Route path="rap/*" element={<Rap />} />
        <Route path="phong/*" element={<Phong />} />
        <Route path="ghe/*" element={<Ghe />} />
        <Route path="loai-ghe/*" element={<LoaiGhe />} />
        <Route path="bap-nuoc/*" element={<BapNuoc />} />
        <Route path="danh-sach-ghe/*" element={<DanhSachGhe />} />
        <Route path="danh-sach-bap-nuoc/*" element={<DanhSachBapNuoc />} />
        <Route path="ve/*" element={<Ve />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
