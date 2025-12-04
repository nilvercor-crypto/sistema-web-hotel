import {
  Component,
  OnInit,
  OnDestroy,
  ChangeDetectionStrategy,
  inject,
  ViewChild,
  ChangeDetectorRef,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, NavigationEnd } from '@angular/router';
import { filter, Subscription } from 'rxjs';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { ReservaResponse } from '../../../models/reserva.model';
import { ReservaService } from '../../../services/reserva.service';

@Component({
  selector: 'app-reserva-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    MatCardModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatChipsModule,
    MatTooltipModule,
    MatProgressSpinnerModule,
    MatSelectModule,
  ],
  templateUrl: './reserva-list.component.html',
  styleUrls: ['./reserva-list.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ReservaListComponent implements OnInit, OnDestroy {
  private srv = inject(ReservaService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);
  private navigationSubscription?: Subscription;

  displayedColumns = [
    'id',
    'cliente',
    'habitacion',
    'fechas',
    'servicios',
    'montoTotal',
    'estado',
    'acciones',
  ];
  dataSource = new MatTableDataSource<ReservaResponse>([]);
  loading = false;
  filtroEstado: string = '';

  estados = [
    { value: '', label: 'Todos' },
    { value: 'PENDIENTE', label: 'Pendiente' },
    { value: 'CONFIRMADA', label: 'Confirmada' },
    { value: 'CANCELADA', label: 'Cancelada' },
    { value: 'FINALIZADA', label: 'Finalizada' }
  ];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit(): void {
    this.cargarReservas();
    
    this.navigationSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        if (event.url === '/reservas') {
          this.cargarReservas();
        }
      });
  }

  ngOnDestroy(): void {
    if (this.navigationSubscription) {
      this.navigationSubscription.unsubscribe();
    }
  }

  applyFilter(value: string) {
    const filterValue = value.trim().toLowerCase();
    this.dataSource.filterPredicate = (data: ReservaResponse, filter: string) => {
      const matchesSearch = !filter || 
        (!!data.clienteNombre && data.clienteNombre.toLowerCase().includes(filter)) ||
        (!!data.clienteApellido && data.clienteApellido.toLowerCase().includes(filter)) ||
        (!!data.habitacionNumero && data.habitacionNumero.toLowerCase().includes(filter)) ||
        (!!data.habitacionTipo && data.habitacionTipo.toLowerCase().includes(filter)) ||
        (!!data.id && data.id.toString().includes(filter));
      
      const matchesEstado = !this.filtroEstado || data.estado === this.filtroEstado;
      
      return !!matchesSearch && !!matchesEstado;
    };
    this.dataSource.filter = filterValue;
  }

  onEstadoFilterChange() {
    this.applyFilter(this.dataSource.filter || '');
  }

  nuevo() {
    this.router.navigate(['/reservas/nuevo']);
  }

  editar(row: ReservaResponse) {
    this.router.navigate(['/reservas', row.id, 'editar']);
  }

  confirmar(row: ReservaResponse) {
    Swal.fire({
      title: '¿Confirmar reserva?',
      text: `¿Desea confirmar la reserva #${row.id}? La habitación pasará a estado OCUPADA si la fecha de ingreso es hoy o pasada.`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, confirmar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.srv.confirmar(row.id!).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Reserva confirmada',
              text: 'La reserva se ha confirmado correctamente',
              timer: 2000,
              timerProgressBar: true
            });
            this.cargarReservas();
          },
          error: (e) => {
            let mensaje = 'Ocurrió un error al confirmar la reserva';
            if (e?.error?.error) {
              mensaje = e.error.error;
            } else if (typeof e?.error === 'string') {
              mensaje = e.error;
            }
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: mensaje,
              confirmButtonText: 'OK'
            });
          }
        });
      }
    });
  }

  cancelar(row: ReservaResponse) {
    Swal.fire({
      title: '¿Cancelar reserva?',
      text: `¿Desea cancelar la reserva #${row.id}? La habitación volverá a DISPONIBLE si estaba ocupada.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, cancelar',
      cancelButtonText: 'No'
    }).then((result) => {
      if (result.isConfirmed) {
        this.srv.cancelar(row.id!).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Reserva cancelada',
              text: 'La reserva se ha cancelado correctamente',
              timer: 2000,
              timerProgressBar: true
            });
            this.cargarReservas();
          },
          error: (e) => {
            let mensaje = 'Ocurrió un error al cancelar la reserva';
            if (e?.error?.error) {
              mensaje = e.error.error;
            } else if (typeof e?.error === 'string') {
              mensaje = e.error;
            }
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: mensaje,
              confirmButtonText: 'OK'
            });
          }
        });
      }
    });
  }

  finalizar(row: ReservaResponse) {
    Swal.fire({
      title: '¿Finalizar reserva?',
      text: `¿Desea finalizar la reserva #${row.id}? La habitación volverá a DISPONIBLE.`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, finalizar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.srv.finalizar(row.id!).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Reserva finalizada',
              text: 'La reserva se ha finalizado correctamente',
              timer: 2000,
              timerProgressBar: true
            });
            this.cargarReservas();
          },
          error: (e) => {
            let mensaje = 'Ocurrió un error al finalizar la reserva';
            if (e?.error?.error) {
              mensaje = e.error.error;
            } else if (typeof e?.error === 'string') {
              mensaje = e.error;
            }
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: mensaje,
              confirmButtonText: 'OK'
            });
          }
        });
      }
    });
  }

  eliminar(row: ReservaResponse) {
    Swal.fire({
      title: '¿Está seguro?',
      text: `¿Desea eliminar la reserva #${row.id}? Esta acción no se puede deshacer.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.srv.eliminar(row.id!).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Reserva eliminada',
              text: 'La reserva se ha eliminado correctamente',
              timer: 2000,
              timerProgressBar: true
            });
            this.cargarReservas();
          },
          error: (e) => {
            let mensaje = 'Ocurrió un error al eliminar la reserva';
            if (e?.error?.error) {
              mensaje = e.error.error;
            } else if (typeof e?.error === 'string') {
              mensaje = e.error;
            }
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: mensaje,
              confirmButtonText: 'OK'
            });
          }
        });
      }
    });
  }

  cargarReservas(): void {
    this.loading = true;
    this.srv.listar().subscribe({
      next: (data) => {
        this.dataSource.data = data;
        setTimeout(() => {
          if (this.paginator) this.dataSource.paginator = this.paginator;
          if (this.sort) this.dataSource.sort = this.sort;
        });
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (e) => {
        this.loading = false;
        console.error('Error al cargar reservas:', e);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar las reservas. Por favor, intente nuevamente.',
          confirmButtonText: 'OK',
        });
      }
    });
  }

  getEstadoColor(estado?: string): string {
    switch (estado) {
      case 'CONFIRMADA':
        return 'primary';
      case 'CANCELADA':
        return 'warn';
      case 'FINALIZADA':
        return 'accent';
      default:
        return '';
    }
  }

  getServiciosCount(reserva: ReservaResponse): number {
    return reserva.servicios?.length || 0;
  }

  getServiciosTooltip(reserva: ReservaResponse): string {
    if (!reserva.servicios || reserva.servicios.length === 0) {
      return 'Sin servicios adicionales';
    }
    return reserva.servicios
      .map(s => `${s.servicioNombre} (x${s.cantidad})`)
      .join(', ');
  }

  puedeConfirmar(estado?: string): boolean {
    return estado === 'PENDIENTE';
  }

  puedeCancelar(estado?: string): boolean {
    return estado === 'PENDIENTE' || estado === 'CONFIRMADA';
  }

  puedeFinalizar(estado?: string): boolean {
    return estado === 'CONFIRMADA';
  }
}
