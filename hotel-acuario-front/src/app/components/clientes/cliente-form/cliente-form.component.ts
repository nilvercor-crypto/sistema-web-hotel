import {
  Component,
  ChangeDetectionStrategy,
  OnInit,
  inject,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { AbstractControl, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSelectModule } from '@angular/material/select';
import Swal from 'sweetalert2';

import { ClienteService } from '../../../services/cliente.service';
import { Cliente, TipoDocumento } from '../../../models/cliente.model';

@Component({
  selector: 'app-cliente-form',
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
    MatSnackBarModule,
  ],
  templateUrl: './cliente-form.component.html',
  styleUrls: ['./cliente-form.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ClienteFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private srv = inject(ClienteService);
  private snack = inject(MatSnackBar);

  id?: number;
  title = 'Nuevo cliente';

  tiposDocumento: { value: TipoDocumento; label: string }[] = [
    { value: 'DNI', label: 'DNI' },
    { value: 'CARNET_EXTRANJERIA', label: 'Carnet de extranjería' },
    { value: 'PASAPORTE', label: 'Pasaporte' },
    { value: 'OTRO', label: 'Otro' },
  ];

  form = this.fb.group({
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
    apellido: ['', [Validators.required, Validators.maxLength(100)]],
    tipoDocumento: ['DNI' as TipoDocumento, [Validators.required]],
    documentoIdentidad: ['', [Validators.required, this.validarDocumentoPorTipo.bind(this)]],
    email: ['', [Validators.email, Validators.maxLength(150)]],
    celular: ['', [Validators.required, Validators.minLength(9), Validators.maxLength(9)]],
    direccion: ['', [Validators.maxLength(200)]],
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.id = +idParam;
      this.title = 'Editar cliente';
      this.srv.obtener(this.id).subscribe((c) => this.form.patchValue(c));
    }
  }

  validarDocumentoPorTipo(control: AbstractControl) {
    const valor = (control.value || '').toString().trim();
    if (!valor) return null;

    const tipo = this.form.get('tipoDocumento')?.value as TipoDocumento;

    if (tipo === 'DNI') {
      const esValido = /^\d{8}$/.test(valor);
      return esValido ? null : { dniInvalido: true };
    }

    if (tipo === 'CARNET_EXTRANJERIA') {
      const esValido = /^[A-Za-z0-9]{9,12}$/.test(valor);
      return esValido ? null : { ceInvalido: true };
    }

    if (tipo === 'PASAPORTE') {
      const esValido = /^[A-Za-z0-9]{6,12}$/.test(valor);
      return esValido ? null : { pasaporteInvalido: true };
    }

    if (tipo === 'OTRO') {
      const len = valor.length;
      if (len < 4 || len > 15) {
        return { otroInvalido: true };
      }
    }

    return null;
  }

  guardar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();

      const camposObligatorios: string[] = [];
      const controls = this.form.controls;

      if (controls.nombre.hasError('required')) camposObligatorios.push('Nombre');
      if (controls.apellido.hasError('required')) camposObligatorios.push('Apellido');
      if (controls.tipoDocumento.hasError('required')) camposObligatorios.push('Tipo de documento');
      if (controls.documentoIdentidad.hasError('required')) camposObligatorios.push('Documento');
      if (controls.celular.hasError('required')) camposObligatorios.push('Celular');

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

    const payload = this.form.value as Cliente;
    const req$ = this.id
      ? this.srv.actualizar(this.id, payload)
      : this.srv.crear(payload);

    req$.subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: '¡Cliente guardado!',
          text: 'El cliente se ha guardado correctamente',
          confirmButtonText: 'OK',
          timer: 2000,
          timerProgressBar: true,
        }).then(() => {
          this.router.navigate(['/clientes']);
        });
      },
      error: (e) => {
        let mensaje = 'Ocurrió un error al guardar el cliente';
        if (e?.error?.error) {
          mensaje = e.error.error;
        } else if (e?.error?.message) {
          mensaje = e.error.message;
        } else if (typeof e?.error === 'string') {
          mensaje = e.error;
        }
        Swal.fire({
          icon: 'error',
          title: 'Error al guardar cliente',
          text: mensaje,
          confirmButtonText: 'Entendido',
          width: '500px',
        });
      },
    });
  }

  cancelar() {
    this.router.navigate(['/clientes']);
  }
}
