import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private authService = inject(AuthService);

  form = this.fb.group({
    nombreUsuario: ['', [Validators.required]],
    contrasena: ['', [Validators.required, Validators.minLength(6)]]
  });

  loading = false;

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      Swal.fire({
        icon: 'error',
        title: 'Error de validación',
        text: 'Por favor, complete todos los campos requeridos',
        confirmButtonText: 'OK'
      });
      return;
    }

    this.loading = true;
    const loginRequest = {
      nombreUsuario: this.form.value.nombreUsuario!,
      contrasena: this.form.value.contrasena!
    };

    this.authService.login(loginRequest).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: '¡Bienvenido!',
          text: 'Inicio de sesión exitoso',
          timer: 2000,
          timerProgressBar: true,
          showConfirmButton: false
        });
        this.router.navigate(['/clientes']);
      },
      error: (e) => {
        this.loading = false;
        console.error('Error de login:', e);
        
        let mensaje = 'Error al iniciar sesión. Verifique sus credenciales.';
        let titulo = 'Error de autenticación';
        
        if (e?.status === 401 || e?.status === 403) {
          titulo = 'Credenciales inválidas';
          mensaje = 'El nombre de usuario o contraseña son incorrectos.';
        } else if (e?.status === 0) {
          titulo = 'Error de conexión';
          mensaje = 'No se pudo conectar con el servidor. Verifique que el backend esté corriendo en http://localhost:8080';
        } else if (e?.status === 500) {
          titulo = 'Error del servidor';
          mensaje = 'Ocurrió un error en el servidor. Verifique los logs del backend.';
        } else if (e?.error?.message) {
          mensaje = e.error.message;
        } else if (e?.error?.error) {
          mensaje = e.error.error;
        } else if (typeof e?.error === 'string') {
          mensaje = e.error;
        }
        
        Swal.fire({
          icon: 'error',
          title: titulo,
          text: mensaje,
          confirmButtonText: 'OK'
        });
      }
    });
  }

  irARegistro(): void {
    this.router.navigate(['/register']);
  }
}

