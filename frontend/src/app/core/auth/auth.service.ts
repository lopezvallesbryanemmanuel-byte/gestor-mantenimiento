import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { tap } from 'rxjs';

import { environment } from '../../../environments/environment';
import { LoginResponse, Rol, UsuarioSesion } from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private readonly KEY = 'token';

  usuario = signal<UsuarioSesion | null>(this.leerSesion());

  login(username: string, password: string) {
    return this.http
      .post<LoginResponse>(`${environment.apiUrl}/auth/login`, { username, password })
      .pipe(tap(r => {
        localStorage.setItem(this.KEY, r.token);
        this.usuario.set(this.leerSesion());
      }));
  }

  logout() {
    localStorage.removeItem(this.KEY);
    this.usuario.set(null);
    this.router.navigate(['/login']);
  }

  get token(): string | null {
    return localStorage.getItem(this.KEY);
  }

  estaAutenticado(): boolean {
    const s = this.leerSesion();
    return !!s && s.exp * 1000 > Date.now();
  }

  tieneRol(rol: Rol): boolean {
    return this.usuario()?.rol === rol;
  }

  // Lee el payload del JWT solo para la interfaz. La seguridad real la valida el backend.
  private leerSesion(): UsuarioSesion | null {
    const t = localStorage.getItem(this.KEY);
    if (!t) return null;
    try {
      const base64 = t.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
      const p = JSON.parse(atob(base64));
      return { username: p.sub, rol: p.rol, exp: p.exp };
    } catch {
      return null;
    }
  }
}
