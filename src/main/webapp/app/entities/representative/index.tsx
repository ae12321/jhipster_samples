import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Representative from './representative';
import RepresentativeDetail from './representative-detail';
import RepresentativeUpdate from './representative-update';
import RepresentativeDeleteDialog from './representative-delete-dialog';

const RepresentativeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Representative />} />
    <Route path="new" element={<RepresentativeUpdate />} />
    <Route path=":id">
      <Route index element={<RepresentativeDetail />} />
      <Route path="edit" element={<RepresentativeUpdate />} />
      <Route path="delete" element={<RepresentativeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RepresentativeRoutes;
