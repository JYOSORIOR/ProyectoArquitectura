import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ATMFormService } from './atm-form.service';
import { ATMService } from '../service/atm.service';
import { IATM } from '../atm.model';
import { ITransaccion } from 'app/entities/backend/transaccion/transaccion.model';
import { TransaccionService } from 'app/entities/backend/transaccion/service/transaccion.service';

import { ATMUpdateComponent } from './atm-update.component';

describe('ATM Management Update Component', () => {
  let comp: ATMUpdateComponent;
  let fixture: ComponentFixture<ATMUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aTMFormService: ATMFormService;
  let aTMService: ATMService;
  let transaccionService: TransaccionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ATMUpdateComponent],
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
      .overrideTemplate(ATMUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ATMUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aTMFormService = TestBed.inject(ATMFormService);
    aTMService = TestBed.inject(ATMService);
    transaccionService = TestBed.inject(TransaccionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Transaccion query and add missing value', () => {
      const aTM: IATM = { id: 456 };
      const transacciones: ITransaccion = { id: 69900 };
      aTM.transacciones = transacciones;

      const transaccionCollection: ITransaccion[] = [{ id: 45818 }];
      jest.spyOn(transaccionService, 'query').mockReturnValue(of(new HttpResponse({ body: transaccionCollection })));
      const additionalTransaccions = [transacciones];
      const expectedCollection: ITransaccion[] = [...additionalTransaccions, ...transaccionCollection];
      jest.spyOn(transaccionService, 'addTransaccionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aTM });
      comp.ngOnInit();

      expect(transaccionService.query).toHaveBeenCalled();
      expect(transaccionService.addTransaccionToCollectionIfMissing).toHaveBeenCalledWith(
        transaccionCollection,
        ...additionalTransaccions.map(expect.objectContaining)
      );
      expect(comp.transaccionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const aTM: IATM = { id: 456 };
      const transacciones: ITransaccion = { id: 63905 };
      aTM.transacciones = transacciones;

      activatedRoute.data = of({ aTM });
      comp.ngOnInit();

      expect(comp.transaccionsSharedCollection).toContain(transacciones);
      expect(comp.aTM).toEqual(aTM);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IATM>>();
      const aTM = { id: 123 };
      jest.spyOn(aTMFormService, 'getATM').mockReturnValue(aTM);
      jest.spyOn(aTMService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aTM });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aTM }));
      saveSubject.complete();

      // THEN
      expect(aTMFormService.getATM).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(aTMService.update).toHaveBeenCalledWith(expect.objectContaining(aTM));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IATM>>();
      const aTM = { id: 123 };
      jest.spyOn(aTMFormService, 'getATM').mockReturnValue({ id: null });
      jest.spyOn(aTMService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aTM: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aTM }));
      saveSubject.complete();

      // THEN
      expect(aTMFormService.getATM).toHaveBeenCalled();
      expect(aTMService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IATM>>();
      const aTM = { id: 123 };
      jest.spyOn(aTMService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aTM });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aTMService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTransaccion', () => {
      it('Should forward to transaccionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(transaccionService, 'compareTransaccion');
        comp.compareTransaccion(entity, entity2);
        expect(transaccionService.compareTransaccion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
