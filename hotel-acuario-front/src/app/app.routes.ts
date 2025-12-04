import { Routes } from '@angular/router';

import { ClienteListComponent } from './components/clientes/cliente-list/cliente-list.component';
import { ClienteFormComponent } from './components/clientes/cliente-form/cliente-form.component';

import { HabitacionListComponent } from './components/habitaciones/habitacion-list/habitacion-list.component';
import { HabitacionFormComponent } from './components/habitaciones/habitacion-form/habitacion-form.component';

import { ReservaListComponent } from './components/reservas/reserva-list/reserva-list.component';
import { ReservaFormComponent } from './components/reservas/reserva-form/reserva-form.component';

import { ServicioListComponent } from './components/servicios/servicio-list/servicio-list.component';
import { ServicioFormComponent } from './components/servicios/servicio-form/servicio-form.component';

import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { HotelFormComponent } from './components/hotel-form/hotel-form.component';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  
  { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
  { path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] },

  { path: 'clientes', component: ClienteListComponent, canActivate: [authGuard] },
  { path: 'clientes/nuevo', component: ClienteFormComponent, canActivate: [authGuard, roleGuard(['ADMINISTRADOR'])] },
  { path: 'clientes/:id/editar', component: ClienteFormComponent, canActivate: [authGuard, roleGuard(['ADMINISTRADOR'])] },

  { path: 'habitaciones', component: HabitacionListComponent, canActivate: [authGuard] },
  { path: 'habitaciones/nuevo', component: HabitacionFormComponent, canActivate: [authGuard, roleGuard(['ADMINISTRADOR'])] },
  { path: 'habitaciones/:id/editar', component: HabitacionFormComponent, canActivate: [authGuard, roleGuard(['ADMINISTRADOR'])] },

  { path: 'reservas', component: ReservaListComponent, canActivate: [authGuard] },
  { path: 'reservas/nuevo', component: ReservaFormComponent, canActivate: [authGuard] },
  { path: 'reservas/:id/editar', component: ReservaFormComponent, canActivate: [authGuard] },

  { path: 'servicios', component: ServicioListComponent, canActivate: [authGuard] },
  { path: 'servicios/nuevo', component: ServicioFormComponent, canActivate: [authGuard, roleGuard(['ADMINISTRADOR'])] },
  { path: 'servicios/:id/editar', component: ServicioFormComponent, canActivate: [authGuard, roleGuard(['ADMINISTRADOR'])] },

  { path: 'hotel', component: HotelFormComponent, canActivate: [authGuard] },

  { path: '**', redirectTo: 'clientes' },
];
