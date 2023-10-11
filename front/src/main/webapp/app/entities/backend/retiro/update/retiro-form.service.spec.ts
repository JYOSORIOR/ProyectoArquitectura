import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../retiro.test-samples';

import { RetiroFormService } from './retiro-form.service';

describe('Retiro Form Service', () => {
  let service: RetiroFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RetiroFormService);
  });

  describe('Service methods', () => {
    describe('createRetiroFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRetiroFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contiene: expect.any(Object),
            estado: expect.any(Object),
            cantidad: expect.any(Object),
            fecha: expect.any(Object),
            atm: expect.any(Object),
          })
        );
      });

      it('passing IRetiro should create a new form with FormGroup', () => {
        const formGroup = service.createRetiroFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            contiene: expect.any(Object),
            estado: expect.any(Object),
            cantidad: expect.any(Object),
            fecha: expect.any(Object),
            atm: expect.any(Object),
          })
        );
      });
    });

    describe('getRetiro', () => {
      it('should return NewRetiro for default Retiro initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRetiroFormGroup(sampleWithNewData);

        const retiro = service.getRetiro(formGroup) as any;

        expect(retiro).toMatchObject(sampleWithNewData);
      });

      it('should return NewRetiro for empty Retiro initial value', () => {
        const formGroup = service.createRetiroFormGroup();

        const retiro = service.getRetiro(formGroup) as any;

        expect(retiro).toMatchObject({});
      });

      it('should return IRetiro', () => {
        const formGroup = service.createRetiroFormGroup(sampleWithRequiredData);

        const retiro = service.getRetiro(formGroup) as any;

        expect(retiro).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRetiro should not enable id FormControl', () => {
        const formGroup = service.createRetiroFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRetiro should disable id FormControl', () => {
        const formGroup = service.createRetiroFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
