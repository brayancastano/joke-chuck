import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { JokesUserFormService, JokesUserFormGroup } from './jokes-user-form.service';
import { IJokesUser } from '../jokes-user.model';
import { JokesUserService } from '../service/jokes-user.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { Account } from '../../../core/auth/account.model';
import { AccountService } from '../../../core/auth/account.service';

@Component({
  selector: 'jhi-jokes-user-update',
  templateUrl: './jokes-user-update.component.html',
})
export class JokesUserUpdateComponent implements OnInit {
  isSaving = false;
  jokesUser: IJokesUser | null = null;

  usersSharedCollection: IUser[] = [];
  editForm: JokesUserFormGroup = this.jokesUserFormService.createJokesUserFormGroup();

  constructor(
    protected jokesUserService: JokesUserService,
    protected jokesUserFormService: JokesUserFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.jokesUserService.findJoke().subscribe(jokesUser => {
      this.jokesUser = jokesUser.body;
      if (this.jokesUser) {
        this.updateForm(this.jokesUser);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jokesUser = this.jokesUserFormService.getJokesUser(this.editForm);
    if (jokesUser.id !== null) {
      this.subscribeToSaveResponse(this.jokesUserService.update(jokesUser));
    } else {
      this.subscribeToSaveResponse(this.jokesUserService.create(jokesUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJokesUser>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(jokesUser: IJokesUser): void {
    this.jokesUser = jokesUser;
    this.jokesUserFormService.resetForm(this.editForm, jokesUser);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, jokesUser.internalUser);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.jokesUser?.internalUser)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
