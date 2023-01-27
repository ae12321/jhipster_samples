import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './seat-control.reducer';

export const SeatControlDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const seatControlEntity = useAppSelector(state => state.seatControl.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="seatControlDetailsHeading">Seat Control</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{seatControlEntity.id}</dd>
          <dt>
            <span id="seatId">Seat Id</span>
          </dt>
          <dd>{seatControlEntity.seatId}</dd>
        </dl>
        <Button tag={Link} to="/seat-control" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">戻る</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/seat-control/${seatControlEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">編集</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SeatControlDetail;
