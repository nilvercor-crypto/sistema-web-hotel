import {
  Component,
  ChangeDetectionStrategy,
  OnInit,
  inject,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import Swal from 'sweetalert2';

import { HabitacionService } from '../../../services/habitacion.service';
import { Habitacion, EstadoHabitacion } from '../../../models/habitacion.model';

@Component({
  selector: 'app-habitacion-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
  ],
  templateUrl: './habitacion-form.component.html',
  styleUrls: ['./habitacion-form.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HabitacionFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private srv = inject(HabitacionService);

  id?: number;
  title = 'Nueva habitación';

  estados: { value: EstadoHabitacion; label: string }[] = [
    { value: 'DISPONIBLE', label: 'Disponible' },
    { value: 'OCUPADA', label: 'Ocupada' },
    { value: 'MANTENIMIENTO', label: 'Mantenimiento' },
  ];

  form = this.fb.group({
    numero: ['', [Validators.required, Validators.maxLength(10)]],
    tipo: ['', [Validators.required, Validators.maxLength(50)]],
    capacidad: [1, [Validators.required, Validators.min(1)]],
    precioPorNoche: [0, [Validators.required, Validators.min(0)]],
    estado: ['DISPONIBLE' as EstadoHabitacion, [Validators.required]],
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.id = +idParam;
      this.title = 'Editar habitación';
      this.srv.obtener(this.id).subscribe({
        next: (h) => {
          this.form.patchValue({
            numero: h.numero,
            tipo: h.tipo,
            capacidad: h.capacidad,
            precioPorNoche: h.precioPorNoche,
            estado: h.estado || 'DISPONIBLE',
          });
        },
        error: () => {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudo cargar la habitación. Por favor, intente nuevamente.',
            confirmButtonText: 'OK',
          }).then(() => {
            this.router.navigate(['/habitaciones']);
          });
        },
      });
    }
  }

  guardar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();

      const camposObligatorios: string[] = [];
      const controls = this.form.controls;

      if (controls.numero.hasError('required')) camposObligatorios.push('Número');
      if (controls.tipo.hasError('required')) camposObligatorios.push('Tipo');
      if (controls.capacidad.hasError('required')) camposObligatorios.push('Capacidad');
      if (controls.precioPorNoche.hasError('required')) camposObligatorios.push('Precio por noche');
      if (controls.estado.hasError('required')) camposObligatorios.push('Estado');

      const mensajeDetalle =
        camposObligatorios.length > 0
          ? 'Faltan completar los siguientes campos obligatorios: ' + camposObligatorios.join(', ')
          : 'Por favor, revisa los campos resaltados en rojo.';

      Swal.fire({
        icon: 'warning',
        title: 'Formulario incompleto',
        text: mensajeDetalle,
        confirmButtonText: 'OK',
        width: '500px',
      });

      return;
    }

    const payload = this.form.value as Habitacion;
    const req$ = this.id
      ? this.srv.actualizar(this.id, payload)
      : this.srv.crear(payload);

    req$.subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: '¡Habitación guardada!',
          text: 'La habitación se ha guardado correctamente',
          confirmButtonText: 'OK',
          timer: 2000,
          timerProgressBar: true,
        }).then(() => {
          this.router.navigate(['/habitaciones']);
        });
      },
      error: (e) => {
        let mensaje = 'Ocurrió un error al guardar la habitación';
        if (e?.error?.error) {
          mensaje = e.error.error;
        } else if (e?.error?.message) {
          mensaje = e.error.message;
        } else if (typeof e?.error === 'string') {
          mensaje = e.error;
        }
        Swal.fire({
          icon: 'error',
          title: 'Error al guardar habitación',
          text: mensaje,
          confirmButtonText: 'Entendido',
          width: '500px',
        });
      },
    });
  }

  cancelar() {
    this.router.navigate(['/habitaciones']);
  }
}
