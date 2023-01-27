import representative from 'app/entities/representative/representative.reducer';
import reservedSeat from 'app/entities/reserved-seat/reserved-seat.reducer';
import seat from 'app/entities/seat/seat.reducer';
import seatGroup from 'app/entities/seat-group/seat-group.reducer';
import seatControl from 'app/entities/seat-control/seat-control.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  representative,
  reservedSeat,
  seat,
  seatGroup,
  seatControl,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
