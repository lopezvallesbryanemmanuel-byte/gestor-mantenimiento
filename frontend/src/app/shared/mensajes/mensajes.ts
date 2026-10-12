import { Component, inject } from '@angular/core';

import { MensajeService } from '../../core/services/mensaje.service';

@Component({
  selector: 'app-mensajes',
  template: `
    @if (servicio.mensaje(); as m) {
      <div class="container mt-3">
        <div class="alert alert-{{ m.tipo }} alert-dismissible mb-0" role="alert">
          {{ m.texto }}
          <button type="button" class="btn-close" aria-label="Cerrar"
                  (click)="servicio.limpiar()"></button>
        </div>
      </div>
    }
  `,
})
export class Mensajes {
  readonly servicio = inject(MensajeService);
}