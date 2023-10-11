import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ATMFormService, ATMFormGroup } from './atm-form.service';
import { IATM } from '../atm.model';
import { ATMService } from '../service/atm.service';
import { ITransaccion } from 'app/entities/backend/transaccion/transaccion.model';
import { TransaccionService } from 'app/entities/backend/transaccion/service/transaccion.service';

@Component({
  selector: 'jhi-atm-update',
  templateUrl: './atm-update.component.html',
})
export class ATMUpdateComponent implements OnInit {
  isSaving = false;
  aTM: IATM | null = null;

  transaccionsSharedCollection: ITransaccion[] = [];

  editForm: ATMFormGroup = this.aTMFormService.createATMFormGroup();

  constructor(
    protected aTMService: ATMService,
    protected aTMFormService: ATMFormService,
    protected transaccionService: TransaccionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTransaccion = (o1: ITransaccion | null, o2: ITransaccion | null): boolean => this.transaccionService.compareTransaccion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aTM }) => {
      this.aTM = aTM;
      if (aTM) {
        this.updateForm(aTM);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aTM = this.aTMFormService.getATM(this.editForm);
    if (aTM.id !== null) {
      this.subscribeToSaveResponse(this.aTMService.update(aTM));
    } else {
      this.subscribeToSaveResponse(this.aTMService.create(aTM));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IATM>>): void {
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

  protected updateForm(aTM: IATM): void {
    this.aTM = aTM;
    this.aTMFormService.resetForm(this.editForm, aTM);

    this.transaccionsSharedCollection = this.transaccionService.addTransaccionToCollectionIfMissing<ITransaccion>(
      this.transaccionsSharedCollection,
      aTM.transacciones
    );
  }

  protected loadRelationshipsOptions(): void {
    this.transaccionService
      .query()
      .pipe(map((res: HttpResponse<ITransaccion[]>) => res.body ?? []))
      .pipe(
        map((transaccions: ITransaccion[]) =>
          this.transaccionService.addTransaccionToCollectionIfMissing<ITransaccion>(transaccions, this.aTM?.transacciones)
        )
      )
      .subscribe((transaccions: ITransaccion[]) => (this.transaccionsSharedCollection = transaccions));
  }
}
