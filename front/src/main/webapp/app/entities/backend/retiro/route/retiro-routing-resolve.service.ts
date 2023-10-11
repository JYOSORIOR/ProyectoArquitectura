import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRetiro } from '../retiro.model';
import { RetiroService } from '../service/retiro.service';

@Injectable({ providedIn: 'root' })
export class RetiroRoutingResolveService implements Resolve<IRetiro | null> {
  constructor(protected service: RetiroService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRetiro | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((retiro: HttpResponse<IRetiro>) => {
          if (retiro.body) {
            return of(retiro.body);
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
