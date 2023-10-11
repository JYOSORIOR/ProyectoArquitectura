import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ATMComponent } from './list/atm.component';
import { ATMDetailComponent } from './detail/atm-detail.component';
import { ATMUpdateComponent } from './update/atm-update.component';
import { ATMDeleteDialogComponent } from './delete/atm-delete-dialog.component';
import { ATMRoutingModule } from './route/atm-routing.module';

@NgModule({
  imports: [SharedModule, ATMRoutingModule],
  declarations: [ATMComponent, ATMDetailComponent, ATMUpdateComponent, ATMDeleteDialogComponent],
})
export class BackendATMModule {}
