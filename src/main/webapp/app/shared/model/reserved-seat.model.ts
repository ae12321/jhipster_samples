import { IMainUser } from 'app/shared/model/main-user.model';

export interface IReservedSeat {
  id?: number;
  personName?: string | null;
  seatName?: string | null;
  mainUser?: IMainUser;
}

export const defaultValue: Readonly<IReservedSeat> = {};
