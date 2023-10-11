import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IATM, NewATM } from '../atm.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IATM for edit and NewATMFormGroupInput for create.
 */
type ATMFormGroupInput = IATM | PartialWithRequiredKeyOf<NewATM>;

type ATMFormDefaults = Pick<NewATM, 'id'>;

type ATMFormGroupContent = {
  id: FormControl<IATM['id'] | NewATM['id']>;
  estado: FormControl<IATM['estado']>;
  tipo: FormControl<IATM['tipo']>;
  saldodisponible: FormControl<IATM['saldodisponible']>;
  ubicacion: FormControl<IATM['ubicacion']>;
  transacciones: FormControl<IATM['transacciones']>;
};

export type ATMFormGroup = FormGroup<ATMFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ATMFormService {
  createATMFormGroup(aTM: ATMFormGroupInput = { id: null }): ATMFormGroup {
    const aTMRawValue = {
      ...this.getFormDefaults(),
      ...aTM,
    };
    return new FormGroup<ATMFormGroupContent>({
      id: new FormControl(
        { value: aTMRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      estado: new FormControl(aTMRawValue.estado, {
        validators: [Validators.required],
      }),
      tipo: new FormControl(aTMRawValue.tipo, {
        validators: [Validators.required],
      }),
      saldodisponible: new FormControl(aTMRawValue.saldodisponible, {
        validators: [Validators.required],
      }),
      ubicacion: new FormControl(aTMRawValue.ubicacion, {
        validators: [Validators.required],
      }),
      transacciones: new FormControl(aTMRawValue.transacciones),
    });
  }

  getATM(form: ATMFormGroup): IATM | NewATM {
    return form.getRawValue() as IATM | NewATM;
  }

  resetForm(form: ATMFormGroup, aTM: ATMFormGroupInput): void {
    const aTMRawValue = { ...this.getFormDefaults(), ...aTM };
    form.reset(
      {
        ...aTMRawValue,
        id: { value: aTMRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ATMFormDefaults {
    return {
      id: null,
    };
  }
}
