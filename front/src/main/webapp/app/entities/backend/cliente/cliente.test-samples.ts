import { ICliente, NewCliente } from './cliente.model';

export const sampleWithRequiredData: ICliente = {
  id: 45820,
  nombre: 'Cataluña',
  tipodeusuario: 'Mascotas',
  correo: 'grow y',
  cedula: 'robust Andalucía synthesize',
  telefono: 'Rústico',
};

export const sampleWithPartialData: ICliente = {
  id: 90975,
  nombre: 'navigate streamline',
  tipodeusuario: 'Técnico',
  correo: 'THX Peso deposit',
  cedula: 'Nueva Savings',
  telefono: 'withdrawal Libyan',
};

export const sampleWithFullData: ICliente = {
  id: 17938,
  nombre: 'Bedfordshire niches éxito',
  tipodeusuario: 'redundant Senda alarm',
  correo: 'Granito Nacional Morado',
  cedula: 'Syrian',
  telefono: 'Open-source back Nacional',
};

export const sampleWithNewData: NewCliente = {
  nombre: 'Moda functionalities best-of-breed',
  tipodeusuario: 'Rojo back-end invoice',
  correo: 'synthesizing Borders Hecho',
  cedula: 'connect Coche payment',
  telefono: 'connecting conocimiento conjunto',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
