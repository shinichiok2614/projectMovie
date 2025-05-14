import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CumRap from './cum-rap';
import CumRapDetail from './cum-rap-detail';
import CumRapUpdate from './cum-rap-update';
import CumRapDeleteDialog from './cum-rap-delete-dialog';

const CumRapRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CumRap />} />
    <Route path="new" element={<CumRapUpdate />} />
    <Route path=":id">
      <Route index element={<CumRapDetail />} />
      <Route path="edit" element={<CumRapUpdate />} />
      <Route path="delete" element={<CumRapDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CumRapRoutes;
