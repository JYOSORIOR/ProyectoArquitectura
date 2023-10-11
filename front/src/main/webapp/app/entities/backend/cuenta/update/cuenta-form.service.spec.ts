import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../cuenta.test-samples';

import { CuentaFormService } from './cuenta-form.service';

describe('Cuenta Form Service', () => {
  let service: CuentaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CuentaFormService);
  });

  describe('Service methods', () => {
    describe('createCuentaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCuentaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipo: expect.any(Object),
            estado: expect.any(Object),
            saldo: expect.any(Object),
            cliente: expect.any(Object),
            retiros: expect.any(Object),
          })
        );
      });

      it('passing ICuenta should create a new form with FormGroup', () => {
        const formGroup = service.createCuentaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipo: expect.any(Object),
            estado: expect.any(Object),
            saldo: expect.any(Object),
            cliente: expect.any(Object),
            retiros: expect.any(Object),
          })
        );
      });
    });

    describe('getCuenta', () => {
      it('should return NewCuenta for default Cuenta initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCuentaFormGroup(sampleWithNewData);

        const cuenta = service.getCuenta(formGroup) as any;

        expect(cuenta).toMatchObject(sampleWithNewData);
      });

      it('should return NewCuenta for empty Cuenta initial value', () => {
        const formGroup = service.createCuentaFormGroup();

        const cuenta = service.getCuenta(formGroup) as any;

        expect(cuenta).toMatchObject({});
      });

      it('should return ICuenta', () => {
        const formGroup = service.createCuentaFormGroup(sampleWithRequiredData);

        const cuenta = service.getCuenta(formGroup) as any;

        expect(cuenta).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICuenta should not enable id FormControl', () => {
        const formGroup = service.createCuentaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCuenta should disable id FormControl', () => {
        const formGroup = service.createCuentaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
