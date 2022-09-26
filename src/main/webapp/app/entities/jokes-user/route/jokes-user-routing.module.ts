import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { JokesUserComponent } from '../list/jokes-user.component';
import { JokesUserDetailComponent } from '../detail/jokes-user-detail.component';
import { JokesUserUpdateComponent } from '../update/jokes-user-update.component';
import { JokesUserRoutingResolveService } from './jokes-user-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const jokesUserRoute: Routes = [
  {
    path: '',
    component: JokesUserComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: JokesUserDetailComponent,
    resolve: {
      jokesUser: JokesUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: JokesUserUpdateComponent,
    resolve: {
      jokesUser: JokesUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: JokesUserUpdateComponent,
    resolve: {
      jokesUser: JokesUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(jokesUserRoute)],
  exports: [RouterModule],
})
export class JokesUserRoutingModule {}
