import { IRap } from 'app/shared/model/rap.model';

export interface IPhong {
  id?: number;
  tenPhong?: string;
  rap?: IRap | null;
}

export const defaultValue: Readonly<IPhong> = {};
