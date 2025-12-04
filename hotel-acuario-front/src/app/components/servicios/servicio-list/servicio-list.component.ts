import {
  Component,
  OnInit,
  ChangeDetectionStrategy,
  inject,
  ViewChild,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import Swal from 'sweetalert2';
import { Servicio } from '../../../models/servicio.model';
import { ServicioService } from '../../../services/servicio.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-servicio-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatCardModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './servicio-list.component.html',
  styleUrls: ['./servicio-list.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ServicioListComponent implements OnInit {
  private srv = inject(ServicioService);
  private router = inject(Router);
  authService = inject(AuthService);

  displayedColumns = ['id', 'nombreServicio', 'descripcion', 'precio', 'acciones'];
  dataSource = new MatTableDataSource<Servicio>([]);
  loading = false;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit(): void {
    this.cargarServicios();
  }

  cargarServicios(): void {
    this.loading = true;
    this.srv.listar().subscribe({
      next: (data) => {
        this.dataSource.data = data;
        setTimeout(() => {
          if (this.paginator) this.dataSource.paginator = this.paginator;
          if (this.sort) this.dataSource.sort = this.sort;
        });
        this.loading = false;
      },
      error: (e) => {
        this.loading = false;
        console.error('Error al cargar servicios:', e);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar los servicios. Por favor, intente nuevamente.',
          confirmButtonText: 'OK',
        });
      },
    });
  }

  applyFilter(value: string) {
    this.dataSource.filter = value.trim().toLowerCase();
  }

  nuevo() {
    this.router.navigate(['/servicios/nuevo']);
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  editar(row: Servicio) {
    this.router.navigate(['/servicios', row.id, 'editar']);
  }

  eliminar(row: Servicio) {
    Swal.fire({
      title: '¿Está seguro?',
      text: `¿Desea eliminar el servicio "${row.nombreServicio}"? Esta acción no se puede deshacer.`,
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
              title: 'Servicio eliminado',
              text: 'El servicio se ha eliminado correctamente',
              timer: 2000,
              timerProgressBar: true
            });
            this.cargarServicios();
          },
          error: (e) => {
            let mensaje = 'Ocurrió un error al eliminar el servicio';
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
}

