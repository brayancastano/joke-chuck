import { IUser } from 'app/entities/user/user.model';

export interface IJokesUser {
  id: number;
  idJoke?: string | null;
  url?: string | null;
  iconUrl?: string | null;
  value?: string | null;
  internalUser?: Pick<IUser, 'id'> | null;
}

export type NewJokesUser = Omit<IJokesUser, 'id'> & { id: null };
