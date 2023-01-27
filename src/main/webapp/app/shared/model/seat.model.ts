export interface ISeat {
  id?: number;
  name?: string;
  canSit?: boolean;
  top?: string;
  left?: string;
}

export const defaultValue: Readonly<ISeat> = {
  canSit: false,
};
