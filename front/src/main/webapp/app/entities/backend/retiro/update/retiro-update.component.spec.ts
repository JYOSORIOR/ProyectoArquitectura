import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RetiroFormService } from './retiro-form.service';
import { RetiroService } from '../service/retiro.service';
import { IRetiro } from '../retiro.model';
import { IATM } from 'app/entities/backend/atm/atm.model';
import { ATMService } from 'app/entities/backend/atm/service/atm.service';

import { RetiroUpdateComponent } from './retiro-update.component';

describe('Retiro Management Update Component', () => {
  let comp: RetiroUpdateComponent;
  let fixture: ComponentFixture<RetiroUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let retiroFormService: RetiroFormService;
  let retiroService: RetiroService;
  let aTMService: ATMService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RetiroUpdateComponent],
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
      .overrideTemplate(RetiroUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RetiroUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    retiroFormService = TestBed.inject(RetiroFormService);
    retiroService = TestBed.inject(RetiroService);
    aTMService = TestBed.inject(ATMService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ATM query and add missing value', () => {
      const retiro: IRetiro = { id: 456 };
      const atm: IATM = { id: 64524 };
      retiro.atm = atm;

      const aTMCollection: IATM[] = [{ id: 23882 }];
      jest.spyOn(aTMService, 'query').mockReturnValue(of(new HttpResponse({ body: aTMCollection })));
      const additionalATMS = [atm];
      const expectedCollection: IATM[] = [...additionalATMS, ...aTMCollection];
      jest.spyOn(aTMService, 'addATMToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ retiro });
      comp.ngOnInit();

      expect(aTMService.query).toHaveBeenCalled();
      expect(aTMService.addATMToCollectionIfMissing).toHaveBeenCalledWith(aTMCollection, ...additionalATMS.map(expect.objectContaining));
      expect(comp.aTMSSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const retiro: IRetiro = { id: 456 };
      const atm: IATM = { id: 13880 };
      retiro.atm = atm;

      activatedRoute.data = of({ retiro });
      comp.ngOnInit();

      expect(comp.aTMSSharedCollection).toContain(atm);
      expect(comp.retiro).toEqual(retiro);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRetiro>>();
      const retiro = { id: 123 };
      jest.spyOn(retiroFormService, 'getRetiro').mockReturnValue(retiro);
      jest.spyOn(retiroService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ retiro });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: retiro }));
      saveSubject.complete();

      // THEN
      expect(retiroFormService.getRetiro).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(retiroService.update).toHaveBeenCalledWith(expect.objectContaining(retiro));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRetiro>>();
      const retiro = { id: 123 };
      jest.spyOn(retiroFormService, 'getRetiro').mockReturnValue({ id: null });
      jest.spyOn(retiroService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ retiro: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: retiro }));
      saveSubject.complete();

      // THEN
      expect(retiroFormService.getRetiro).toHaveBeenCalled();
      expect(retiroService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRetiro>>();
      const retiro = { id: 123 };
      jest.spyOn(retiroService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ retiro });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(retiroService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareATM', () => {
      it('Should forward to aTMService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(aTMService, 'compareATM');
        comp.compareATM(entity, entity2);
        expect(aTMService.compareATM).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
