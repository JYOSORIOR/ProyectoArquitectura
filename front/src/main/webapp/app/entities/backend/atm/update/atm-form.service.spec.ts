import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../atm.test-samples';

import { ATMFormService } from './atm-form.service';

describe('ATM Form Service', () => {
  let service: ATMFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ATMFormService);
  });

  describe('Service methods', () => {
    describe('createATMFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createATMFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            estado: expect.any(Object),
            tipo: expect.any(Object),
            saldodisponible: expect.any(Object),
            ubicacion: expect.any(Object),
            transacciones: expect.any(Object),
          })
        );
      });

      it('passing IATM should create a new form with FormGroup', () => {
        const formGroup = service.createATMFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            estado: expect.any(Object),
            tipo: expect.any(Object),
            saldodisponible: expect.any(Object),
            ubicacion: expect.any(Object),
            transacciones: expect.any(Object),
          })
        );
      });
    });

    describe('getATM', () => {
      it('should return NewATM for default ATM initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createATMFormGroup(sampleWithNewData);

        const aTM = service.getATM(formGroup) as any;

        expect(aTM).toMatchObject(sampleWithNewData);
      });

      it('should return NewATM for empty ATM initial value', () => {
        const formGroup = service.createATMFormGroup();

        const aTM = service.getATM(formGroup) as any;

        expect(aTM).toMatchObject({});
      });

      it('should return IATM', () => {
        const formGroup = service.createATMFormGroup(sampleWithRequiredData);

        const aTM = service.getATM(formGroup) as any;

        expect(aTM).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IATM should not enable id FormControl', () => {
        const formGroup = service.createATMFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewATM should disable id FormControl', () => {
        const formGroup = service.createATMFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
