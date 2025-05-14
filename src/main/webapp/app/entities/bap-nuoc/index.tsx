import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import BapNuoc from './bap-nuoc';
import BapNuocDetail from './bap-nuoc-detail';
import BapNuocUpdate from './bap-nuoc-update';
import BapNuocDeleteDialog from './bap-nuoc-delete-dialog';

const BapNuocRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BapNuoc />} />
    <Route path="new" element={<BapNuocUpdate />} />
    <Route path=":id">
      <Route index element={<BapNuocDetail />} />
      <Route path="edit" element={<BapNuocUpdate />} />
      <Route path="delete" element={<BapNuocDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BapNuocRoutes;
