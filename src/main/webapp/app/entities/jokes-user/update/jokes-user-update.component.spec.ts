import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { JokesUserFormService } from './jokes-user-form.service';
import { JokesUserService } from '../service/jokes-user.service';
import { IJokesUser } from '../jokes-user.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { JokesUserUpdateComponent } from './jokes-user-update.component';

describe('JokesUser Management Update Component', () => {
  let comp: JokesUserUpdateComponent;
  let fixture: ComponentFixture<JokesUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let jokesUserFormService: JokesUserFormService;
  let jokesUserService: JokesUserService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [JokesUserUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(JokesUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(JokesUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    jokesUserFormService = TestBed.inject(JokesUserFormService);
    jokesUserService = TestBed.inject(JokesUserService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const jokesUser: IJokesUser = { id: 456 };
      const internalUser: IUser = { id: 46746 };
      jokesUser.internalUser = internalUser;

      const userCollection: IUser[] = [{ id: 95076 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [internalUser];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ jokesUser });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const jokesUser: IJokesUser = { id: 456 };
      const internalUser: IUser = { id: 50546 };
      jokesUser.internalUser = internalUser;

      activatedRoute.data = of({ jokesUser });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(internalUser);
      expect(comp.jokesUser).toEqual(jokesUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJokesUser>>();
      const jokesUser = { id: 123 };
      jest.spyOn(jokesUserFormService, 'getJokesUser').mockReturnValue(jokesUser);
      jest.spyOn(jokesUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jokesUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: jokesUser }));
      saveSubject.complete();

      // THEN
      expect(jokesUserFormService.getJokesUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(jokesUserService.update).toHaveBeenCalledWith(expect.objectContaining(jokesUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJokesUser>>();
      const jokesUser = { id: 123 };
      jest.spyOn(jokesUserFormService, 'getJokesUser').mockReturnValue({ id: null });
      jest.spyOn(jokesUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jokesUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: jokesUser }));
      saveSubject.complete();

      // THEN
      expect(jokesUserFormService.getJokesUser).toHaveBeenCalled();
      expect(jokesUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJokesUser>>();
      const jokesUser = { id: 123 };
      jest.spyOn(jokesUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jokesUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(jokesUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
