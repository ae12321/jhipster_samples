import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ReservedSeat from './reserved-seat';
import ReservedSeatDetail from './reserved-seat-detail';
import ReservedSeatUpdate from './reserved-seat-update';
import ReservedSeatDeleteDialog from './reserved-seat-delete-dialog';

const ReservedSeatRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ReservedSeat />} />
    <Route path="new" element={<ReservedSeatUpdate />} />
    <Route path=":id">
      <Route index element={<ReservedSeatDetail />} />
      <Route path="edit" element={<ReservedSeatUpdate />} />
      <Route path="delete" element={<ReservedSeatDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ReservedSeatRoutes;
