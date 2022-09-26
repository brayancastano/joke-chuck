import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { JokesUserComponent } from './list/jokes-user.component';
import { JokesUserDetailComponent } from './detail/jokes-user-detail.component';
import { JokesUserUpdateComponent } from './update/jokes-user-update.component';
import { JokesUserDeleteDialogComponent } from './delete/jokes-user-delete-dialog.component';
import { JokesUserRoutingModule } from './route/jokes-user-routing.module';

@NgModule({
  imports: [SharedModule, JokesUserRoutingModule],
  declarations: [JokesUserComponent, JokesUserDetailComponent, JokesUserUpdateComponent, JokesUserDeleteDialogComponent],
})
export class JokesUserModule {}
