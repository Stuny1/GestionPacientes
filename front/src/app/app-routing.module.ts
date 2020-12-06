import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';
import {AppComponent} from './app.component';
import {InicioComponent} from './component/inicio/inicio.component';
import {AgendaComponent} from './component/agenda/agenda.component';
import {PacientesComponent} from './component/pacientes/pacientes.component';


const routes: Routes = [
  {path: 'Login',
    loadChildren: () => import('./login/login.module').then( m => m.LoginPageModule)
  },
  {path: 'Inicio',component: InicioComponent},
  {path: 'Agenda',component: AgendaComponent,
  children: [
    {path: ':id',component: AgendaComponent}
  ]},
  {path: 'Pacientes',component: PacientesComponent}

];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {}
