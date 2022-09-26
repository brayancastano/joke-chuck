import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IJokesUser, NewJokesUser } from '../jokes-user.model';

export type PartialUpdateJokesUser = Partial<IJokesUser> & Pick<IJokesUser, 'id'>;

export type EntityResponseType = HttpResponse<IJokesUser>;
export type EntityArrayResponseType = HttpResponse<IJokesUser[]>;

@Injectable({ providedIn: 'root' })
export class JokesUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/jokes-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(jokesUser: NewJokesUser): Observable<EntityResponseType> {
    return this.http.post<IJokesUser>(this.resourceUrl, jokesUser, { observe: 'response' });
  }

  update(jokesUser: IJokesUser): Observable<EntityResponseType> {
    return this.http.put<IJokesUser>(`${this.resourceUrl}/${this.getJokesUserIdentifier(jokesUser)}`, jokesUser, { observe: 'response' });
  }

  partialUpdate(jokesUser: PartialUpdateJokesUser): Observable<EntityResponseType> {
    return this.http.patch<IJokesUser>(`${this.resourceUrl}/${this.getJokesUserIdentifier(jokesUser)}`, jokesUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IJokesUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findJoke(): Observable<EntityResponseType> {
    return this.http.get<IJokesUser>(`${this.resourceUrl}/chuck-api/`, { observe: 'response' });
  }

  findByIdUser(): Observable<EntityArrayResponseType> {
    return this.http.get<IJokesUser[]>(`${this.resourceUrl}/id-user`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJokesUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getJokesUserIdentifier(jokesUser: Pick<IJokesUser, 'id'>): number {
    return jokesUser.id;
  }

  compareJokesUser(o1: Pick<IJokesUser, 'id'> | null, o2: Pick<IJokesUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getJokesUserIdentifier(o1) === this.getJokesUserIdentifier(o2) : o1 === o2;
  }

  addJokesUserToCollectionIfMissing<Type extends Pick<IJokesUser, 'id'>>(
    jokesUserCollection: Type[],
    ...jokesUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const jokesUsers: Type[] = jokesUsersToCheck.filter(isPresent);
    if (jokesUsers.length > 0) {
      const jokesUserCollectionIdentifiers = jokesUserCollection.map(jokesUserItem => this.getJokesUserIdentifier(jokesUserItem)!);
      const jokesUsersToAdd = jokesUsers.filter(jokesUserItem => {
        const jokesUserIdentifier = this.getJokesUserIdentifier(jokesUserItem);
        if (jokesUserCollectionIdentifiers.includes(jokesUserIdentifier)) {
          return false;
        }
        jokesUserCollectionIdentifiers.push(jokesUserIdentifier);
        return true;
      });
      return [...jokesUsersToAdd, ...jokesUserCollection];
    }
    return jokesUserCollection;
  }
}
