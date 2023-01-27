import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './reserved-seat.reducer';

export const ReservedSeatDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const reservedSeatEntity = useAppSelector(state => state.reservedSeat.entity);
  const updateSuccess = useAppSelector(state => state.reservedSeat.updateSuccess);

  const handleClose = () => {
    navigate('/reserved-seat' + location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(reservedSeatEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="reservedSeatDeleteDialogHeading">
        削除の確認
      </ModalHeader>
      <ModalBody id="jhipsterSamplesApp.reservedSeat.delete.question">
        Reserved Seat {reservedSeatEntity.id}を削除してもよろしいですか？
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; キャンセル
        </Button>
        <Button id="jhi-confirm-delete-reservedSeat" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; 削除
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default ReservedSeatDeleteDialog;
