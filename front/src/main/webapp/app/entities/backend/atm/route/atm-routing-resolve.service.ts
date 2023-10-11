import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IATM } from '../atm.model';
import { ATMService } from '../service/atm.service';

@Injectable({ providedIn: 'root' })
export class ATMRoutingResolveService implements Resolve<IATM | null> {
  constructor(protected service: ATMService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IATM | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aTM: HttpResponse<IATM>) => {
          if (aTM.body) {
            return of(aTM.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
