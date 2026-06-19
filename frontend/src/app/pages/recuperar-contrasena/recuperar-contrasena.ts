import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-recuperar-contrasena',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './recuperar-contrasena.html',
  styleUrls: ['./recuperar-contrasena.scss']
})
export class RecuperarContrasena {
  email = '';
  message = '';

  constructor(private router: Router) {}

  sendRecovery() {
    // Aquí podríamos llamar a un servicio; por ahora simulamos.
    if (!this.email) {
      this.message = 'Introduce un correo válido';
      return;
    }

    this.message = 'Si existe una cuenta, recibirás un correo con instrucciones.';
  }

  backToLogin() {
    this.router.navigate(['/']);
  }
}
