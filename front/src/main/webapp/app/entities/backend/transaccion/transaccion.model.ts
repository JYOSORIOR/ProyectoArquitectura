import dayjs from 'dayjs/esm';
import { ICuenta } from 'app/entities/backend/cuenta/cuenta.model';

export interface ITransaccion {
  id: number;
  estado?: string | null;
  fecha?: dayjs.Dayjs | null;
  cantidad?: number | null;
  cuenta?: Pick<ICuenta, 'id'> | null;
}

export type NewTransaccion = Omit<ITransaccion, 'id'> & { id: null };
