import dayjs from 'dayjs/esm';
import { IATM } from 'app/entities/backend/atm/atm.model';

export interface IRetiro {
  id: number;
  contiene?: string | null;
  estado?: string | null;
  cantidad?: number | null;
  fecha?: dayjs.Dayjs | null;
  atm?: Pick<IATM, 'id'> | null;
}

export type NewRetiro = Omit<IRetiro, 'id'> & { id: null };
