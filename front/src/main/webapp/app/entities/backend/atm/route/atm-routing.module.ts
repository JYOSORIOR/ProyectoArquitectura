import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ATMComponent } from '../list/atm.component';
import { ATMDetailComponent } from '../detail/atm-detail.component';
import { ATMUpdateComponent } from '../update/atm-update.component';
import { ATMRoutingResolveService } from './atm-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const aTMRoute: Routes = [
  {
    path: '',
    component: ATMComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ATMDetailComponent,
    resolve: {
      aTM: ATMRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ATMUpdateComponent,
    resolve: {
      aTM: ATMRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ATMUpdateComponent,
    resolve: {
      aTM: ATMRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aTMRoute)],
  exports: [RouterModule],
})
export class ATMRoutingModule {}
