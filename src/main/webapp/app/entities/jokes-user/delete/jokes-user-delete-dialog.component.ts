import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IJokesUser } from '../jokes-user.model';
import { JokesUserService } from '../service/jokes-user.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './jokes-user-delete-dialog.component.html',
})
export class JokesUserDeleteDialogComponent {
  jokesUser?: IJokesUser;

  constructor(protected jokesUserService: JokesUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.jokesUserService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
