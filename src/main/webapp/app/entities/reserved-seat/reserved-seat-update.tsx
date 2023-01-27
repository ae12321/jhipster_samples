import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRepresentative } from 'app/shared/model/representative.model';
import { getEntities as getRepresentatives } from 'app/entities/representative/representative.reducer';
import { ISeat } from 'app/shared/model/seat.model';
import { getEntities as getSeats } from 'app/entities/seat/seat.reducer';
import { IReservedSeat } from 'app/shared/model/reserved-seat.model';
import { getEntity, updateEntity, createEntity, reset } from './reserved-seat.reducer';

export const ReservedSeatUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const representatives = useAppSelector(state => state.representative.entities);
  const seats = useAppSelector(state => state.seat.entities);
  const reservedSeatEntity = useAppSelector(state => state.reservedSeat.entity);
  const loading = useAppSelector(state => state.reservedSeat.loading);
  const updating = useAppSelector(state => state.reservedSeat.updating);
  const updateSuccess = useAppSelector(state => state.reservedSeat.updateSuccess);

  const handleClose = () => {
    navigate('/reserved-seat' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getRepresentatives({}));
    dispatch(getSeats({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...reservedSeatEntity,
      ...values,
      representative: representatives.find(it => it.id.toString() === values.representative.toString()),
      seat: seats.find(it => it.id.toString() === values.seat.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...reservedSeatEntity,
          representative: reservedSeatEntity?.representative?.id,
          seat: reservedSeatEntity?.seat?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSamplesApp.reservedSeat.home.createOrEditLabel" data-cy="ReservedSeatCreateUpdateHeading">
            Reserved Seatを追加または編集
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="reserved-seat-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Name"
                id="reserved-seat-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                id="reserved-seat-representative"
                name="representative"
                data-cy="representative"
                label="Representative"
                type="select"
                required
              >
                <option value="" key="0" />
                {representatives
                  ? representatives.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <ValidatedField id="reserved-seat-seat" name="seat" data-cy="seat" label="Seat" type="select" required>
                <option value="" key="0" />
                {seats
                  ? seats.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/reserved-seat" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">戻る</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; 保存
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ReservedSeatUpdate;
