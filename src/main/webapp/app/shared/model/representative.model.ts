export interface IRepresentative {
  id?: number;
  name?: string;
  mail?: string;
  face?: string | null;
  faceId?: string | null;
  hash?: string;
}

export const defaultValue: Readonly<IRepresentative> = {};
