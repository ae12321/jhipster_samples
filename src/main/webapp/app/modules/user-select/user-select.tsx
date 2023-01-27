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

export const UserSelect = () => {
  const navigate = useNavigate();

  const [representative, setRepresentative] = useState<RepresentativeStateType>({ name: '', kana: '', mail: '' });
  const [guests, setGuests] = useState<GuestStateType[]>([{ name: representative.name, kana: representative.kana }]);

  useEffect(() => {
    // display representative state
    console.log('representative: ' + representative.name + ':' + representative.kana + ':' + representative.mail);
    // display guests state
    guests.forEach((g, i) => {
      console.log(`guest${i + 1}: ` + g.name + ':' + g.kana);
    });
    console.log('-----');
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
    </Container>
  );
};

// const a = (
//   <>
//     {/* representative */}

//     <h2>Guest Info</h2>
//     {/* guests */}
//     {guests.forEach((guest, index) => {
//       const guestheader = `guest${padStart(index.toString(), 2, '0')}`;

//       return (
//         <Card>
//           <CardBody>
//             <CardTitle>guestheader</CardTitle>
//             <CardText>
//               <Label for="representative_name">Name</Label>
//               <Input id="representative_name" name="name" placeholder="taro yamada" type="text" onChange={handleChangeRepresentative} />
//               <Label for="representative_kana">Kana</Label>
//               <Input
//                 id="representative_kana"
//                 name="kana"
//                 placeholder="taro yamada(kana)"
//                 type="text"
//                 onChange={handleChangeRepresentative}
//               />
//               <Label for="representative_mail">Email</Label>
//               <Input id="representative_mail" name="mail" placeholder="aaa@bbb.com" type="email" onChange={handleChangeRepresentative} />
//             </CardText>
//           </CardBody>
//         </Card>
//       );
//     })}
//   </>
// );

// const GuestCard = ({ no }) => {
//   const string = `guest${no}`;

//   return (
//     <Card>
//       <CardBody>
//         <CardTitle>{string} Info</CardTitle>
//         <CardText>
//           <Label for={string}>Name</Label>
//           <Input id={`${string}_name`} name={`${string}_name`} placeholder="taro yamada" type="text" />
//           <Label for={`${string}_kana`}>Kana</Label>
//           <Input id={`${string}_kana`} name={`${string}_kana`} placeholder="taro yamada" type="text" />
//         </CardText>
//       </CardBody>
//     </Card>
//   );
// };

// const RepresentativeCard = () => {
//   const string = `representative`;

//   return (
//     <Card>
//       <CardBody>
//         <CardTitle>{string} Info</CardTitle>
//         <CardText>
//           <Label for={string}>Name</Label>
//           <Input id={`${string}_name`} name={`${string}_name`} placeholder="taro yamada" type="text" />
//           <Label for={`${string}_kana`}>Kana</Label>
//           <Input id={`${string}_kana`} name={`${string}_kana`} placeholder="taro yamada" type="text" />
//           <Label for={`${string}_mail`}>Kana</Label>
//           <Input id={`${string}_mail`} name={`${string}_mail`} placeholder="aaa@bbb.com" type="email" />
//         </CardText>
//       </CardBody>
//     </Card>
//   );
// };

export default UserSelect;
