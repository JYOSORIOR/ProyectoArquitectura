import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRetiro } from '../retiro.model';

@Component({
  selector: 'jhi-retiro-detail',
  templateUrl: './retiro-detail.component.html',
})
export class RetiroDetailComponent implements OnInit {
  retiro: IRetiro | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ retiro }) => {
      this.retiro = retiro;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
