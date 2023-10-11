export interface ICliente {
  id: number;
  nombre?: string | null;
  tipodeusuario?: string | null;
  correo?: string | null;
  cedula?: string | null;
  telefono?: string | null;
}

export type NewCliente = Omit<ICliente, 'id'> & { id: null };
