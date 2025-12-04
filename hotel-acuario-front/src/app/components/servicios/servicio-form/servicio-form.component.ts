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
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import Swal from 'sweetalert2';

import { ServicioService } from '../../../services/servicio.service';
import { Servicio } from '../../../models/servicio.model';

@Component({
  selector: 'app-servicio-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule,
  ],
  templateUrl: './servicio-form.component.html',
  styleUrls: ['./servicio-form.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ServicioFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private srv = inject(ServicioService);
  private snack = inject(MatSnackBar);

  id?: number;
  title = 'Nuevo servicio';

  form = this.fb.group({
    nombreServicio: ['', [Validators.required, Validators.maxLength(100)]],
    descripcion: ['', [Validators.maxLength(250)]],
    precio: [0, [Validators.required, Validators.min(0.01)]],
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.id = +idParam;
      this.title = 'Editar servicio';
      this.srv.obtener(this.id).subscribe((s) => this.form.patchValue(s));
    }
  }

  guardar() {
    if (this.form.invalid) return;

    const payload = this.form.value as Servicio;
    const req$ = this.id
      ? this.srv.actualizar(this.id, payload)
      : this.srv.crear(payload);

    req$.subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: '¡Servicio guardado!',
          text: 'El servicio se ha guardado correctamente',
          confirmButtonText: 'OK',
          timer: 2000,
          timerProgressBar: true
        }).then(() => {
          this.router.navigate(['/servicios']);
        });
      },
      error: (e) => {
        let mensaje = 'Ocurrió un error al guardar el servicio';
        if (e?.error?.error) {
          mensaje = e.error.error;
        } else if (e?.error?.message) {
          mensaje = e.error.message;
        } else if (typeof e?.error === 'string') {
          mensaje = e.error;
        }
        Swal.fire({
          icon: 'error',
          title: 'Error al guardar servicio',
          text: mensaje,
          confirmButtonText: 'Entendido',
          width: '500px'
        });
      },
    });
  }

  cancelar() {
    this.router.navigate(['/servicios']);
  }
}

