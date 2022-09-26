import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IJokesUser, NewJokesUser } from '../jokes-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IJokesUser for edit and NewJokesUserFormGroupInput for create.
 */
type JokesUserFormGroupInput = IJokesUser | PartialWithRequiredKeyOf<NewJokesUser>;

type JokesUserFormDefaults = Pick<NewJokesUser, 'id'>;

type JokesUserFormGroupContent = {
  id: FormControl<IJokesUser['id'] | NewJokesUser['id']>;
  idJoke: FormControl<IJokesUser['idJoke']>;
  url: FormControl<IJokesUser['url']>;
  iconUrl: FormControl<IJokesUser['iconUrl']>;
  value: FormControl<IJokesUser['value']>;
  internalUser: FormControl<IJokesUser['internalUser']>;
};

export type JokesUserFormGroup = FormGroup<JokesUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class JokesUserFormService {
  createJokesUserFormGroup(jokesUser: JokesUserFormGroupInput = { id: null }): JokesUserFormGroup {
    const jokesUserRawValue = {
      ...this.getFormDefaults(),
      ...jokesUser,
    };
    return new FormGroup<JokesUserFormGroupContent>({
      id: new FormControl(
        { value: jokesUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      idJoke: new FormControl(jokesUserRawValue.idJoke, {
        validators: [Validators.required],
      }),
      url: new FormControl(jokesUserRawValue.url, {
        validators: [Validators.required],
      }),
      iconUrl: new FormControl(jokesUserRawValue.iconUrl, {
        validators: [Validators.required],
      }),
      value: new FormControl(jokesUserRawValue.value, {
        validators: [Validators.required],
      }),
      internalUser: new FormControl(jokesUserRawValue.internalUser),
    });
  }

  getJokesUser(form: JokesUserFormGroup): IJokesUser | NewJokesUser {
    return form.getRawValue() as IJokesUser | NewJokesUser;
  }

  resetForm(form: JokesUserFormGroup, jokesUser: JokesUserFormGroupInput): void {
    const jokesUserRawValue = { ...this.getFormDefaults(), ...jokesUser };
    form.reset(
      {
        ...jokesUserRawValue,
        id: { value: jokesUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): JokesUserFormDefaults {
    return {
      id: null,
    };
  }
}
