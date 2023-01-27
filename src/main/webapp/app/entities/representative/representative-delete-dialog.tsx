import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './representative.reducer';

export const RepresentativeDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const representativeEntity = useAppSelector(state => state.representative.entity);
  const updateSuccess = useAppSelector(state => state.representative.updateSuccess);

  const handleClose = () => {
    navigate('/representative' + location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(representativeEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="representativeDeleteDialogHeading">
        削除の確認
      </ModalHeader>
      <ModalBody id="jhipsterSamplesApp.representative.delete.question">
        Representative {representativeEntity.id}を削除してもよろしいですか？
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; キャンセル
        </Button>
        <Button id="jhi-confirm-delete-representative" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; 削除
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default RepresentativeDeleteDialog;
