import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITransaccion, NewTransaccion } from '../transaccion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransaccion for edit and NewTransaccionFormGroupInput for create.
 */
type TransaccionFormGroupInput = ITransaccion | PartialWithRequiredKeyOf<NewTransaccion>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITransaccion | NewTransaccion> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

type TransaccionFormRawValue = FormValueOf<ITransaccion>;

type NewTransaccionFormRawValue = FormValueOf<NewTransaccion>;

type TransaccionFormDefaults = Pick<NewTransaccion, 'id' | 'fecha'>;

type TransaccionFormGroupContent = {
  id: FormControl<TransaccionFormRawValue['id'] | NewTransaccion['id']>;
  estado: FormControl<TransaccionFormRawValue['estado']>;
  fecha: FormControl<TransaccionFormRawValue['fecha']>;
  cantidad: FormControl<TransaccionFormRawValue['cantidad']>;
  cuenta: FormControl<TransaccionFormRawValue['cuenta']>;
};

export type TransaccionFormGroup = FormGroup<TransaccionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransaccionFormService {
  createTransaccionFormGroup(transaccion: TransaccionFormGroupInput = { id: null }): TransaccionFormGroup {
    const transaccionRawValue = this.convertTransaccionToTransaccionRawValue({
      ...this.getFormDefaults(),
      ...transaccion,
    });
    return new FormGroup<TransaccionFormGroupContent>({
      id: new FormControl(
        { value: transaccionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      estado: new FormControl(transaccionRawValue.estado, {
        validators: [Validators.required],
      }),
      fecha: new FormControl(transaccionRawValue.fecha, {
        validators: [Validators.required],
      }),
      cantidad: new FormControl(transaccionRawValue.cantidad, {
        validators: [Validators.required],
      }),
      cuenta: new FormControl(transaccionRawValue.cuenta),
    });
  }

  getTransaccion(form: TransaccionFormGroup): ITransaccion | NewTransaccion {
    return this.convertTransaccionRawValueToTransaccion(form.getRawValue() as TransaccionFormRawValue | NewTransaccionFormRawValue);
  }

  resetForm(form: TransaccionFormGroup, transaccion: TransaccionFormGroupInput): void {
    const transaccionRawValue = this.convertTransaccionToTransaccionRawValue({ ...this.getFormDefaults(), ...transaccion });
    form.reset(
      {
        ...transaccionRawValue,
        id: { value: transaccionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TransaccionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fecha: currentTime,
    };
  }

  private convertTransaccionRawValueToTransaccion(
    rawTransaccion: TransaccionFormRawValue | NewTransaccionFormRawValue
  ): ITransaccion | NewTransaccion {
    return {
      ...rawTransaccion,
      fecha: dayjs(rawTransaccion.fecha, DATE_TIME_FORMAT),
    };
  }

  private convertTransaccionToTransaccionRawValue(
    transaccion: ITransaccion | (Partial<NewTransaccion> & TransaccionFormDefaults)
  ): TransaccionFormRawValue | PartialWithRequiredKeyOf<NewTransaccionFormRawValue> {
    return {
      ...transaccion,
      fecha: transaccion.fecha ? transaccion.fecha.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
