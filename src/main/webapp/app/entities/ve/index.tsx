import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ve from './ve';
import VeDetail from './ve-detail';
import VeUpdate from './ve-update';
import VeDeleteDialog from './ve-delete-dialog';

const VeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Ve />} />
    <Route path="new" element={<VeUpdate />} />
    <Route path=":id">
      <Route index element={<VeDetail />} />
      <Route path="edit" element={<VeUpdate />} />
      <Route path="delete" element={<VeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default VeRoutes;
