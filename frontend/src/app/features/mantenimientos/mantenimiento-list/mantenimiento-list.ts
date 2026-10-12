import { Component, inject, input, output, signal } from '@angular/core';

import {
  EstadoMantenimiento,
  Mantenimiento,
} from '../../../core/models/mantenimiento.model';
import { MantenimientoService } from '../../../core/services/mantenimiento.service';
import { MensajeService } from '../../../core/services/mensaje.service';
import { EstadoBadge } from '../../../shared/estado-badge/estado-badge';

// El estado solo avanza un paso (regla R3 del backend)
const SIGUIENTE: Partial<Record<EstadoMantenimiento, EstadoMantenimiento>> = {
  PENDIENTE: 'EN_PROCESO',
  EN_PROCESO: 'TERMINADO',
};

@Component({
  selector: 'app-mantenimiento-list',
  imports: [EstadoBadge],
  templateUrl: './mantenimiento-list.html',
  styleUrl: './mantenimiento-list.scss',
})
export class MantenimientoList {
  private service = inject(MantenimientoService);
  private mensajes = inject(MensajeService);

  items = input.required<Mantenimiento[]>();
  cambiado = output<void>();
  procesando = signal<number | null>(null);

  siguiente(m: Mantenimiento) {
    return SIGUIENTE[m.estado];
  }

  avanzar(m: Mantenimiento) {
    const nuevo = SIGUIENTE[m.estado];
    if (!nuevo) return;
    this.procesando.set(m.id);
    this.service.cambiarEstado(m.id, nuevo).subscribe({
      next: () => {
        this.procesando.set(null);
        this.mensajes.exito('Estado actualizado');
        this.cambiado.emit();
      },
      error: e => {
        this.procesando.set(null);
        this.mensajes.error(e);
      },
    });
  }
}