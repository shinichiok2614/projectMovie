import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DanhSachBapNuoc from './danh-sach-bap-nuoc';
import DanhSachBapNuocDetail from './danh-sach-bap-nuoc-detail';
import DanhSachBapNuocUpdate from './danh-sach-bap-nuoc-update';
import DanhSachBapNuocDeleteDialog from './danh-sach-bap-nuoc-delete-dialog';

const DanhSachBapNuocRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DanhSachBapNuoc />} />
    <Route path="new" element={<DanhSachBapNuocUpdate />} />
    <Route path=":id">
      <Route index element={<DanhSachBapNuocDetail />} />
      <Route path="edit" element={<DanhSachBapNuocUpdate />} />
      <Route path="delete" element={<DanhSachBapNuocDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DanhSachBapNuocRoutes;
