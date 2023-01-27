import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SeatControl from './seat-control';
import SeatControlDetail from './seat-control-detail';
import SeatControlUpdate from './seat-control-update';
import SeatControlDeleteDialog from './seat-control-delete-dialog';

const SeatControlRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SeatControl />} />
    <Route path="new" element={<SeatControlUpdate />} />
    <Route path=":id">
      <Route index element={<SeatControlDetail />} />
      <Route path="edit" element={<SeatControlUpdate />} />
      <Route path="delete" element={<SeatControlDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SeatControlRoutes;
