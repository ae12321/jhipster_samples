import React, { useEffect } from 'react';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import { saveAccountSettings, reset } from './settings.reducer';

export const SettingsPage = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.settings.successMessage);

  useEffect(() => {
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    }
  }, [successMessage]);

  const handleValidSubmit = values => {
    dispatch(
      saveAccountSettings({
        ...account,
        ...values,
      })
    );
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="settings-title">
            [<strong>{account.login}</strong>]の設定
          </h2>
          <ValidatedForm id="settings-form" onSubmit={handleValidSubmit} defaultValues={account}>
            <ValidatedField
              name="firstName"
              label="名"
              id="firstName"
              placeholder="名"
              validate={{
                required: { value: true, message: '名を入力してください' },
                minLength: { value: 1, message: '名は1文字以上で入力してください' },
                maxLength: { value: 50, message: '名は50文字以下で入力してください' },
              }}
              data-cy="firstname"
            />
            <ValidatedField
              name="lastName"
              label="姓"
              id="lastName"
              placeholder="姓"
              validate={{
                required: { value: true, message: '姓を入力してください' },
                minLength: { value: 1, message: '姓は1文字以上で入力してください' },
                maxLength: { value: 50, message: '姓は50文字以下で入力してください' },
              }}
              data-cy="lastname"
            />
            <ValidatedField
              name="email"
              label="メールアドレス"
              placeholder="あなたのメールアドレス"
              type="email"
              validate={{
                required: { value: true, message: 'メールアドレスを入力してください。' },
                minLength: { value: 5, message: 'メールアドレスは5文字以上で入力してください。' },
                maxLength: { value: 254, message: 'メールアドレスは50文字以下で入力してください。' },
                validate: v => isEmail(v) || '正しいメールアドレスを入力してください。',
              }}
              data-cy="email"
            />
            <Button color="primary" type="submit" data-cy="submit">
              保存する
            </Button>
          </ValidatedForm>
        </Col>
      </Row>
    </div>
  );
};

export default SettingsPage;
