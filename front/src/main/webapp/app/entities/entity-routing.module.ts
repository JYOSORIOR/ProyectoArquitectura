import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'cliente',
        data: { pageTitle: 'frontApp.backendCliente.home.title' },
        loadChildren: () => import('./backend/cliente/cliente.module').then(m => m.BackendClienteModule),
      },
      {
        path: 'cuenta',
        data: { pageTitle: 'frontApp.backendCuenta.home.title' },
        loadChildren: () => import('./backend/cuenta/cuenta.module').then(m => m.BackendCuentaModule),
      },
      {
        path: 'retiro',
        data: { pageTitle: 'frontApp.backendRetiro.home.title' },
        loadChildren: () => import('./backend/retiro/retiro.module').then(m => m.BackendRetiroModule),
      },
      {
        path: 'atm',
        data: { pageTitle: 'frontApp.backendATm.home.title' },
        loadChildren: () => import('./backend/atm/atm.module').then(m => m.BackendATMModule),
      },
      {
        path: 'transaccion',
        data: { pageTitle: 'frontApp.backendTransaccion.home.title' },
        loadChildren: () => import('./backend/transaccion/transaccion.module').then(m => m.BackendTransaccionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
