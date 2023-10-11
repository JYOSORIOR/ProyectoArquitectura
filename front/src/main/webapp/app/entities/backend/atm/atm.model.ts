import { ITransaccion } from 'app/entities/backend/transaccion/transaccion.model';

export interface IATM {
  id: number;
  estado?: string | null;
  tipo?: string | null;
  saldodisponible?: number | null;
  ubicacion?: string | null;
  transacciones?: Pick<ITransaccion, 'id'> | null;
}

export type NewATM = Omit<IATM, 'id'> & { id: null };
