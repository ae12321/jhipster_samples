import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Representative from './representative';
import ReservedSeat from './reserved-seat';
import Seat from './seat';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="representative/*" element={<Representative />} />
        <Route path="reserved-seat/*" element={<ReservedSeat />} />
        <Route path="seat/*" element={<Seat />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
