import {
  Component,
  ChangeDetectionStrategy,
  OnInit,
  inject,
  signal,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import Swal from 'sweetalert2';

import { ReservaService } from '../../../services/reserva.service';
import { ClienteService } from '../../../services/cliente.service';
import { HabitacionService } from '../../../services/habitacion.service';
import { ServicioService } from '../../../services/servicio.service';
import { ReservaRequest, ReservaResponse, ReservaServicio } from '../../../models/reserva.model';
import { Cliente } from '../../../models/cliente.model';
import { Habitacion } from '../../../models/habitacion.model';
import { Servicio } from '../../../models/servicio.model';

@Component({
  selector: 'app-reserva-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterLink,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatTableModule,
  ],
  templateUrl: './reserva-form.component.html',
  styleUrls: ['./reserva-form.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ReservaFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private reservaSrv = inject(ReservaService);
  private clienteSrv = inject(ClienteService);
  private habitacionSrv = inject(HabitacionService);
  private servicioSrv = inject(ServicioService);
  private snack = inject(MatSnackBar);

  id?: number;
  title = 'Nueva reserva';
  
  minDate = new Date();
  
  clientes = signal<Cliente[]>([]);
  habitaciones = signal<Habitacion[]>([]);
  servicios = signal<Servicio[]>([]);
  serviciosSeleccionados = signal<ReservaServicio[]>([]);
  
  montoHabitacion = signal<number>(0);
  montoServicios = signal<number>(0);
  montoTotal = signal<number>(0);
  
  private reservaCargada = false;
  private habitacionActual: Habitacion | null = null;

  displayedColumns = ['servicio', 'precio', 'cantidad', 'subtotal', 'acciones'];

  form = this.fb.group({
    clienteId: [null as number | null, [Validators.required]],
    habitacionId: [null as number | null, [Validators.required]],
    fechaIngreso: ['', [Validators.required, this.validarFechaMinima.bind(this)]],
    fechaSalida: ['', [Validators.required]],
  });

  validarFechaMinima(control: any) {
    if (!control.value) return null;
    const fechaSeleccionada = new Date(control.value);
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);
    fechaSeleccionada.setHours(0, 0, 0, 0);
    
    if (fechaSeleccionada < hoy) {
      return { fechaPasada: true };
    }
    return null;
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    
    if (idParam) {
      this.id = +idParam;
      this.title = 'Editar reserva';
    }
    
    this.cargarDatos();

    this.form.get('habitacionId')?.valueChanges.subscribe(() => this.calcularMontos());
    this.form.get('fechaIngreso')?.valueChanges.subscribe(() => {
      this.calcularMontos();
      this.buscarHabitacionesDisponibles();
    });
    this.form.get('fechaSalida')?.valueChanges.subscribe(() => {
      this.calcularMontos();
      this.buscarHabitacionesDisponibles();
    });
  }

  cargarDatos() {
    this.clienteSrv.listar().subscribe((data) => {
      this.clientes.set(data);
      this.intentarCargarReserva();
    });
    this.habitacionSrv.listar({ estado: 'DISPONIBLE' }).subscribe((data) => {
      this.habitaciones.set(data);
      if (this.id) {
        this.intentarCargarReserva();
      }
    });
    this.servicioSrv.listar().subscribe((data) => this.servicios.set(data));
  }

  buscarHabitacionesDisponibles() {
    const fechaIngreso = this.form.get('fechaIngreso')?.value;
    const fechaSalida = this.form.get('fechaSalida')?.value;
    const habitacionIdActual = this.form.get('habitacionId')?.value;

    if (fechaIngreso && fechaSalida) {
      const fechaIngresoStr = this.formatearFecha(fechaIngreso);
      const fechaSalidaStr = this.formatearFecha(fechaSalida);

      this.habitacionSrv.buscarDisponibles(fechaIngresoStr, fechaSalidaStr).subscribe({
        next: (data) => {
          if (this.id && habitacionIdActual && this.habitacionActual) {
            if (!data.find(h => h.id === habitacionIdActual)) {
              data = [...data, this.habitacionActual];
            }
          }
          
          this.habitaciones.set(data);
          
          if (!this.id && habitacionIdActual && !data.find(h => h.id === habitacionIdActual)) {
            this.form.patchValue({ habitacionId: null });
            Swal.fire({
              icon: 'warning',
              title: 'Habitación no disponible',
              text: 'La habitación seleccionada ya no está disponible para las fechas elegidas. Por favor, seleccione otra.',
              confirmButtonText: 'OK',
            });
          }
        },
        error: (err) => {
          console.error('Error al buscar habitaciones disponibles:', err);
          this.habitacionSrv.listar({ estado: 'DISPONIBLE' }).subscribe((data) => {
            this.habitaciones.set(data);
          });
        },
      });
    } else {
      this.habitacionSrv.listar({ estado: 'DISPONIBLE' }).subscribe((data) => {
        this.habitaciones.set(data);
      });
    }
  }

  formatearFecha(fecha: string | Date): string {
    const date = typeof fecha === 'string' ? new Date(fecha) : fecha;
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  private intentarCargarReserva() {
    if (!this.reservaCargada && this.id && this.clientes().length > 0 && this.habitaciones().length > 0) {
      this.reservaCargada = true;
      this.reservaSrv.obtener(this.id).subscribe({
        next: (reserva) => {
          if (reserva.habitacionId) {
            const habitacion = this.habitaciones().find(h => h.id === reserva.habitacionId);
            if (habitacion) {
              this.habitacionActual = habitacion;
            } else {
              this.habitacionSrv.obtener(reserva.habitacionId).subscribe({
                next: (hab) => {
                  this.habitacionActual = hab;
                },
                error: () => {
                  console.error('Error al obtener habitación actual');
                },
              });
            }
          }

          this.form.patchValue({
            clienteId: reserva.clienteId ?? null,
            habitacionId: reserva.habitacionId ?? null,
            fechaIngreso: reserva.fechaIngreso,
            fechaSalida: reserva.fechaSalida,
          });

          if (reserva.servicios) {
            this.serviciosSeleccionados.set(reserva.servicios);
          }

          setTimeout(() => {
            this.calcularMontos();
            this.buscarHabitacionesDisponibles();
          }, 100);
        },
        error: () => {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudo cargar la reserva. Por favor, intente nuevamente.',
            confirmButtonText: 'OK',
          });
        },
      });
    }
  }

  servicioSeleccionadoId: number | null = null;
  cantidadServicio: number = 1;

  agregarServicio() {
    if (!this.servicioSeleccionadoId) {
      Swal.fire({
        icon: 'warning',
        title: 'Servicio requerido',
        text: 'Por favor, seleccione un servicio',
        confirmButtonText: 'OK'
      });
      return;
    }

    const servicio = this.servicios().find(s => s.id === this.servicioSeleccionadoId);
    if (!servicio) {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'Servicio no encontrado',
        confirmButtonText: 'OK'
      });
      return;
    }

    if (this.serviciosSeleccionados().some(s => s.servicioId === servicio.id)) {
      Swal.fire({
        icon: 'warning',
        title: 'Servicio duplicado',
        text: 'Este servicio ya está agregado. Si necesita más cantidad, elimínelo y vuelva a agregarlo con la cantidad total.',
        confirmButtonText: 'Entendido'
      });
      return;
    }

    if (this.cantidadServicio < 1) {
      Swal.fire({
        icon: 'warning',
        title: 'Cantidad inválida',
        text: 'La cantidad debe ser al menos 1',
        confirmButtonText: 'OK'
      });
      return;
    }

    const nuevo: ReservaServicio = {
      servicioId: servicio.id!,
      servicioNombre: servicio.nombreServicio,
      servicioPrecio: servicio.precio,
      cantidad: this.cantidadServicio,
      subtotal: servicio.precio * this.cantidadServicio,
    };

    this.serviciosSeleccionados.update(list => [...list, nuevo]);
    this.servicioSeleccionadoId = null;
    this.cantidadServicio = 1;
    this.calcularMontos();
  }

  quitarServicio(index: number) {
    this.serviciosSeleccionados.update(list => list.filter((_, i) => i !== index));
    this.calcularMontos();
  }

  calcularMontos() {
    const habitacionId = this.form.get('habitacionId')?.value;
    const fechaIngreso = this.form.get('fechaIngreso')?.value;
    const fechaSalida = this.form.get('fechaSalida')?.value;

    let montoHab = 0;
    if (habitacionId && fechaIngreso && fechaSalida) {
      const habitacion = this.habitaciones().find(h => h.id === +habitacionId);
      if (habitacion) {
        const inicio = new Date(fechaIngreso);
        const fin = new Date(fechaSalida);
        const diffTime = fin.getTime() - inicio.getTime();
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        const noches = diffDays <= 0 ? 1 : diffDays;
        montoHab = habitacion.precioPorNoche! * noches;
      }
    }
    this.montoHabitacion.set(montoHab);

    const montoServ = this.serviciosSeleccionados().reduce((sum, s) => sum + (s.subtotal || 0), 0);
    this.montoServicios.set(montoServ);

    this.montoTotal.set(montoHab + montoServ);
  }

  guardar() {
    if (this.form.invalid) {
      Swal.fire({
        icon: 'warning',
        title: 'Formulario incompleto',
        text: 'Por favor, complete todos los campos requeridos',
        confirmButtonText: 'OK'
      });
      return;
    }

    const payload: ReservaRequest = {
      clienteId: +this.form.value.clienteId!,
      habitacionId: +this.form.value.habitacionId!,
      fechaIngreso: this.form.value.fechaIngreso!,
      fechaSalida: this.form.value.fechaSalida!,
      servicios: this.serviciosSeleccionados().map(s => ({
        servicioId: s.servicioId,
        cantidad: s.cantidad,
      })),
    };

    this.reservaSrv.crear(payload).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: '¡Reserva creada!',
          text: 'La reserva se ha creado correctamente',
          confirmButtonText: 'OK',
          timer: 2000,
          timerProgressBar: true
        }).then(() => {
          this.router.navigate(['/reservas']);
        });
      },
      error: (e) => {
        let mensaje = 'Ocurrió un error al crear la reserva';
        
        if (e?.error?.error) {
          mensaje = e.error.error;
        } else if (typeof e?.error === 'string') {
          mensaje = e.error;
        } else if (e?.error?.message) {
          mensaje = e.error.message;
        } else if (e?.message) {
          mensaje = e.message;
        }
        
        console.error('Error al crear reserva:', e);
        
        Swal.fire({
          icon: 'error',
          title: 'Error al crear reserva',
          text: mensaje,
          confirmButtonText: 'Entendido',
          width: '500px'
        });
      },
    });
  }

  cancelar() {
    this.router.navigate(['/reservas']);
  }
}
