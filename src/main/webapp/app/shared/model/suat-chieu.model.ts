import dayjs from 'dayjs';
import { IPhong } from 'app/shared/model/phong.model';
import { IPhim } from 'app/shared/model/phim.model';

export interface ISuatChieu {
  id?: number;
  ngayChieu?: dayjs.Dayjs;
  gioChieu?: string;
  phong?: IPhong | null;
  phim?: IPhim | null;
}

export const defaultValue: Readonly<ISuatChieu> = {};
