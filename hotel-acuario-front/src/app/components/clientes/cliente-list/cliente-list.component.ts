import {
  Component,
  OnInit,
  ChangeDetectionStrategy,
  inject,
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
import { ViewChild } from '@angular/core';
import { Cliente } from '../../../models/cliente.model';
import { ClienteService } from '../../../services/cliente.service';
import { AuthService } from '../../../services/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-cliente-list',
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
  templateUrl: './cliente-list.component.html',
  styleUrls: ['./cliente-list.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ClienteListComponent implements OnInit {
  private srv = inject(ClienteService);
  private router = inject(Router);
  authService = inject(AuthService);

  displayedColumns = [
    'id',
    'nombre',
    'apellido',
    'documentoIdentidad',
    'email',
    'acciones',
  ];
  dataSource = new MatTableDataSource<Cliente>([]);
  loading = false;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {
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
        console.error('Error al cargar clientes:', e);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar los clientes. Por favor, intente nuevamente.',
          confirmButtonText: 'OK',
        });
      },
    });
  }

  applyFilter(value: string) {
    this.dataSource.filter = value.trim().toLowerCase();
  }

  nuevo() {
    this.router.navigate(['/clientes/nuevo']);
  }

  editar(row: Cliente) {
    this.router.navigate(['/clientes', row.id, 'editar']);
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  eliminar(row: Cliente) {
    Swal.fire({
      title: '¿Está seguro?',
      text: `¿Desea eliminar al cliente "${row.nombre} ${row.apellido}"? Esta acción no se puede deshacer.`,
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
              title: 'Cliente eliminado',
              text: 'El cliente se ha eliminado correctamente',
              timer: 2000,
              timerProgressBar: true,
            });
            this.cargarClientes();
          },
          error: (e) => {
            let mensaje = 'Ocurrió un error al eliminar el cliente';
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
