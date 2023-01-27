import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './representative.reducer';

export const RepresentativeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const representativeEntity = useAppSelector(state => state.representative.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="representativeDetailsHeading">Representative</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{representativeEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{representativeEntity.name}</dd>
          <dt>
            <span id="mail">Mail</span>
          </dt>
          <dd>{representativeEntity.mail}</dd>
          <dt>
            <span id="face">Face</span>
          </dt>
          <dd>{representativeEntity.face}</dd>
          <dt>
            <span id="faceId">Face Id</span>
          </dt>
          <dd>{representativeEntity.faceId}</dd>
          <dt>
            <span id="hash">Hash</span>
          </dt>
          <dd>{representativeEntity.hash}</dd>
        </dl>
        <Button tag={Link} to="/representative" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">戻る</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/representative/${representativeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">編集</span>
        </Button>
      </Col>
    </Row>
  );
};

export default RepresentativeDetail;
