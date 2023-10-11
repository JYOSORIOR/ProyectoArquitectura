import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ATMDetailComponent } from './atm-detail.component';

describe('ATM Management Detail Component', () => {
  let comp: ATMDetailComponent;
  let fixture: ComponentFixture<ATMDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ATMDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ aTM: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ATMDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ATMDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load aTM on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.aTM).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
