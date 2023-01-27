import './user-select.scss';

import React, { FC, useEffect, useRef, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

import { Row, Col, Alert, Button, Label, Input, Form, FormGroup, Container, Card, CardBody, CardTitle, CardText } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import { pad, padStart, capitalize } from 'lodash';

const MAX_GUEST = 10;

type RepresentativeStateType = {
  name: string;
  kana: string;
  mail: string;
};
type GuestStateType = {
  name: string;
  kana: string;
};

// TODO: validation of whether previous guest name/kana is filled
// TODO: validation mail format check and output error
// TODO: validation of whether previous guest name/kana is filled
export const UserSelect = () => {
  const navigate = useNavigate();

  const [representative, setRepresentative] = useState<RepresentativeStateType>({ name: '', kana: '', mail: '' });
  const [guests, setGuests] = useState<GuestStateType[]>([{ name: representative.name, kana: representative.kana }]);
  const [disabledNextStep, setDisabledNextStep] = useState(true);

  const outputs = () => {
    // display representative state
    console.log('representative: ' + representative.name + ':' + representative.kana + ':' + representative.mail);
    // display guests state
    guests.forEach((g, i) => {
      console.log(`guest${i + 1}: ` + g.name + ':' + g.kana);
    });
    console.log('-----');
  };

  const validateInput = () => {
    let emptyValueCount = 0;

    Object.keys(representative).forEach(key => {
      const value = representative[key];
      if (value === null || value === undefined || value === '') {
        emptyValueCount++;
      }
    });
    guests.forEach((guest, index) => {
      console.log(index, guest);
      Object.keys(guest).forEach(key => {
        const value = guest[key];
        if (value === null || value === undefined || value === '') {
          emptyValueCount++;
        }
      });
    });

    setDisabledNextStep(1 <= emptyValueCount);
  };
  useEffect(() => {
    outputs();
    validateInput();
  }, [representative, guests]);

  const handleChangeRepresentative = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newState = { ...representative, [e.target.name]: e.target.value };
    setRepresentative(newState);
    // representative's name and kana link guest01's
    if (e.target.name !== 'mail') {
      handleChangeGuest(e, 0);
    }
  };
  const handleChangeGuest = (e: React.ChangeEvent<HTMLInputElement>, index: number) => {
    const newState = [...guests];
    // key is value of name attribute

    newState[index][e.target.name] = e.target.value;
    setGuests(newState);
  };

  const handleAddGuest = (e: React.MouseEvent<HTMLInputElement>) => {
    const newState = [...guests, { name: '', kana: '' }];
    setGuests(newState);
  };
  const handleDeleteGuest = (e: React.MouseEvent<HTMLInputElement>) => {
    const newState = [...guests];
    newState.pop();
    setGuests(newState);
  };
  const handleNextStep = () => {
    navigate('/seat-select');
  };
  return (
    <Container fluid>
      <h1>User Select</h1>
      <hr />

      <h2>Please input representative.</h2>
      <Card>
        <CardBody>
          <CardTitle>Representative</CardTitle>
          <CardText>
            <Label for="representative_name">Name</Label>
            <Input id="representative_name" name="name" placeholder="taro yamada" type="text" onChange={handleChangeRepresentative} />
            <Label for="representative_kana">Kana</Label>
            <Input id="representative_kana" name="kana" placeholder="taro yamada(kana)" type="text" onChange={handleChangeRepresentative} />
            <Label for="representative_mail">Email</Label>
            <Input id="representative_mail" name="mail" placeholder="aaa@bbb.com" type="email" onChange={handleChangeRepresentative} />
          </CardText>
        </CardBody>
      </Card>

      <h2>Please input guest info.</h2>
      {guests.map((guest, index) => {
        const ord = index + 1;
        const guestname = `guest` + padStart(ord.toString(), 2, '0');
        return (
          <Card key={index}>
            <CardBody>
              <CardTitle>{capitalize(guestname)}</CardTitle>
              <CardText>
                <Label for={`${guestname}_name`}>Name</Label>
                <Input
                  id={`${guestname}_name`}
                  name="name"
                  placeholder="taro yamada"
                  type="text"
                  disabled={index === 0}
                  value={guest.name}
                  onChange={e => handleChangeGuest(e, index)}
                />
                <Label for="representative_kana">Kana</Label>
                <Input
                  id={`${guestname}_kana`}
                  name="kana"
                  placeholder="taro yamada(kana)"
                  type="text"
                  disabled={index === 0}
                  value={guest.kana}
                  onChange={e => handleChangeGuest(e, index)}
                />
              </CardText>
            </CardBody>
          </Card>
        );
      })}

      <Row>
        <Col md={6}>
          <Button color="info" style={{ width: '100%' }} onClick={handleAddGuest} disabled={guests.length >= MAX_GUEST}>
            Add
          </Button>
        </Col>
        <Col md={6}>
          <Button color="danger" style={{ width: '100%' }} onClick={handleDeleteGuest} disabled={guests.length <= 1}>
            Delete
          </Button>
        </Col>
      </Row>
      <hr />
      <Row>
        <Col md={12}>
          <Button color="info" style={{ width: '100%' }} onClick={handleNextStep} disabled={disabledNextStep}>
            Next Step
          </Button>
        </Col>
      </Row>
    </Container>
  );
};

export default UserSelect;
