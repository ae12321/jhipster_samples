import { ISeatGroup } from 'app/shared/model/seat-group.model';

export interface ISeat {
  id?: number;
  name?: string;
  canSit?: boolean;
  top?: string;
  left?: string;
  seatGroup?: ISeatGroup;
}

export const defaultValue: Readonly<ISeat> = {
  canSit: false,
};
