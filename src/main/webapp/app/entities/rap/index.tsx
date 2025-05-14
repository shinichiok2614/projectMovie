import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Rap from './rap';
import RapDetail from './rap-detail';
import RapUpdate from './rap-update';
import RapDeleteDialog from './rap-delete-dialog';

const RapRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Rap />} />
    <Route path="new" element={<RapUpdate />} />
    <Route path=":id">
      <Route index element={<RapDetail />} />
      <Route path="edit" element={<RapUpdate />} />
      <Route path="delete" element={<RapDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RapRoutes;
