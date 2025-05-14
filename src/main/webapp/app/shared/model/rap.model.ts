import { ICumRap } from 'app/shared/model/cum-rap.model';

export interface IRap {
  id?: number;
  tenRap?: string;
  diaChi?: string | null;
  thanhPho?: string | null;
  cumRap?: ICumRap | null;
}

export const defaultValue: Readonly<IRap> = {};
