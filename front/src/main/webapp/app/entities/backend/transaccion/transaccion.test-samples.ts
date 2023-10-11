import dayjs from 'dayjs/esm';

import { ITransaccion, NewTransaccion } from './transaccion.model';

export const sampleWithRequiredData: ITransaccion = {
  id: 86269,
  estado: 'relationships Auto',
  fecha: dayjs('2023-10-04T09:16'),
  cantidad: 94513,
};

export const sampleWithPartialData: ITransaccion = {
  id: 78963,
  estado: 'AGP',
  fecha: dayjs('2023-10-04T09:11'),
  cantidad: 94621,
};

export const sampleWithFullData: ITransaccion = {
  id: 32126,
  estado: 'Plástico Navarra Región',
  fecha: dayjs('2023-10-04T23:24'),
  cantidad: 96517,
};

export const sampleWithNewData: NewTransaccion = {
  estado: 'efficient',
  fecha: dayjs('2023-10-04T10:28'),
  cantidad: 86290,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
