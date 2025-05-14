import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import DanhSachGhe from './danh-sach-ghe';
import DanhSachGheDetail from './danh-sach-ghe-detail';
import DanhSachGheUpdate from './danh-sach-ghe-update';
import DanhSachGheDeleteDialog from './danh-sach-ghe-delete-dialog';

const DanhSachGheRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<DanhSachGhe />} />
    <Route path="new" element={<DanhSachGheUpdate />} />
    <Route path=":id">
      <Route index element={<DanhSachGheDetail />} />
      <Route path="edit" element={<DanhSachGheUpdate />} />
      <Route path="delete" element={<DanhSachGheDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DanhSachGheRoutes;
