import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Hotel } from '../models/hotel.model';
import { environment } from '../../environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class HotelService {
  private readonly base = `${environment.apiBase}/hotel`;

  constructor(private http: HttpClient) {}

  obtener(): Observable<Hotel> {
    return this.http.get<Hotel>(this.base);
  }

  actualizar(hotel: Hotel): Observable<Hotel> {
    return this.http.put<Hotel>(this.base, hotel);
  }
}

