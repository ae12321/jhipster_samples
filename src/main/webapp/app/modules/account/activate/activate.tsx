import React, { useEffect } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { Row, Col, Alert } from 'reactstrap';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { activateAction, reset } from './activate.reducer';

const successAlert = (
  <Alert color="success">
    <strong>あなたのアカウントはアクティベートされました。</strong>
    <Link to="/login" className="alert-link">
      認証
    </Link>
    .
  </Alert>
);

const failureAlert = (
  <Alert color="danger">
    <strong>あなたのアカウントはアクティベートされませんでした。</strong>
    サインアップするために登録フォームを使ってサインアップしてください。
  </Alert>
);

export const ActivatePage = () => {
  const dispatch = useAppDispatch();

  const [searchParams] = useSearchParams();

  useEffect(() => {
    const key = searchParams.get('key');

    dispatch(activateAction(key));
    return () => {
      dispatch(reset());
    };
  }, []);

  const { activationSuccess, activationFailure } = useAppSelector(state => state.activate);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>アクティベーション</h1>
          {activationSuccess ? successAlert : undefined}
          {activationFailure ? failureAlert : undefined}
        </Col>
      </Row>
    </div>
  );
};

export default ActivatePage;
