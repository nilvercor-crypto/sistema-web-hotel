import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ReservaRequest, ReservaResponse } from '../models/reserva.model';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ReservaService {
  private readonly base = `${environment.apiBase}/reservas`;

  constructor(private http: HttpClient) {}

  listar(): Observable<ReservaResponse[]> {
    return this.http.get<ReservaResponse[]>(this.base);
  }

  obtener(id: number): Observable<ReservaResponse> {
    return this.http.get<ReservaResponse>(`${this.base}/${id}`);
  }

  crear(r: ReservaRequest): Observable<ReservaResponse> {
    return this.http.post<ReservaResponse>(this.base, r);
  }

  confirmar(id: number): Observable<ReservaResponse> {
    return this.http.post<ReservaResponse>(`${this.base}/${id}/confirmar`, {});
  }

  cancelar(id: number): Observable<void> {
    return this.http.post<void>(`${this.base}/${id}/cancelar`, {});
  }

  finalizar(id: number): Observable<void> {
    return this.http.post<void>(`${this.base}/${id}/finalizar`, {});
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
