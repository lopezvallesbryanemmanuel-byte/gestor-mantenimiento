import { Routes } from '@angular/router';

import { adminGuard, authGuard } from './core/auth/auth.guard';
import { EquipoDetail } from './features/equipos/equipo-detail/equipo-detail';
import { EquipoForm } from './features/equipos/equipo-form/equipo-form';
import { EquipoList } from './features/equipos/equipo-list/equipo-list';
import { Login } from './features/login/login';
import { SitioList } from './features/sitios/sitio-list/sitio-list';

export const routes: Routes = [
  { path: 'login', component: Login },
  {
    path: '',
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'equipos', pathMatch: 'full' },
      { path: 'sitios', component: SitioList },
      { path: 'equipos', component: EquipoList },
      { path: 'equipos/nuevo', component: EquipoForm, canActivate: [adminGuard] },
      { path: 'equipos/:id', component: EquipoDetail },
      { path: 'equipos/:id/editar', component: EquipoForm, canActivate: [adminGuard] },
    ],
  },
  { path: '**', redirectTo: '' },
];