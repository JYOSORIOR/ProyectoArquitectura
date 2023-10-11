import dayjs from 'dayjs/esm';

import { IRetiro, NewRetiro } from './retiro.model';

export const sampleWithRequiredData: IRetiro = {
  id: 82824,
  contiene: 'Account',
  estado: 'Respuesta programming',
  cantidad: 87504,
  fecha: dayjs('2023-10-04T02:04'),
};

export const sampleWithPartialData: IRetiro = {
  id: 14304,
  contiene: 'Sopa',
  estado: 'Investment Account',
  cantidad: 64242,
  fecha: dayjs('2023-10-04T11:54'),
};

export const sampleWithFullData: IRetiro = {
  id: 44038,
  contiene: 'mindshare syndicate parsing',
  estado: 'nacional',
  cantidad: 90825,
  fecha: dayjs('2023-10-04T07:11'),
};

export const sampleWithNewData: NewRetiro = {
  contiene: 'Rojo circuit',
  estado: 'Rústico synthesize',
  cantidad: 58275,
  fecha: dayjs('2023-10-04T13:43'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
