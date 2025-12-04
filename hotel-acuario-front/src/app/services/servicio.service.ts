import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Servicio } from '../models/servicio.model';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ServicioService {
  private readonly base = `${environment.apiBase}/servicios`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Servicio[]> {
    return this.http.get<Servicio[]>(this.base);
  }

  obtener(id: number): Observable<Servicio> {
    return this.http.get<Servicio>(`${this.base}/${id}`);
  }

  crear(servicio: Servicio): Observable<Servicio> {
    return this.http.post<Servicio>(this.base, servicio);
  }

  actualizar(id: number, servicio: Servicio): Observable<Servicio> {
    return this.http.put<Servicio>(`${this.base}/${id}`, servicio);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}




