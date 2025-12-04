import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { HotelService } from '../../services/hotel.service';
import { Hotel } from '../../models/hotel.model';
import { AuthService } from '../../services/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-hotel-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './hotel-form.component.html',
  styleUrls: ['./hotel-form.component.css'],
})
export class HotelFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private srv = inject(HotelService);
  private router = inject(Router);
  private authService = inject(AuthService);

  form: FormGroup;
  loading = false;
  hotel: Hotel | null = null;

  constructor() {
    this.form = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(150)]],
      ruc: ['', [Validators.minLength(11), Validators.maxLength(11), Validators.pattern('^[0-9]{11}$')]],
      direccion: ['', [Validators.maxLength(200)]],
      celular: ['', [Validators.minLength(9), Validators.maxLength(9), Validators.pattern('^[0-9]{9}$')]],
      email: ['', [Validators.email, Validators.maxLength(150)]],
    });
  }

  ngOnInit(): void {
    this.cargarHotel();
  }

  cargarHotel(): void {
    this.loading = true;
    this.srv.obtener().subscribe({
      next: (data) => {
        this.hotel = data;
        this.form.patchValue({
          nombre: data.nombre || '',
          ruc: data.ruc || '',
          direccion: data.direccion || '',
          celular: data.celular || '',
          email: data.email || '',
        });
        this.loading = false;
      },
      error: (e) => {
        this.loading = false;
        let mensaje = 'No se pudo cargar la información del hotel';
        if (e?.error?.error) {
          mensaje = e.error.error;
        } else if (typeof e?.error === 'string') {
          mensaje = e.error;
        }
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: mensaje,
          confirmButtonText: 'OK',
        });
      },
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      Swal.fire({
        icon: 'warning',
        title: 'Formulario incompleto',
        text: 'Por favor, complete todos los campos requeridos correctamente',
        confirmButtonText: 'OK',
      });
      return;
    }

    const payload: Hotel = this.form.value;
    this.loading = true;

    this.srv.actualizar(payload).subscribe({
      next: () => {
        this.loading = false;
        Swal.fire({
          icon: 'success',
          title: '¡Información actualizada!',
          text: 'La información del hotel se ha actualizado correctamente',
          confirmButtonText: 'OK',
          timer: 2000,
          timerProgressBar: true,
        }).then(() => {
          this.router.navigate(['/dashboard']);
        });
      },
      error: (e) => {
        this.loading = false;
        let mensaje = 'Ocurrió un error al actualizar la información del hotel';
        if (e?.error?.error) {
          mensaje = e.error.error;
        } else if (e?.error?.message) {
          mensaje = e.error.message;
        } else if (typeof e?.error === 'string') {
          mensaje = e.error;
        }
        Swal.fire({
          icon: 'error',
          title: 'Error al actualizar',
          text: mensaje,
          confirmButtonText: 'Entendido',
        });
      },
    });
  }

  cancelar(): void {
    this.router.navigate(['/dashboard']);
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }
}
