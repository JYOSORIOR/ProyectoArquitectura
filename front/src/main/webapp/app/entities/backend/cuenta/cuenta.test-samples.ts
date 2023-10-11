import { ICuenta, NewCuenta } from './cuenta.model';

export const sampleWithRequiredData: ICuenta = {
  id: 79601,
  tipo: 'Ronda maximizada',
  estado: 'Borders hacking deposit',
  saldo: 87588,
};

export const sampleWithPartialData: ICuenta = {
  id: 18922,
  tipo: 'bluetooth Algodón',
  estado: 'International withdrawal',
  saldo: 51623,
};

export const sampleWithFullData: ICuenta = {
  id: 22006,
  tipo: 'Bacon SDD circuit',
  estado: 'Inversor',
  saldo: 45556,
};

export const sampleWithNewData: NewCuenta = {
  tipo: 'Corporativo',
  estado: 'engage Pollo online',
  saldo: 20423,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
