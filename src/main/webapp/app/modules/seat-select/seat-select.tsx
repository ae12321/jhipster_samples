import './seat-select.scss';

import React, { FC, useEffect, useRef, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

import { Row, Col, Alert, Button, Label, Input, Form, FormGroup, Container, Card, CardBody, CardTitle, CardText, Table } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import { pad, padStart, capitalize } from 'lodash';

// import hall1 from './images/zasekihyou-1.png';

// import { seatDatas, SeatDataType } from './data-sample';

type DisplayStyleType = 'none' | 'block';
type GuestType = { name: string; seat: string };
type GroupType = { groupName: string; displayStyle: DisplayStyleType };
// prettier-ignore
type SeatType = { 'seat-id': string; 'seat-group': string; 'seat-status': 'can-select' | 'cannot-select'; 'seat-top': string; 'seat-left': string; };

export const SeatSelect = () => {
  const navigate = useNavigate();

  // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
  // prettier-ignore
  const [groups, setGroups] = useState<GroupType[]>([
    { groupName: 'group-a', displayStyle: 'none' },
    { groupName: 'group-b', displayStyle: 'none' },
  ]);
  // prettier-ignore
  const seats: SeatType[] = [
    { 'seat-id': 'a-1', 'seat-group': 'group-a', 'seat-top': '0px', 'seat-left': '0px', 'seat-status': 'cannot-select' },
    { 'seat-id': 'a-2', 'seat-group': 'group-a', 'seat-top': '14px', 'seat-left': '90px', 'seat-status': 'can-select' },
    { 'seat-id': 'x-1', 'seat-group': 'group-a', 'seat-top': '55px', 'seat-left': '40px', 'seat-status': 'can-select' },
    { 'seat-id': 'x-2', 'seat-group': 'group-a', 'seat-top': '55px', 'seat-left': '90px', 'seat-status': 'can-select' },
    { 'seat-id': 'z-1', 'seat-group': 'group-b', 'seat-top': '0px', 'seat-left': '0px', 'seat-status': 'cannot-select' },
    { 'seat-id': 'z-2', 'seat-group': 'group-b', 'seat-top': '80px', 'seat-left': '150px', 'seat-status': 'can-select' },
  ];
  // prettier-ignore
  const [guests, setGuests] = useState<GuestType[]>([
    { name: 'john doe', seat: '' },
    { name: 'justin beaver', seat: '' },
  ]);

  // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

  const [disableClickNextStep, setDisableClickNextStep] = useState(true);
  useEffect(() => {
    const allGuestSeatSelected = guests.every(guest => guest.seat !== '');
    setDisableClickNextStep(!allGuestSeatSelected);
  }, [guests]);

  const handleNextStep = () => {
    navigate('/face-select');
  };

  const handleClearSelectedSeat = () => {
    const newState = [...guests].map(guest => {
      const newStateObj = { ...guest, seat: '' };
      return newStateObj;
    });
    setGuests(newState);
  };
  const handleChangeSelectGroup = (e: React.ChangeEvent<HTMLInputElement>) => {
    const selected = e.currentTarget.value;
    const newState = [...groups].map(group => {
      if (group.groupName === selected) {
        group.displayStyle = 'block';
      } else {
        group.displayStyle = 'none';
      }
      return group;
    });
    setGroups(newState);
  };

  useEffect(() => {
    console.log('====================');
    guests.forEach(guest => console.log(guest));
  }, [guests]);

  const GuestList = () => {
    return (
      <>
        <Table bordered responsive size="sm">
          <thead>
            <tr>
              <th>##</th>
              <th>name</th>
              <th>seat</th>
            </tr>
          </thead>
          <tbody>
            {guests.map((guest, index) => {
              return (
                <tr key={index}>
                  <td>{index + 1}</td>
                  <td>{guest.name}</td>
                  <td>{guest.seat}</td>
                </tr>
              );
            })}
          </tbody>
        </Table>
      </>
    );
  };

  return (
    <Container fluid>
      <h1>Seat Select</h1>
      <hr />

      <h2>Please input seat.</h2>

      <img src="content/images/seat/hall-1.png" alt="aa" />

      <FormGroup>
        <Label for="exampleSelect">Select Group</Label>
        <Input id="exampleSelect" name="select" type="select" onChange={handleChangeSelectGroup}>
          <option value=""></option>
          <option value="group-a">A</option>
          <option value="group-b">B</option>
        </Input>
      </FormGroup>

      {/* display GroupList */}
      {groups.map(({ groupName, displayStyle }, index) => {
        // ({ groupName, displayStyle }: { groupName: string; displayStyle: 'none' | 'block' }, index: number)
        return (
          <>
            <GroupList key={index} groupName={groupName} displayStyle={displayStyle} seats={seats} guests={guests} setGuests={setGuests} />
          </>
        );
      })}

      {/* display GuestStatus */}
      <GuestList />
      <hr />

      <Row>
        <Col md={12}>
          <Button color="warning" style={{ width: '50%' }} onClick={handleClearSelectedSeat}>
            Clear All Seat
          </Button>
        </Col>
      </Row>
      <hr />

      <Row>
        <Col md={12}>
          <Button color="info" style={{ width: '100%' }} onClick={handleNextStep} disabled={disableClickNextStep}>
            Next Step
          </Button>
        </Col>
      </Row>
    </Container>
  );
};

const GroupList = ({
  groupName,
  displayStyle,
  seats,
  guests,
  setGuests,
}: {
  groupName: string;
  displayStyle: DisplayStyleType;
  seats: SeatType[];
  guests: GuestType[];
  setGuests: React.Dispatch<React.SetStateAction<GuestType[]>>;
}) => {
  return (
    <>
      <div className="relative" style={{ display: `${displayStyle}` }}>
        <img src={`content/images/seat/${groupName}.png`} alt="aa" />
        {seats
          .filter(seat => seat['seat-group'] === groupName)
          .map((seat, index) => {
            let seatStatus: string;
            let handleClickSeat: React.MouseEventHandler<HTMLDivElement> | undefined;

            // console.log(seat);

            if (seat['seat-status'] === 'can-select') {
              seatStatus = 'seat-can-select';
              handleClickSeat = e => {
                const newState = [...guests];
                const filtered = newState.filter(guest => guest.seat === '');
                if (filtered.length > 0) {
                  newState.filter(guest => guest.seat === '')[0].seat = e.currentTarget.dataset.seatId;
                  setGuests(newState);
                }
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
    </>
  );
};

export default SeatSelect;
