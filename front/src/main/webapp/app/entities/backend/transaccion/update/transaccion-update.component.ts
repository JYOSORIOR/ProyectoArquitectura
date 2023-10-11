import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { TransaccionFormService, TransaccionFormGroup } from './transaccion-form.service';
import { ITransaccion } from '../transaccion.model';
import { TransaccionService } from '../service/transaccion.service';
import { ICuenta } from 'app/entities/backend/cuenta/cuenta.model';
import { CuentaService } from 'app/entities/backend/cuenta/service/cuenta.service';

@Component({
  selector: 'jhi-transaccion-update',
  templateUrl: './transaccion-update.component.html',
})
export class TransaccionUpdateComponent implements OnInit {
  isSaving = false;
  transaccion: ITransaccion | null = null;

  cuentasSharedCollection: ICuenta[] = [];

  editForm: TransaccionFormGroup = this.transaccionFormService.createTransaccionFormGroup();

  constructor(
    protected transaccionService: TransaccionService,
    protected transaccionFormService: TransaccionFormService,
    protected cuentaService: CuentaService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCuenta = (o1: ICuenta | null, o2: ICuenta | null): boolean => this.cuentaService.compareCuenta(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaccion }) => {
      this.transaccion = transaccion;
      if (transaccion) {
        this.updateForm(transaccion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transaccion = this.transaccionFormService.getTransaccion(this.editForm);
    if (transaccion.id !== null) {
      this.subscribeToSaveResponse(this.transaccionService.update(transaccion));
    } else {
      this.subscribeToSaveResponse(this.transaccionService.create(transaccion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaccion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(transaccion: ITransaccion): void {
    this.transaccion = transaccion;
    this.transaccionFormService.resetForm(this.editForm, transaccion);

    this.cuentasSharedCollection = this.cuentaService.addCuentaToCollectionIfMissing<ICuenta>(
      this.cuentasSharedCollection,
      transaccion.cuenta
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cuentaService
      .query()
      .pipe(map((res: HttpResponse<ICuenta[]>) => res.body ?? []))
      .pipe(map((cuentas: ICuenta[]) => this.cuentaService.addCuentaToCollectionIfMissing<ICuenta>(cuentas, this.transaccion?.cuenta)))
      .subscribe((cuentas: ICuenta[]) => (this.cuentasSharedCollection = cuentas));
  }
}
