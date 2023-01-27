import './privacy.scss';

import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

import { Row, Col, Alert, Button, Label, Input, Form, FormGroup, Container } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Privacy = () => {
  const navigate = useNavigate();
  const [checked, setChecked] = useState(false);

  const handleGotoPrivacyPolicy = () => {
    // navigate('/privacy');
    alert('aaa');
  };

  useEffect(() => {
    console.log(checked);
  }, [checked]);

  const handleNextStep = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    alert('clicked');
  };

  return (
    <Container fluid>
      <h1>Privacy policy</h1>
      <p>Please agree the following.</p>
      <textarea name="" id="" cols={50} rows={10}></textarea>
      <br />

      <Form>
        <FormGroup switch>
          <Input id="agree-switch" type="switch" onClick={() => setChecked(!checked)} />
          <Label for="agree-switch">I'm agree.</Label>
        </FormGroup>
      </Form>
      <Button onClick={handleNextStep} disabled={!checked} color="info">
        next step
      </Button>
    </Container>
  );
};

export default Privacy;
