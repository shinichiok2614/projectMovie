export interface IBapNuoc {
  id?: number;
  tenBapNuoc?: string;
  logoContentType?: string | null;
  logo?: string | null;
  giaTien?: number;
}

export const defaultValue: Readonly<IBapNuoc> = {};
