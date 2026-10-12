import { HttpErrorResponse } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';

export type TipoMensaje = 'success' | 'danger' | 'info';

export interface Mensaje {
  texto: string;
  tipo: TipoMensaje;
}

@Injectable({ providedIn: 'root' })
export class MensajeService {
  mensaje = signal<Mensaje | null>(null);
  private temporizador?: ReturnType<typeof setTimeout>;

  exito(texto: string) {
    this.mostrar(texto, 'success');
  }

  info(texto: string) {
    this.mostrar(texto, 'info');
  }

  // Convierte el error de la API en un texto legible
  error(err: unknown) {
    const e = err as HttpErrorResponse;
    if (e.status === 401) return; // el interceptor ya avisó de la sesión vencida
    if (e.status === 0) {
      this.mostrar('No se pudo conectar con el servidor', 'danger');
      return;
    }
    const campos = e.error?.campos;
    const texto = campos
      ? Object.values(campos).join('. ')
      : (e.error?.mensaje ?? 'Error inesperado');
    this.mostrar(texto, 'danger');
  }

  limpiar() {
    clearTimeout(this.temporizador);
    this.mensaje.set(null);
  }

  private mostrar(texto: string, tipo: TipoMensaje) {
    clearTimeout(this.temporizador);
    this.mensaje.set({ texto, tipo });
    this.temporizador = setTimeout(() => this.mensaje.set(null), 6000);
  }
}