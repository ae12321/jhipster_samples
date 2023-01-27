import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './seat.reducer';

export const SeatDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const seatEntity = useAppSelector(state => state.seat.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="seatDetailsHeading">Seat</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{seatEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{seatEntity.name}</dd>
          <dt>
            <span id="canSit">Can Sit</span>
          </dt>
          <dd>{seatEntity.canSit ? 'true' : 'false'}</dd>
          <dt>
            <span id="top">Top</span>
          </dt>
          <dd>{seatEntity.top}</dd>
          <dt>
            <span id="left">Left</span>
          </dt>
          <dd>{seatEntity.left}</dd>
          <dt>Seat Group</dt>
          <dd>{seatEntity.seatGroup ? seatEntity.seatGroup.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/seat" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">戻る</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/seat/${seatEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">編集</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SeatDetail;
