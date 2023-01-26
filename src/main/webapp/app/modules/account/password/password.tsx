import React, { useState, useEffect } from 'react';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { Row, Col, Button } from 'reactstrap';
import { toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { savePassword, reset } from './password.reducer';

export const PasswordPage = () => {
  const [password, setPassword] = useState('');
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(reset());
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  const handleValidSubmit = ({ currentPassword, newPassword }) => {
    dispatch(savePassword({ currentPassword, newPassword }));
  };

  const updatePassword = event => setPassword(event.target.value);

  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.password.successMessage);
  const errorMessage = useAppSelector(state => state.password.errorMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    } else if (errorMessage) {
      toast.error(errorMessage);
    }
    dispatch(reset());
  }, [successMessage, errorMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="password-title">
            [<strong>{account.login}</strong>]のパスワード
          </h2>
          <ValidatedForm id="password-form" onSubmit={handleValidSubmit}>
            <ValidatedField
              name="currentPassword"
              label="Current password"
              placeholder="Current password"
              type="password"
              validate={{
                required: { value: true, message: 'パスワードを入力してください。' },
              }}
              data-cy="currentPassword"
            />
            <ValidatedField
              name="newPassword"
              label="新しいパスワード"
              placeholder="新しいパスワード"
              type="password"
              validate={{
                required: { value: true, message: 'パスワードを入力してください。' },
                minLength: { value: 4, message: 'パスワードは4文字以上で入力してください。' },
                maxLength: { value: 50, message: 'パスワードは50文字以下で入力してください。' },
              }}
              onChange={updatePassword}
              data-cy="newPassword"
            />
            <PasswordStrengthBar password={password} />
            <ValidatedField
              name="confirmPassword"
              label="新しいパスワード(確認)"
              placeholder="新しいパスワード(確認)"
              type="password"
              validate={{
                required: { value: true, message: 'パスワード(確認)を入力してください。' },
                minLength: { value: 4, message: 'パスワード(確認)は4文字以上で入力してください。' },
                maxLength: { value: 50, message: 'パスワード(確認)は50文字以下で入力してください。' },
                validate: v => v === password || 'パスワードが一致しません!',
              }}
              data-cy="confirmPassword"
            />
            <Button color="success" type="submit" data-cy="submit">
              保存
            </Button>
          </ValidatedForm>
        </Col>
      </Row>
    </div>
  );
};

export default PasswordPage;
