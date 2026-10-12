import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';

import { environment } from '../../../environments/environment';
import { Sitio, SitioRequest } from '../models/sitio.model';

@Injectable({ providedIn: 'root' })
export class SitioService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/sitios`;

  listar() {
    return this.http.get<Sitio[]>(this.url);
  }

  obtener(id: number) {
    return this.http.get<Sitio>(`${this.url}/${id}`);
  }

  crear(s: SitioRequest) {
    return this.http.post<Sitio>(this.url, s);
  }

  actualizar(id: number, s: SitioRequest) {
    return this.http.put<Sitio>(`${this.url}/${id}`, s);
  }

  eliminar(id: number) {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
