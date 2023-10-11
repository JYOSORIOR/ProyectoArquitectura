import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TransaccionFormService } from './transaccion-form.service';
import { TransaccionService } from '../service/transaccion.service';
import { ITransaccion } from '../transaccion.model';
import { ICuenta } from 'app/entities/backend/cuenta/cuenta.model';
import { CuentaService } from 'app/entities/backend/cuenta/service/cuenta.service';

import { TransaccionUpdateComponent } from './transaccion-update.component';

describe('Transaccion Management Update Component', () => {
  let comp: TransaccionUpdateComponent;
  let fixture: ComponentFixture<TransaccionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transaccionFormService: TransaccionFormService;
  let transaccionService: TransaccionService;
  let cuentaService: CuentaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TransaccionUpdateComponent],
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
      .overrideTemplate(TransaccionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransaccionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transaccionFormService = TestBed.inject(TransaccionFormService);
    transaccionService = TestBed.inject(TransaccionService);
    cuentaService = TestBed.inject(CuentaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cuenta query and add missing value', () => {
      const transaccion: ITransaccion = { id: 456 };
      const cuenta: ICuenta = { id: 23571 };
      transaccion.cuenta = cuenta;

      const cuentaCollection: ICuenta[] = [{ id: 23224 }];
      jest.spyOn(cuentaService, 'query').mockReturnValue(of(new HttpResponse({ body: cuentaCollection })));
      const additionalCuentas = [cuenta];
      const expectedCollection: ICuenta[] = [...additionalCuentas, ...cuentaCollection];
      jest.spyOn(cuentaService, 'addCuentaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transaccion });
      comp.ngOnInit();

      expect(cuentaService.query).toHaveBeenCalled();
      expect(cuentaService.addCuentaToCollectionIfMissing).toHaveBeenCalledWith(
        cuentaCollection,
        ...additionalCuentas.map(expect.objectContaining)
      );
      expect(comp.cuentasSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const transaccion: ITransaccion = { id: 456 };
      const cuenta: ICuenta = { id: 90189 };
      transaccion.cuenta = cuenta;

      activatedRoute.data = of({ transaccion });
      comp.ngOnInit();

      expect(comp.cuentasSharedCollection).toContain(cuenta);
      expect(comp.transaccion).toEqual(transaccion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransaccion>>();
      const transaccion = { id: 123 };
      jest.spyOn(transaccionFormService, 'getTransaccion').mockReturnValue(transaccion);
      jest.spyOn(transaccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transaccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transaccion }));
      saveSubject.complete();

      // THEN
      expect(transaccionFormService.getTransaccion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transaccionService.update).toHaveBeenCalledWith(expect.objectContaining(transaccion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransaccion>>();
      const transaccion = { id: 123 };
      jest.spyOn(transaccionFormService, 'getTransaccion').mockReturnValue({ id: null });
      jest.spyOn(transaccionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transaccion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transaccion }));
      saveSubject.complete();

      // THEN
      expect(transaccionFormService.getTransaccion).toHaveBeenCalled();
      expect(transaccionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransaccion>>();
      const transaccion = { id: 123 };
      jest.spyOn(transaccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transaccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transaccionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCuenta', () => {
      it('Should forward to cuentaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(cuentaService, 'compareCuenta');
        comp.compareCuenta(entity, entity2);
        expect(cuentaService.compareCuenta).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
