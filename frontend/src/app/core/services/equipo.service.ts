import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { Equipo, EquipoRequest, TipoEquipo } from '../models/equipo.model';

@Injectable({ providedIn: 'root' })
export class EquipoService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/equipos`;

  listar(sitioId?: number, tipo?: TipoEquipo): Observable<Equipo[]> {
    let params = new HttpParams();
    if (sitioId) params = params.set('sitioId', sitioId);
    if (tipo) params = params.set('tipo', tipo);
    return this.http.get<Equipo[]>(this.url, { params });
  }

  obtener(id: number) {
    return this.http.get<Equipo>(`${this.url}/${id}`);
  }

  crear(e: EquipoRequest) {
    return this.http.post<Equipo>(this.url, e);
  }

  actualizar(id: number, e: EquipoRequest) {
    return this.http.put<Equipo>(`${this.url}/${id}`, e);
  }

  desactivar(id: number) {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
