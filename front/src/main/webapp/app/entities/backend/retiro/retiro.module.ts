import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RetiroComponent } from './list/retiro.component';
import { RetiroDetailComponent } from './detail/retiro-detail.component';
import { RetiroUpdateComponent } from './update/retiro-update.component';
import { RetiroDeleteDialogComponent } from './delete/retiro-delete-dialog.component';
import { RetiroRoutingModule } from './route/retiro-routing.module';

@NgModule({
  imports: [SharedModule, RetiroRoutingModule],
  declarations: [RetiroComponent, RetiroDetailComponent, RetiroUpdateComponent, RetiroDeleteDialogComponent],
})
export class BackendRetiroModule {}
