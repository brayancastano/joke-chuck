import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../jokes-user.test-samples';

import { JokesUserFormService } from './jokes-user-form.service';

describe('JokesUser Form Service', () => {
  let service: JokesUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JokesUserFormService);
  });

  describe('Service methods', () => {
    describe('createJokesUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createJokesUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idJoke: expect.any(Object),
            url: expect.any(Object),
            iconUrl: expect.any(Object),
            value: expect.any(Object),
            internalUser: expect.any(Object),
          })
        );
      });

      it('passing IJokesUser should create a new form with FormGroup', () => {
        const formGroup = service.createJokesUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idJoke: expect.any(Object),
            url: expect.any(Object),
            iconUrl: expect.any(Object),
            value: expect.any(Object),
            internalUser: expect.any(Object),
          })
        );
      });
    });

    describe('getJokesUser', () => {
      it('should return NewJokesUser for default JokesUser initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createJokesUserFormGroup(sampleWithNewData);

        const jokesUser = service.getJokesUser(formGroup) as any;

        expect(jokesUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewJokesUser for empty JokesUser initial value', () => {
        const formGroup = service.createJokesUserFormGroup();

        const jokesUser = service.getJokesUser(formGroup) as any;

        expect(jokesUser).toMatchObject({});
      });

      it('should return IJokesUser', () => {
        const formGroup = service.createJokesUserFormGroup(sampleWithRequiredData);

        const jokesUser = service.getJokesUser(formGroup) as any;

        expect(jokesUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IJokesUser should not enable id FormControl', () => {
        const formGroup = service.createJokesUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewJokesUser should disable id FormControl', () => {
        const formGroup = service.createJokesUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
