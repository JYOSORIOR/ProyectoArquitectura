import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRetiro, NewRetiro } from '../retiro.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRetiro for edit and NewRetiroFormGroupInput for create.
 */
type RetiroFormGroupInput = IRetiro | PartialWithRequiredKeyOf<NewRetiro>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRetiro | NewRetiro> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

type RetiroFormRawValue = FormValueOf<IRetiro>;

type NewRetiroFormRawValue = FormValueOf<NewRetiro>;

type RetiroFormDefaults = Pick<NewRetiro, 'id' | 'fecha'>;

type RetiroFormGroupContent = {
  id: FormControl<RetiroFormRawValue['id'] | NewRetiro['id']>;
  contiene: FormControl<RetiroFormRawValue['contiene']>;
  estado: FormControl<RetiroFormRawValue['estado']>;
  cantidad: FormControl<RetiroFormRawValue['cantidad']>;
  fecha: FormControl<RetiroFormRawValue['fecha']>;
  atm: FormControl<RetiroFormRawValue['atm']>;
};

export type RetiroFormGroup = FormGroup<RetiroFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RetiroFormService {
  createRetiroFormGroup(retiro: RetiroFormGroupInput = { id: null }): RetiroFormGroup {
    const retiroRawValue = this.convertRetiroToRetiroRawValue({
      ...this.getFormDefaults(),
      ...retiro,
    });
    return new FormGroup<RetiroFormGroupContent>({
      id: new FormControl(
        { value: retiroRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      contiene: new FormControl(retiroRawValue.contiene, {
        validators: [Validators.required],
      }),
      estado: new FormControl(retiroRawValue.estado, {
        validators: [Validators.required],
      }),
      cantidad: new FormControl(retiroRawValue.cantidad, {
        validators: [Validators.required],
      }),
      fecha: new FormControl(retiroRawValue.fecha, {
        validators: [Validators.required],
      }),
      atm: new FormControl(retiroRawValue.atm),
    });
  }

  getRetiro(form: RetiroFormGroup): IRetiro | NewRetiro {
    return this.convertRetiroRawValueToRetiro(form.getRawValue() as RetiroFormRawValue | NewRetiroFormRawValue);
  }

  resetForm(form: RetiroFormGroup, retiro: RetiroFormGroupInput): void {
    const retiroRawValue = this.convertRetiroToRetiroRawValue({ ...this.getFormDefaults(), ...retiro });
    form.reset(
      {
        ...retiroRawValue,
        id: { value: retiroRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RetiroFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fecha: currentTime,
    };
  }

  private convertRetiroRawValueToRetiro(rawRetiro: RetiroFormRawValue | NewRetiroFormRawValue): IRetiro | NewRetiro {
    return {
      ...rawRetiro,
      fecha: dayjs(rawRetiro.fecha, DATE_TIME_FORMAT),
    };
  }

  private convertRetiroToRetiroRawValue(
    retiro: IRetiro | (Partial<NewRetiro> & RetiroFormDefaults)
  ): RetiroFormRawValue | PartialWithRequiredKeyOf<NewRetiroFormRawValue> {
    return {
      ...retiro,
      fecha: retiro.fecha ? retiro.fecha.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
