import React, { useState, useEffect } from 'react';
import { ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { Row, Col, Alert, Button } from 'reactstrap';
import { toast } from 'react-toastify';

import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { handleRegister, reset } from './register.reducer';

export const RegisterPage = () => {
  const [password, setPassword] = useState('');
  const dispatch = useAppDispatch();

  useEffect(
    () => () => {
      dispatch(reset());
    },
    []
  );

  const handleValidSubmit = ({ username, email, firstPassword }) => {
    dispatch(handleRegister({ login: username, email, password: firstPassword, langKey: 'en' }));
  };

  const updatePassword = event => setPassword(event.target.value);

  const successMessage = useAppSelector(state => state.register.successMessage);

  useEffect(() => {
    if (successMessage) {
      toast.success(successMessage);
    }
  }, [successMessage]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1 id="register-title" data-cy="registerTitle">
            登録
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          <ValidatedForm id="register-form" onSubmit={handleValidSubmit}>
            <ValidatedField
              name="username"
              label="ユーザー名"
              placeholder="ユーザー名"
              validate={{
                required: { value: true, message: 'ユーザー名が必要です。' },
                pattern: {
                  value: /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/,
                  message: 'Your username is invalid.',
                },
                minLength: { value: 1, message: 'ユーザー名は1文字以上で入力してください' },
                maxLength: { value: 50, message: 'ユーザー名は50文字以下で入力してください' },
              }}
              data-cy="username"
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
            <ValidatedField
              name="firstPassword"
              label="新しいパスワード"
              placeholder="新しいパスワード"
              type="password"
              onChange={updatePassword}
              validate={{
                required: { value: true, message: 'パスワードを入力してください。' },
                minLength: { value: 4, message: 'パスワードは4文字以上で入力してください。' },
                maxLength: { value: 50, message: 'パスワードは50文字以下で入力してください。' },
              }}
              data-cy="firstPassword"
            />
            <PasswordStrengthBar password={password} />
            <ValidatedField
              name="secondPassword"
              label="新しいパスワード(確認)"
              placeholder="新しいパスワード(確認)"
              type="password"
              validate={{
                required: { value: true, message: 'パスワード(確認)を入力してください。' },
                minLength: { value: 4, message: 'パスワード(確認)は4文字以上で入力してください。' },
                maxLength: { value: 50, message: 'パスワード(確認)は50文字以下で入力してください。' },
                validate: v => v === password || 'パスワードが一致しません!',
              }}
              data-cy="secondPassword"
            />
            <Button id="register-submit" color="primary" type="submit" data-cy="submit">
              登録する
            </Button>
          </ValidatedForm>
          <p>&nbsp;</p>
          <Alert color="warning">
            <span></span>
            <a className="alert-link">認証</a>
            <span>
              を試したければ, デフォルトアカウント:
              <br />- 管理者 (ユーザー名=&quot;admin&quot;, パスワード=&quot;admin&quot;) <br />- ユーザー (login=&quot;user&quot;,
              password=&quot;user&quot;).
            </span>
          </Alert>
        </Col>
      </Row>
    </div>
  );
};

export default RegisterPage;
