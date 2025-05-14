import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ghe from './ghe';
import GheDetail from './ghe-detail';
import GheUpdate from './ghe-update';
import GheDeleteDialog from './ghe-delete-dialog';

const GheRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Ghe />} />
    <Route path="new" element={<GheUpdate />} />
    <Route path=":id">
      <Route index element={<GheDetail />} />
      <Route path="edit" element={<GheUpdate />} />
      <Route path="delete" element={<GheDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GheRoutes;
