import './home.scss';

import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

import { Row, Col, Alert, Button } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const navigate = useNavigate();

  const handleGotoPrivacyPolicy = () => {
    navigate('/privacy');
  };

  return (
    <Row>
      <Col md="12">
        <h1>Home</h1>
        <p>this page is home page.</p>

        <hr />

        <Button color="info" onClick={handleGotoPrivacyPolicy}>
          go to privacy policy
        </Button>
      </Col>
    </Row>
  );
};

export default Home;
