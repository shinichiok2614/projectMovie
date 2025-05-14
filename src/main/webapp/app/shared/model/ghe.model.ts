import { IPhong } from 'app/shared/model/phong.model';
import { IVe } from 'app/shared/model/ve.model';
import { ILoaiGhe } from 'app/shared/model/loai-ghe.model';
import { TinhTrangGhe } from 'app/shared/model/enumerations/tinh-trang-ghe.model';

export interface IGhe {
  id?: number;
  tenGhe?: string;
  tinhTrang?: keyof typeof TinhTrangGhe;
  phong?: IPhong | null;
  ve?: IVe | null;
  loaiGhe?: ILoaiGhe | null;
}

export const defaultValue: Readonly<IGhe> = {};
