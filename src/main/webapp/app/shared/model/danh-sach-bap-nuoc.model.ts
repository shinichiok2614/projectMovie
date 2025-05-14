import { IVe } from 'app/shared/model/ve.model';

export interface IDanhSachBapNuoc {
  id?: number;
  soDienThoai?: string;
  tenBapNuoc?: string;
  ve?: IVe | null;
}

export const defaultValue: Readonly<IDanhSachBapNuoc> = {};
