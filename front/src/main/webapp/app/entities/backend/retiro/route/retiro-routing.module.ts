import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RetiroComponent } from '../list/retiro.component';
import { RetiroDetailComponent } from '../detail/retiro-detail.component';
import { RetiroUpdateComponent } from '../update/retiro-update.component';
import { RetiroRoutingResolveService } from './retiro-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const retiroRoute: Routes = [
  {
    path: '',
    component: RetiroComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RetiroDetailComponent,
    resolve: {
      retiro: RetiroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RetiroUpdateComponent,
    resolve: {
      retiro: RetiroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RetiroUpdateComponent,
    resolve: {
      retiro: RetiroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(retiroRoute)],
  exports: [RouterModule],
})
export class RetiroRoutingModule {}
