import { IJokesUser, NewJokesUser } from './jokes-user.model';

export const sampleWithRequiredData: IJokesUser = {
  id: 63808,
  idJoke: 'Pelota Estratega',
  url: 'https://jacobo.com.es',
  iconUrl: 'Account Andalucía bluetooth',
  value: 'Videojuegos Informática',
};

export const sampleWithPartialData: IJokesUser = {
  id: 39538,
  idJoke: 'Joyería bandwidth',
  url: 'https://samuel.com.es',
  iconUrl: 'fritas hacking',
  value: 'Violeta',
};

export const sampleWithFullData: IJokesUser = {
  id: 28019,
  idJoke: 'Heredado',
  url: 'https://carla.org',
  iconUrl: 'transmit Investment Nauru',
  value: 'hack Account Valenciana',
};

export const sampleWithNewData: NewJokesUser = {
  idJoke: 'Sabroso Guapo',
  url: 'http://lorena.org',
  iconUrl: 'Verde',
  value: 'quantify enfoque Re-contextualizado',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
