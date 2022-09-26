import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'jokes-user',
        data: { pageTitle: 'JokesUsers' },
        loadChildren: () => import('./jokes-user/jokes-user.module').then(m => m.JokesUserModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
