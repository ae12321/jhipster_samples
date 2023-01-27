import './home.scss';

import React from 'react';
import { Link, useNavigate } from 'react-router-dom';

import { Row, Col, Alert, Button, Container } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const navigate = useNavigate();

  const handleGotoPrivacyPolicy = () => {
    navigate('/privacy');
  };

  return (
    <Container fluid>
      <h1>Home</h1>
      <p>this page is home page.</p>

      <Button color="info" onClick={handleGotoPrivacyPolicy}>
        go to privacy policy
      </Button>
    </Container>
  );
};

export default Home;
