import {
  Component,
  OnInit,
  OnDestroy,
  ChangeDetectionStrategy,
  inject,
  ChangeDetectorRef,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, NavigationEnd } from '@angular/router';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { ViewChild } from '@angular/core';
import { Habitacion } from '../../../models/habitacion.model';
import { HabitacionService } from '../../../services/habitacion.service';
import { AuthService } from '../../../services/auth.service';
import Swal from 'sweetalert2';
import { filter, Subscription } from 'rxjs';

@Component({
  selector: 'app-habitacion-list',
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
    MatTooltipModule,
    MatProgressSpinnerModule,
    MatSelectModule,
  ],
  templateUrl: './habitacion-list.component.html',
  styleUrls: ['./habitacion-list.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class HabitacionListComponent implements OnInit, OnDestroy {
  private srv = inject(HabitacionService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);
  authService = inject(AuthService);
  private navigationSubscription?: Subscription;

  displayedColumns = [
    'id',
    'numero',
    'tipo',
    'capacidad',
    'precioPorNoche',
    'estado',
    'acciones',
  ];
  dataSource = new MatTableDataSource<Habitacion>([]);
  loading = false;
  filtroEstado: string = '';

  estados = [
    { value: '', label: 'Todos' },
    { value: 'DISPONIBLE', label: 'Disponible' },
    { value: 'OCUPADA', label: 'Ocupada' },
    { value: 'MANTENIMIENTO', label: 'Mantenimiento' }
  ];

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit(): void {
    this.cargarHabitaciones();
    
    this.navigationSubscription = this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        if (event.url === '/habitaciones') {
          this.cargarHabitaciones();
        }
      });
  }

  ngOnDestroy(): void {
    if (this.navigationSubscription) {
      this.navigationSubscription.unsubscribe();
    }
  }

  cargarHabitaciones(): void {
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
        console.error('Error al cargar habitaciones:', e);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar las habitaciones. Por favor, intente nuevamente.',
          confirmButtonText: 'OK',
        });
      },
    });
  }

  applyFilter(value: string) {
    const filterValue = value.trim().toLowerCase();
    this.dataSource.filterPredicate = (data: Habitacion, filter: string) => {
      const matchesSearch = !filter || 
        data.numero.toLowerCase().includes(filter) ||
        data.tipo.toLowerCase().includes(filter) ||
        data.capacidad.toString().includes(filter);
      
      const matchesEstado = !this.filtroEstado || data.estado === this.filtroEstado;
      
      return matchesSearch && matchesEstado;
    };
    this.dataSource.filter = filterValue;
  }

  onEstadoFilterChange() {
    this.applyFilter(this.dataSource.filter || '');
  }

  nuevo() {
    this.router.navigate(['/habitaciones/nuevo']);
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  editar(row: Habitacion) {
    this.router.navigate(['/habitaciones', row.id, 'editar']);
  }

  eliminar(row: Habitacion) {
    Swal.fire({
      title: '¿Está seguro?',
      text: `¿Desea eliminar la habitación "${row.numero} - ${row.tipo}"? Esta acción no se puede deshacer.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        this.srv.eliminar(row.id!).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Habitación eliminada',
              text: 'La habitación se ha eliminado correctamente',
              timer: 2000,
              timerProgressBar: true,
            });
            this.cargarHabitaciones();
          },
          error: (e) => {
            let mensaje = 'Ocurrió un error al eliminar la habitación';
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
    });
  }
}
