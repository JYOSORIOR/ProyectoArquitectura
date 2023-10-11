import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CuentaComponent } from './list/cuenta.component';
import { CuentaDetailComponent } from './detail/cuenta-detail.component';
import { CuentaUpdateComponent } from './update/cuenta-update.component';
import { CuentaDeleteDialogComponent } from './delete/cuenta-delete-dialog.component';
import { CuentaRoutingModule } from './route/cuenta-routing.module';

@NgModule({
  imports: [SharedModule, CuentaRoutingModule],
  declarations: [CuentaComponent, CuentaDetailComponent, CuentaUpdateComponent, CuentaDeleteDialogComponent],
})
export class BackendCuentaModule {}
