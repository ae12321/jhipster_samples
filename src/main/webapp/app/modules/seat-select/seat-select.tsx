import './seat-select.scss';

import React, { FC, useEffect, useRef, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

import { Row, Col, Alert, Button, Label, Input, Form, FormGroup, Container, Card, CardBody, CardTitle, CardText } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import { pad, padStart, capitalize } from 'lodash';

// import hall1 from './images/zasekihyou-1.png';

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
export const SeatSelect = () => {
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

  const handleChangeSelectGroup = (e: React.ChangeEvent<HTMLInputElement>) => {};

  type SeatData = {
    'seat-id': string;
    'seat-group': string;
    'seat-status': 'can-select' | 'cannot-select';
    'seat-top': string;
    'seat-left': string;
  };
  // prettier-ignore
  const seatDatas: SeatData[] = [
    { 'seat-id': 'a-1', 'seat-group': 'group-a', 'seat-top': '0px', 'seat-left': '0px', 'seat-status': 'cannot-select' },
    { 'seat-id': 'a-2', 'seat-group': 'group-a', 'seat-top': '14px', 'seat-left': '90px', 'seat-status': 'can-select' },
    { 'seat-id': 'x-1', 'seat-group': 'group-a', 'seat-top': '55px', 'seat-left': '40px', 'seat-status': 'can-select' },
    { 'seat-id': 'x-2', 'seat-group': 'group-a', 'seat-top': '55px', 'seat-left': '90px', 'seat-status': 'can-select' },

    { 'seat-id': 'z-1', 'seat-group': 'group-b', 'seat-top': '0px', 'seat-left': '0px', 'seat-status': 'cannot-select' },
    { 'seat-id': 'z-2', 'seat-group': 'group-b', 'seat-top': '80px', 'seat-left': '150px', 'seat-status': 'can-select' },
  ];

  return (
    <Container fluid>
      <h1>Seat Select</h1>
      <hr />

      <h2>Please input seat.</h2>

      {/* <img src="content/images/logo-jhipster.png" alt="Logo" /> */}
      {/*  */}
      <img src="content/images/seat/hall-1.png" alt="aa" />

      <FormGroup>
        <Label for="exampleSelect">Select Group</Label>
        <Input id="exampleSelect" name="select" type="select" onChange={handleChangeSelectGroup}>
          <option></option>
          <option>A</option>
          <option>B</option>
          <option>C</option>
        </Input>
      </FormGroup>

      {/* ok */}
      <div className="relative" style={{ display: 'none' }}>
        <img src="content/images/seat/group-a.png" alt="aa" />
        {seatDatas
          // filter data containing group file name
          .filter(seat => seat['seat-group'] === 'group-a')
          .map((seat, index) => {
            console.log(seat);

            // according to status
            let seatStatus: string;
            let handleClickSeat: React.MouseEventHandler<HTMLDivElement> | undefined;
            if (seat['seat-status'] === 'can-select') {
              seatStatus = 'seat-can-select';
              handleClickSeat = e => {
                alert(e.currentTarget.dataset.seatId);
              };
            } else if (seat['seat-status'] === 'cannot-select') {
              seatStatus = 'seat-cannot-select';
              handleClickSeat = undefined;
            } else {
              seatStatus = 'seat-cannot-select';
              handleClickSeat = undefined;
            }

            // according to position
            const seatPosition = ((): { top: string; left: string } => {
              return { top: seat['seat-top'], left: seat['seat-left'] };
            })();

            const seatId = seat['seat-id'];

            return (
              <>
                <div
                  key={index}
                  className={`seat ${seatStatus}`}
                  style={seatPosition}
                  data-seat-id={seatId}
                  onClick={handleClickSeat}
                ></div>
              </>
            );
          })}
      </div>

      <div className="relative">
        <img src="content/images/seat/group-b.png" alt="aa" />
        {seatDatas
          .filter(seat => seat['seat-group'] === 'group-b')
          .map((seat, index) => {
            let seatStatus: string;
            let handleClickSeat;

            console.log(seat);

            if (seat['seat-status'] === 'can-select') {
              seatStatus = 'seat-can-select';
              handleClickSeat = e => {
                alert(e.currentTarget.dataset.seatId);
              };
            } else if (seat['seat-status'] === 'cannot-select') {
              seatStatus = 'seat-cannot-select';
              handleClickSeat = undefined;
            } else {
              seatStatus = 'seat-cannot-select';
              handleClickSeat = undefined;
            }

            const seatPosition = (): { top: string; left: string } => {
              return { top: seat['seat-top'], left: seat['seat-left'] };
            };

            return (
              <>
                <div
                  key={index}
                  className={`seat ${seatStatus}`}
                  style={seatPosition()}
                  data-seat-id={seat['seat-id']}
                  onClick={handleClickSeat}
                ></div>
              </>
            );
          })}
      </div>

      <hr />
      <Row>
        <Col md={12}>
          <Button color="info" style={{ width: '100%' }} onClick={() => {}} disabled={false}>
            Next Step
          </Button>
        </Col>
      </Row>
    </Container>
  );
};

export default SeatSelect;
