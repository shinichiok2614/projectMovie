import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Phong from './phong';
import PhongDetail from './phong-detail';
import PhongUpdate from './phong-update';
import PhongDeleteDialog from './phong-delete-dialog';

const PhongRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Phong />} />
    <Route path="new" element={<PhongUpdate />} />
    <Route path=":id">
      <Route index element={<PhongDetail />} />
      <Route path="edit" element={<PhongUpdate />} />
      <Route path="delete" element={<PhongDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PhongRoutes;
