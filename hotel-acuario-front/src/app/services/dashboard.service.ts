import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DashboardStats } from '../models/dashboard.model';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  private readonly base = `${environment.apiBase}/dashboard`;

  constructor(private http: HttpClient) {}

  obtenerEstadisticas(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${this.base}/estadisticas`);
  }
}

