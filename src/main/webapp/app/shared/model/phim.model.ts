import dayjs from 'dayjs';
import { TheLoai } from 'app/shared/model/enumerations/the-loai.model';

export interface IPhim {
  id?: number;
  tenPhim?: string;
  thoiLuong?: number;
  gioiThieu?: string | null;
  ngayCongChieu?: dayjs.Dayjs | null;
  linkTrailer?: string | null;
  logoContentType?: string | null;
  logo?: string | null;
  doTuoi?: string | null;
  theLoai?: keyof typeof TheLoai;
  dinhDang?: string | null;
}

export const defaultValue: Readonly<IPhim> = {};
