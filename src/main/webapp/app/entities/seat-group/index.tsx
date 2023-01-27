import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SeatGroup from './seat-group';
import SeatGroupDetail from './seat-group-detail';
import SeatGroupUpdate from './seat-group-update';
import SeatGroupDeleteDialog from './seat-group-delete-dialog';

const SeatGroupRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SeatGroup />} />
    <Route path="new" element={<SeatGroupUpdate />} />
    <Route path=":id">
      <Route index element={<SeatGroupDetail />} />
      <Route path="edit" element={<SeatGroupUpdate />} />
      <Route path="delete" element={<SeatGroupDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SeatGroupRoutes;
