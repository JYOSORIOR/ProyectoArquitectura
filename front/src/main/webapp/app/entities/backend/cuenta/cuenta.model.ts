import { ICliente } from 'app/entities/backend/cliente/cliente.model';
import { IRetiro } from 'app/entities/backend/retiro/retiro.model';

export interface ICuenta {
  id: number;
  tipo?: string | null;
  estado?: string | null;
  saldo?: number | null;
  cliente?: Pick<ICliente, 'id'> | null;
  retiros?: Pick<IRetiro, 'id'> | null;
}

export type NewCuenta = Omit<ICuenta, 'id'> & { id: null };
