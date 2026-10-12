import { Component, computed, input } from '@angular/core';

import { EstadoMantenimiento } from '../../core/models/mantenimiento.model';

@Component({
  selector: 'app-estado-badge',
  template: `<span class="badge" [class]="clase()">{{ etiqueta() }}</span>`,
})
export class EstadoBadge {
  estado = input.required<EstadoMantenimiento>();

  clase = computed(() => ({
    PENDIENTE: 'text-bg-warning',
    EN_PROCESO: 'text-bg-info',
    TERMINADO: 'text-bg-success',
  }[this.estado()]));

  etiqueta = computed(() => ({
    PENDIENTE: 'Pendiente',
    EN_PROCESO: 'En proceso',
    TERMINADO: 'Terminado',
  }[this.estado()]));
}