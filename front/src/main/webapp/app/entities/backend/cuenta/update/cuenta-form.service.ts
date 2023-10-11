import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICuenta, NewCuenta } from '../cuenta.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICuenta for edit and NewCuentaFormGroupInput for create.
 */
type CuentaFormGroupInput = ICuenta | PartialWithRequiredKeyOf<NewCuenta>;

type CuentaFormDefaults = Pick<NewCuenta, 'id'>;

type CuentaFormGroupContent = {
  id: FormControl<ICuenta['id'] | NewCuenta['id']>;
  tipo: FormControl<ICuenta['tipo']>;
  estado: FormControl<ICuenta['estado']>;
  saldo: FormControl<ICuenta['saldo']>;
  cliente: FormControl<ICuenta['cliente']>;
  retiros: FormControl<ICuenta['retiros']>;
};

export type CuentaFormGroup = FormGroup<CuentaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CuentaFormService {
  createCuentaFormGroup(cuenta: CuentaFormGroupInput = { id: null }): CuentaFormGroup {
    const cuentaRawValue = {
      ...this.getFormDefaults(),
      ...cuenta,
    };
    return new FormGroup<CuentaFormGroupContent>({
      id: new FormControl(
        { value: cuentaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      tipo: new FormControl(cuentaRawValue.tipo, {
        validators: [Validators.required],
      }),
      estado: new FormControl(cuentaRawValue.estado, {
        validators: [Validators.required],
      }),
      saldo: new FormControl(cuentaRawValue.saldo, {
        validators: [Validators.required],
      }),
      cliente: new FormControl(cuentaRawValue.cliente),
      retiros: new FormControl(cuentaRawValue.retiros),
    });
  }

  getCuenta(form: CuentaFormGroup): ICuenta | NewCuenta {
    return form.getRawValue() as ICuenta | NewCuenta;
  }

  resetForm(form: CuentaFormGroup, cuenta: CuentaFormGroupInput): void {
    const cuentaRawValue = { ...this.getFormDefaults(), ...cuenta };
    form.reset(
      {
        ...cuentaRawValue,
        id: { value: cuentaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CuentaFormDefaults {
    return {
      id: null,
    };
  }
}
