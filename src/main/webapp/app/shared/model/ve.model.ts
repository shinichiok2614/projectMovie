import { ISuatChieu } from 'app/shared/model/suat-chieu.model';
import { TinhTrangVe } from 'app/shared/model/enumerations/tinh-trang-ve.model';

export interface IVe {
  id?: number;
  soDienThoai?: string;
  email?: string | null;
  giaTien?: number;
  tinhTrang?: keyof typeof TinhTrangVe;
  suatChieu?: ISuatChieu | null;
}

export const defaultValue: Readonly<IVe> = {};
