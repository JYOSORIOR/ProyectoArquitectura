import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RetiroDetailComponent } from './retiro-detail.component';

describe('Retiro Management Detail Component', () => {
  let comp: RetiroDetailComponent;
  let fixture: ComponentFixture<RetiroDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RetiroDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ retiro: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(RetiroDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(RetiroDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load retiro on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.retiro).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
