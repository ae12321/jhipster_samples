import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reserved-seat.reducer';

export const ReservedSeatDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reservedSeatEntity = useAppSelector(state => state.reservedSeat.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reservedSeatDetailsHeading">Reserved Seat</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{reservedSeatEntity.id}</dd>
          <dt>
            <span id="personName">Person Name</span>
          </dt>
          <dd>{reservedSeatEntity.personName}</dd>
          <dt>
            <span id="seatName">Seat Name</span>
          </dt>
          <dd>{reservedSeatEntity.seatName}</dd>
          <dt>Main User</dt>
          <dd>{reservedSeatEntity.mainUser ? reservedSeatEntity.mainUser.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/reserved-seat" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">戻る</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reserved-seat/${reservedSeatEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">編集</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReservedSeatDetail;
