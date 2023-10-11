import { IATM, NewATM } from './atm.model';

export const sampleWithRequiredData: IATM = {
  id: 57515,
  estado: 'circuito bus',
  tipo: 'Bicicleta EXE Plástico',
  saldodisponible: 513,
  ubicacion: 'Hormigon Desarrollador',
};

export const sampleWithPartialData: IATM = {
  id: 18352,
  estado: 'Programable Mercado',
  tipo: 'Investigación Informática',
  saldodisponible: 2088,
  ubicacion: 'Organizado Guapa hardware',
};

export const sampleWithFullData: IATM = {
  id: 65626,
  estado: 'Programable Canarias',
  tipo: 'Market',
  saldodisponible: 78845,
  ubicacion: 'Canarias XSS',
};

export const sampleWithNewData: NewATM = {
  estado: 'Avon protocol',
  tipo: 'Estonia Madera Refinado',
  saldodisponible: 38115,
  ubicacion: 'override Senior web-readiness',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
