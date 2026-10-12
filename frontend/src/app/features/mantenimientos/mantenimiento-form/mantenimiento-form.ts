import { Component, inject, input, output, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { AuthService } from '../../../core/auth/auth.service';
import {
  TIPOS_MANTENIMIENTO,
  TipoMantenimiento,
} from '../../../core/models/mantenimiento.model';
import { MantenimientoService } from '../../../core/services/mantenimiento.service';
import { MensajeService } from '../../../core/services/mensaje.service';

@Component({
  selector: 'app-mantenimiento-form',
  imports: [ReactiveFormsModule],
  templateUrl: './mantenimiento-form.html',
  styleUrl: './mantenimiento-form.scss',
})
export class MantenimientoForm {
  private fb = inject(FormBuilder);
  private service = inject(MantenimientoService);
  private auth = inject(AuthService);
  private mensajes = inject(MensajeService);

  equipoId = input.required<number>();
  registrado = output<void>();
  cancelado = output<void>();

  readonly tipos = TIPOS_MANTENIMIENTO;
  guardando = signal(false);

  form = this.fb.nonNullable.group({
    fecha: [this.hoy(), Validators.required],
    tipo: ['PREVENTIVO' as TipoMantenimiento, Validators.required],
    descripcion: ['', [Validators.required, Validators.maxLength(500)]],
    tecnico: [this.auth.nombre ?? '', [Validators.required, Validators.maxLength(100)]],
  });

  guardar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.guardando.set(true);
    this.service.registrar(this.equipoId(), this.form.getRawValue()).subscribe({
      next: () => {
        this.guardando.set(false);
        this.registrado.emit();
      },
      error: e => {
        this.guardando.set(false);
        this.mensajes.error(e); // 409 si el equipo está inactivo (R2)
      },
    });
  }

  private hoy(): string {
    const d = new Date();
    const mes = String(d.getMonth() + 1).padStart(2, '0');
    const dia = String(d.getDate()).padStart(2, '0');
    return `${d.getFullYear()}-${mes}-${dia}`;
  }
}