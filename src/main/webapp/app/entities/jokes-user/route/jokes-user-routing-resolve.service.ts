import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IJokesUser } from '../jokes-user.model';
import { JokesUserService } from '../service/jokes-user.service';

@Injectable({ providedIn: 'root' })
export class JokesUserRoutingResolveService implements Resolve<IJokesUser | null> {
  constructor(protected service: JokesUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJokesUser | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((jokesUser: HttpResponse<IJokesUser>) => {
          if (jokesUser.body) {
            return of(jokesUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
