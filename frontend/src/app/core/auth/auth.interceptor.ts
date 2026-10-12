import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';

import { MensajeService } from '../services/mensaje.service';
import { AuthService } from './auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const mensajes = inject(MensajeService);
  const token = auth.token;

  const peticion = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(peticion).pipe(
    catchError((err: HttpErrorResponse) => {
      if (err.status === 401 && !req.url.includes('/auth/login')) {
        mensajes.info('Tu sesión expiró. Inicia sesión nuevamente.');
        auth.logout(); // token vencido o inválido
      }
      return throwError(() => err);
    })
  );
};
