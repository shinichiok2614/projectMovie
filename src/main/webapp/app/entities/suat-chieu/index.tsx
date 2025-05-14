import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SuatChieu from './suat-chieu';
import SuatChieuDetail from './suat-chieu-detail';
import SuatChieuUpdate from './suat-chieu-update';
import SuatChieuDeleteDialog from './suat-chieu-delete-dialog';

const SuatChieuRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SuatChieu />} />
    <Route path="new" element={<SuatChieuUpdate />} />
    <Route path=":id">
      <Route index element={<SuatChieuDetail />} />
      <Route path="edit" element={<SuatChieuUpdate />} />
      <Route path="delete" element={<SuatChieuDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SuatChieuRoutes;
