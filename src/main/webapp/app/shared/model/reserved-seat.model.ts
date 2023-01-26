export interface IReservedSeat {
  id?: number;
  personName?: string | null;
  seatName?: string | null;
}

export const defaultValue: Readonly<IReservedSeat> = {};
