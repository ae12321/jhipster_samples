import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MainUser from './main-user';
import MainUserDetail from './main-user-detail';
import MainUserUpdate from './main-user-update';
import MainUserDeleteDialog from './main-user-delete-dialog';

const MainUserRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MainUser />} />
    <Route path="new" element={<MainUserUpdate />} />
    <Route path=":id">
      <Route index element={<MainUserDetail />} />
      <Route path="edit" element={<MainUserUpdate />} />
      <Route path="delete" element={<MainUserDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MainUserRoutes;
