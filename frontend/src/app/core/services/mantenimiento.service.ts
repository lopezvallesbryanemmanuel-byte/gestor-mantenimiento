import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import { environment } from '../../../environments/environment';
import {
  EstadoMantenimiento,
  Mantenimiento,
  MantenimientoRequest,
} from '../models/mantenimiento.model';

@Injectable({ providedIn: 'root' })
export class MantenimientoService {
  private http = inject(HttpClient);
  private base = environment.apiUrl;

  // Historial de un equipo, del más reciente al más antiguo
  historial(equipoId: number) {
    return this.http.get<Mantenimiento[]>(`${this.base}/equipos/${equipoId}/mantenimientos`);
  }

  // Registrar un mantenimiento: el equipo va en la ruta
  registrar(equipoId: number, m: MantenimientoRequest) {
    return this.http.post<Mantenimiento>(`${this.base}/equipos/${equipoId}/mantenimientos`, m);
  }

  // Búsqueda por estado y rango de fechas ('YYYY-MM-DD'); todos los filtros son opcionales
  buscar(estado?: EstadoMantenimiento, desde?: string, hasta?: string) {
    let params = new HttpParams();
    if (estado) params = params.set('estado', estado);
    if (desde) params = params.set('desde', desde);
    if (hasta) params = params.set('hasta', hasta);
    return this.http.get<Mantenimiento[]>(`${this.base}/mantenimientos`, { params });
  }

  actualizar(id: number, m: MantenimientoRequest) {
    return this.http.put<Mantenimiento>(`${this.base}/mantenimientos/${id}`, m);
  }

  cambiarEstado(id: number, estado: EstadoMantenimiento) {
    return this.http.patch<Mantenimiento>(`${this.base}/mantenimientos/${id}/estado`, { estado });
  }
}
