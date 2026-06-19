import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { RecuperarContrasena } from './pages/recuperar-contrasena/recuperar-contrasena';
import { Registro } from './pages/registro/registro';
import { Inicio } from './pages/inicio/inicio';

export const routes: Routes = [
  {
    path: '',
    component: Login
  }
  ,
  {
    path: 'recuperar-contraseña',
    component: RecuperarContrasena
  }
  ,
  {
    path: 'registro',
    component: Registro
  }
  ,
  {
    path: 'inicio',
    component: Inicio
  }
];