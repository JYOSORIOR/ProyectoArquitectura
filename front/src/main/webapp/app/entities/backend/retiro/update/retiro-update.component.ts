import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RetiroFormService, RetiroFormGroup } from './retiro-form.service';
import { IRetiro } from '../retiro.model';
import { RetiroService } from '../service/retiro.service';
import { IATM } from 'app/entities/backend/atm/atm.model';
import { ATMService } from 'app/entities/backend/atm/service/atm.service';

@Component({
  selector: 'jhi-retiro-update',
  templateUrl: './retiro-update.component.html',
})
export class RetiroUpdateComponent implements OnInit {
  isSaving = false;
  retiro: IRetiro | null = null;

  aTMSSharedCollection: IATM[] = [];

  editForm: RetiroFormGroup = this.retiroFormService.createRetiroFormGroup();

  constructor(
    protected retiroService: RetiroService,
    protected retiroFormService: RetiroFormService,
    protected aTMService: ATMService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareATM = (o1: IATM | null, o2: IATM | null): boolean => this.aTMService.compareATM(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ retiro }) => {
      this.retiro = retiro;
      if (retiro) {
        this.updateForm(retiro);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const retiro = this.retiroFormService.getRetiro(this.editForm);
    if (retiro.id !== null) {
      this.subscribeToSaveResponse(this.retiroService.update(retiro));
    } else {
      this.subscribeToSaveResponse(this.retiroService.create(retiro));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRetiro>>): void {
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

  protected updateForm(retiro: IRetiro): void {
    this.retiro = retiro;
    this.retiroFormService.resetForm(this.editForm, retiro);

    this.aTMSSharedCollection = this.aTMService.addATMToCollectionIfMissing<IATM>(this.aTMSSharedCollection, retiro.atm);
  }

  protected loadRelationshipsOptions(): void {
    this.aTMService
      .query()
      .pipe(map((res: HttpResponse<IATM[]>) => res.body ?? []))
      .pipe(map((aTMS: IATM[]) => this.aTMService.addATMToCollectionIfMissing<IATM>(aTMS, this.retiro?.atm)))
      .subscribe((aTMS: IATM[]) => (this.aTMSSharedCollection = aTMS));
  }
}
