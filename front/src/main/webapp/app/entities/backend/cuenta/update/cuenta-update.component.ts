import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CuentaFormService, CuentaFormGroup } from './cuenta-form.service';
import { ICuenta } from '../cuenta.model';
import { CuentaService } from '../service/cuenta.service';
import { ICliente } from 'app/entities/backend/cliente/cliente.model';
import { ClienteService } from 'app/entities/backend/cliente/service/cliente.service';
import { IRetiro } from 'app/entities/backend/retiro/retiro.model';
import { RetiroService } from 'app/entities/backend/retiro/service/retiro.service';

@Component({
  selector: 'jhi-cuenta-update',
  templateUrl: './cuenta-update.component.html',
})
export class CuentaUpdateComponent implements OnInit {
  isSaving = false;
  cuenta: ICuenta | null = null;

  clientesSharedCollection: ICliente[] = [];
  retirosSharedCollection: IRetiro[] = [];

  editForm: CuentaFormGroup = this.cuentaFormService.createCuentaFormGroup();

  constructor(
    protected cuentaService: CuentaService,
    protected cuentaFormService: CuentaFormService,
    protected clienteService: ClienteService,
    protected retiroService: RetiroService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCliente = (o1: ICliente | null, o2: ICliente | null): boolean => this.clienteService.compareCliente(o1, o2);

  compareRetiro = (o1: IRetiro | null, o2: IRetiro | null): boolean => this.retiroService.compareRetiro(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cuenta }) => {
      this.cuenta = cuenta;
      if (cuenta) {
        this.updateForm(cuenta);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cuenta = this.cuentaFormService.getCuenta(this.editForm);
    if (cuenta.id !== null) {
      this.subscribeToSaveResponse(this.cuentaService.update(cuenta));
    } else {
      this.subscribeToSaveResponse(this.cuentaService.create(cuenta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICuenta>>): void {
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

  protected updateForm(cuenta: ICuenta): void {
    this.cuenta = cuenta;
    this.cuentaFormService.resetForm(this.editForm, cuenta);

    this.clientesSharedCollection = this.clienteService.addClienteToCollectionIfMissing<ICliente>(
      this.clientesSharedCollection,
      cuenta.cliente
    );
    this.retirosSharedCollection = this.retiroService.addRetiroToCollectionIfMissing<IRetiro>(this.retirosSharedCollection, cuenta.retiros);
  }

  protected loadRelationshipsOptions(): void {
    this.clienteService
      .query()
      .pipe(map((res: HttpResponse<ICliente[]>) => res.body ?? []))
      .pipe(map((clientes: ICliente[]) => this.clienteService.addClienteToCollectionIfMissing<ICliente>(clientes, this.cuenta?.cliente)))
      .subscribe((clientes: ICliente[]) => (this.clientesSharedCollection = clientes));

    this.retiroService
      .query()
      .pipe(map((res: HttpResponse<IRetiro[]>) => res.body ?? []))
      .pipe(map((retiros: IRetiro[]) => this.retiroService.addRetiroToCollectionIfMissing<IRetiro>(retiros, this.cuenta?.retiros)))
      .subscribe((retiros: IRetiro[]) => (this.retirosSharedCollection = retiros));
  }
}
