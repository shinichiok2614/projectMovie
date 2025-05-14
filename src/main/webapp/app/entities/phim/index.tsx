import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Phim from './phim';
import PhimDetail from './phim-detail';
import PhimUpdate from './phim-update';
import PhimDeleteDialog from './phim-delete-dialog';

const PhimRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Phim />} />
    <Route path="new" element={<PhimUpdate />} />
    <Route path=":id">
      <Route index element={<PhimDetail />} />
      <Route path="edit" element={<PhimUpdate />} />
      <Route path="delete" element={<PhimDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PhimRoutes;
