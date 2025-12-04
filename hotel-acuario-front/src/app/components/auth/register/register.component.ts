import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

import { AuthService } from '../../../services/auth.service';
import { Rol } from '../../../models/auth.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private authService = inject(AuthService);

  form = this.fb.group({
    nombreUsuario: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
    contrasena: ['', [Validators.required, Validators.minLength(6)]],
    confirmarContrasena: ['', [Validators.required]],
    rol: ['USUARIO' as Rol, [Validators.required]]
  }, { validators: this.passwordMatchValidator });

  loading = false;

  passwordMatchValidator(group: any) {
    const password = group.get('contrasena');
    const confirmPassword = group.get('confirmarContrasena');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      confirmPassword.setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    }
    return null;
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      Swal.fire({
        icon: 'error',
        title: 'Error de validación',
        text: 'Por favor, complete todos los campos correctamente',
        confirmButtonText: 'OK'
      });
      return;
    }

    this.loading = true;
    const registerRequest = {
      nombreUsuario: this.form.value.nombreUsuario!,
      contrasena: this.form.value.contrasena!,
      rol: this.form.value.rol as Rol
    };

    this.authService.register(registerRequest).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: '¡Registro exitoso!',
          text: 'Tu cuenta ha sido creada correctamente',
          timer: 2000,
          timerProgressBar: true,
          showConfirmButton: false
        });
        this.router.navigate(['/clientes']);
      },
      error: (e) => {
        this.loading = false;
        console.error('Error de registro:', e);
        
        let mensaje = 'Error al registrar usuario. Intente nuevamente.';
        
        if (e?.status === 0) {
          mensaje = 'No se pudo conectar con el servidor. Verifique que el backend esté corriendo.';
        } else if (e?.error?.message) {
          mensaje = e.error.message;
        } else if (e?.error?.error) {
          mensaje = e.error.error;
        } else if (typeof e?.error === 'string') {
          mensaje = e.error;
        }
        
        Swal.fire({
          icon: 'error',
          title: 'Error de registro',
          text: mensaje,
          confirmButtonText: 'OK'
        });
      }
    });
  }

  irALogin(): void {
    this.router.navigate(['/login']);
  }
}

