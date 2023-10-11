import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CuentaFormService } from './cuenta-form.service';
import { CuentaService } from '../service/cuenta.service';
import { ICuenta } from '../cuenta.model';
import { ICliente } from 'app/entities/backend/cliente/cliente.model';
import { ClienteService } from 'app/entities/backend/cliente/service/cliente.service';
import { IRetiro } from 'app/entities/backend/retiro/retiro.model';
import { RetiroService } from 'app/entities/backend/retiro/service/retiro.service';

import { CuentaUpdateComponent } from './cuenta-update.component';

describe('Cuenta Management Update Component', () => {
  let comp: CuentaUpdateComponent;
  let fixture: ComponentFixture<CuentaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cuentaFormService: CuentaFormService;
  let cuentaService: CuentaService;
  let clienteService: ClienteService;
  let retiroService: RetiroService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CuentaUpdateComponent],
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
      .overrideTemplate(CuentaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CuentaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cuentaFormService = TestBed.inject(CuentaFormService);
    cuentaService = TestBed.inject(CuentaService);
    clienteService = TestBed.inject(ClienteService);
    retiroService = TestBed.inject(RetiroService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cliente query and add missing value', () => {
      const cuenta: ICuenta = { id: 456 };
      const cliente: ICliente = { id: 3925 };
      cuenta.cliente = cliente;

      const clienteCollection: ICliente[] = [{ id: 35963 }];
      jest.spyOn(clienteService, 'query').mockReturnValue(of(new HttpResponse({ body: clienteCollection })));
      const additionalClientes = [cliente];
      const expectedCollection: ICliente[] = [...additionalClientes, ...clienteCollection];
      jest.spyOn(clienteService, 'addClienteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cuenta });
      comp.ngOnInit();

      expect(clienteService.query).toHaveBeenCalled();
      expect(clienteService.addClienteToCollectionIfMissing).toHaveBeenCalledWith(
        clienteCollection,
        ...additionalClientes.map(expect.objectContaining)
      );
      expect(comp.clientesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Retiro query and add missing value', () => {
      const cuenta: ICuenta = { id: 456 };
      const retiros: IRetiro = { id: 67026 };
      cuenta.retiros = retiros;

      const retiroCollection: IRetiro[] = [{ id: 63802 }];
      jest.spyOn(retiroService, 'query').mockReturnValue(of(new HttpResponse({ body: retiroCollection })));
      const additionalRetiros = [retiros];
      const expectedCollection: IRetiro[] = [...additionalRetiros, ...retiroCollection];
      jest.spyOn(retiroService, 'addRetiroToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cuenta });
      comp.ngOnInit();

      expect(retiroService.query).toHaveBeenCalled();
      expect(retiroService.addRetiroToCollectionIfMissing).toHaveBeenCalledWith(
        retiroCollection,
        ...additionalRetiros.map(expect.objectContaining)
      );
      expect(comp.retirosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cuenta: ICuenta = { id: 456 };
      const cliente: ICliente = { id: 40878 };
      cuenta.cliente = cliente;
      const retiros: IRetiro = { id: 99604 };
      cuenta.retiros = retiros;

      activatedRoute.data = of({ cuenta });
      comp.ngOnInit();

      expect(comp.clientesSharedCollection).toContain(cliente);
      expect(comp.retirosSharedCollection).toContain(retiros);
      expect(comp.cuenta).toEqual(cuenta);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICuenta>>();
      const cuenta = { id: 123 };
      jest.spyOn(cuentaFormService, 'getCuenta').mockReturnValue(cuenta);
      jest.spyOn(cuentaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cuenta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cuenta }));
      saveSubject.complete();

      // THEN
      expect(cuentaFormService.getCuenta).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cuentaService.update).toHaveBeenCalledWith(expect.objectContaining(cuenta));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICuenta>>();
      const cuenta = { id: 123 };
      jest.spyOn(cuentaFormService, 'getCuenta').mockReturnValue({ id: null });
      jest.spyOn(cuentaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cuenta: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cuenta }));
      saveSubject.complete();

      // THEN
      expect(cuentaFormService.getCuenta).toHaveBeenCalled();
      expect(cuentaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICuenta>>();
      const cuenta = { id: 123 };
      jest.spyOn(cuentaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cuenta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cuentaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCliente', () => {
      it('Should forward to clienteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(clienteService, 'compareCliente');
        comp.compareCliente(entity, entity2);
        expect(clienteService.compareCliente).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRetiro', () => {
      it('Should forward to retiroService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(retiroService, 'compareRetiro');
        comp.compareRetiro(entity, entity2);
        expect(retiroService.compareRetiro).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
