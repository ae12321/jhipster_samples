import { IRepresentative } from 'app/shared/model/representative.model';

export interface IReservedSeat {
  id?: number;
  name?: string;
  representative?: IRepresentative;
}

export const defaultValue: Readonly<IReservedSeat> = {};
