import { IRepresentative } from 'app/shared/model/representative.model';
import { ISeat } from 'app/shared/model/seat.model';

export interface IReservedSeat {
  id?: number;
  name?: string;
  representative?: IRepresentative;
  seat?: ISeat;
}

export const defaultValue: Readonly<IReservedSeat> = {};
