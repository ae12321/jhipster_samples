import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        <h2>ようこそ, Java Hipster!</h2>
        <p className="lead">ここはトップページです。</p>
        {account?.login ? (
          <div>
            <Alert color="success">&quot;{account.login}&quot;としてログインしています。</Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              <span>&nbsp;</span>
              <Link to="/login" className="alert-link">
                認証
              </Link>
              を試したければ, デフォルトアカウント:
              <br />- 管理者 (ユーザー名=&quot;admin&quot;, パスワード=&quot;admin&quot;) <br />- ユーザー (login=&quot;user&quot;,
              password=&quot;user&quot;).
            </Alert>

            <Alert color="warning">
              アカウントをまだお持ちでないですか?&nbsp;
              <Link to="/account/register" className="alert-link">
                アカウントを登録する
              </Link>
            </Alert>
          </div>
        )}
        <p>JHipsterに関して質問があれば:</p>

        <ul>
          <li>
            <a href="https://www.jhipster.tech/" target="_blank" rel="noopener noreferrer">
              JHipsterのサイト
            </a>
          </li>
          <li>
            <a href="https://stackoverflow.com/tags/jhipster/info" target="_blank" rel="noopener noreferrer">
              Stack Overflow のJHipster
            </a>
          </li>
          <li>
            <a href="https://github.com/jhipster/generator-jhipster/issues?state=open" target="_blank" rel="noopener noreferrer">
              JHipsterのバグトラッカー
            </a>
          </li>
          <li>
            <a href="https://gitter.im/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
              JHipster public chat room
            </a>
          </li>
          <li>
            <a href="https://twitter.com/jhipster" target="_blank" rel="noopener noreferrer">
              Twitterの @jhipster
            </a>
          </li>
        </ul>

        <p>
          JHipsterを気に入ったら、是非スターをつけてください。{' '}
          <a href="https://github.com/jhipster/generator-jhipster" target="_blank" rel="noopener noreferrer">
            GitHub
          </a>
          !
        </p>
      </Col>
    </Row>
  );
};

export default Home;
