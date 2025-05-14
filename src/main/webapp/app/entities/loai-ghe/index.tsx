import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import LoaiGhe from './loai-ghe';
import LoaiGheDetail from './loai-ghe-detail';
import LoaiGheUpdate from './loai-ghe-update';
import LoaiGheDeleteDialog from './loai-ghe-delete-dialog';

const LoaiGheRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<LoaiGhe />} />
    <Route path="new" element={<LoaiGheUpdate />} />
    <Route path=":id">
      <Route index element={<LoaiGheDetail />} />
      <Route path="edit" element={<LoaiGheUpdate />} />
      <Route path="delete" element={<LoaiGheDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LoaiGheRoutes;
