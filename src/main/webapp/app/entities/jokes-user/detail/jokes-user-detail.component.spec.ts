import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JokesUserDetailComponent } from './jokes-user-detail.component';

describe('JokesUser Management Detail Component', () => {
  let comp: JokesUserDetailComponent;
  let fixture: ComponentFixture<JokesUserDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [JokesUserDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ jokesUser: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(JokesUserDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(JokesUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load jokesUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.jokesUser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
