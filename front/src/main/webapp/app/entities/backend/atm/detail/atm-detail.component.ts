import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IATM } from '../atm.model';

@Component({
  selector: 'jhi-atm-detail',
  templateUrl: './atm-detail.component.html',
})
export class ATMDetailComponent implements OnInit {
  aTM: IATM | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aTM }) => {
      this.aTM = aTM;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
