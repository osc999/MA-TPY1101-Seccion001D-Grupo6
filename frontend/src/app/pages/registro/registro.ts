import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './registro.html',
  styleUrls: ['./registro.scss']
})
export class Registro {
  nombre = '';
  email = '';
  telefono = '';
  password = '';
  confirmPassword = '';
  rol = 'CLIENTE';
  message = '';

  constructor(private router: Router, private authService: AuthService) {}

  register() {
    if (!this.nombre || !this.email || !this.password || !this.confirmPassword) {
      this.message = 'Completa todos los campos';
      return;
    }
    if (this.password !== this.confirmPassword) {
      this.message = 'Las contraseñas no coinciden';
      return;
    }

    this.authService.register(this.nombre, this.email, this.password).subscribe({
      next: () => {
        this.message = 'Registro exitoso. Redirigiendo al login...';
        setTimeout(() => this.router.navigate(['/']), 1200);
      },
      error: (err: any) => {
        console.error('Registro error:', err);
        if (err?.error instanceof ProgressEvent) {
          this.message = 'Error de red: no se pudo conectar al servidor';
        } else if (err?.error && typeof err.error === 'object' && err.error.message) {
          this.message = err.error.message;
        } else if (typeof err?.error === 'string') {
          this.message = err.error;
        } else {
          this.message = err?.message || 'Error al registrar';
        }
      }
    });
  }

  backToLogin() {
    this.router.navigate(['/']);
  }
}
