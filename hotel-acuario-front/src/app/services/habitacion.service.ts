import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Habitacion } from '../models/habitacion.model';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class HabitacionService {
  private readonly base = `${environment.apiBase}/habitaciones`;

  constructor(private http: HttpClient) {}

  listar(params?: {
    estado?: string;
    tipo?: string;
  }): Observable<Habitacion[]> {
    let hp = new HttpParams();
    if (params?.estado) hp = hp.set('estado', params.estado);
    if (params?.tipo) hp = hp.set('tipo', params.tipo);
    return this.http.get<Habitacion[]>(this.base, { params: hp });
  }

  obtener(id: number): Observable<Habitacion> {
    return this.http.get<Habitacion>(`${this.base}/${id}`);
  }

  crear(h: Habitacion): Observable<Habitacion> {
    return this.http.post<Habitacion>(this.base, h);
  }

  actualizar(id: number, h: Habitacion): Observable<Habitacion> {
    return this.http.put<Habitacion>(`${this.base}/${id}`, h);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  buscarDisponibles(fechaIngreso: string, fechaSalida: string, tipo?: string): Observable<Habitacion[]> {
    let hp = new HttpParams();
    hp = hp.set('fechaIngreso', fechaIngreso);
    hp = hp.set('fechaSalida', fechaSalida);
    if (tipo) hp = hp.set('tipo', tipo);
    return this.http.get<Habitacion[]>(`${this.base}/disponibles`, { params: hp });
  }
}
