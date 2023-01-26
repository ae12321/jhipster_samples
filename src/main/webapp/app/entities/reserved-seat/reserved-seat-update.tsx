import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMainUser } from 'app/shared/model/main-user.model';
import { getEntities as getMainUsers } from 'app/entities/main-user/main-user.reducer';
import { IReservedSeat } from 'app/shared/model/reserved-seat.model';
import { getEntity, updateEntity, createEntity, reset } from './reserved-seat.reducer';

export const ReservedSeatUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const mainUsers = useAppSelector(state => state.mainUser.entities);
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

    dispatch(getMainUsers({}));
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
      mainUser: mainUsers.find(it => it.id.toString() === values.mainUser.toString()),
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
          mainUser: reservedSeatEntity?.mainUser?.id,
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
              <ValidatedField label="Person Name" id="reserved-seat-personName" name="personName" data-cy="personName" type="text" />
              <ValidatedField label="Seat Name" id="reserved-seat-seatName" name="seatName" data-cy="seatName" type="text" />
              <ValidatedField id="reserved-seat-mainUser" name="mainUser" data-cy="mainUser" label="Main User" type="select" required>
                <option value="" key="0" />
                {mainUsers
                  ? mainUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
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
