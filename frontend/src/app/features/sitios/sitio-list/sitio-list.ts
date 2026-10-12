import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { AuthService } from '../../../core/auth/auth.service';
import { Sitio, SitioRequest } from '../../../core/models/sitio.model';
import { MensajeService } from '../../../core/services/mensaje.service';
import { SitioService } from '../../../core/services/sitio.service';

@Component({
  selector: 'app-sitio-list',
  imports: [ReactiveFormsModule],
  templateUrl: './sitio-list.html',
  styleUrl: './sitio-list.scss',
})
export class SitioList implements OnInit {
  readonly auth = inject(AuthService);
  private service = inject(SitioService);
  private mensajes = inject(MensajeService);
  private fb = inject(FormBuilder);

  sitios = signal<Sitio[]>([]);
  cargando = signal(true);
  mostrandoForm = signal(false);
  editandoId = signal<number | null>(null);
  guardando = signal(false);

  form = this.fb.nonNullable.group({
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
    direccion: ['', Validators.maxLength(200)],
    cliente: ['', [Validators.required, Validators.maxLength(100)]],
  });

  ngOnInit() {
    this.cargar();
  }

  cargar() {
    this.cargando.set(true);
    this.service.listar().subscribe({
      next: s => {
        this.sitios.set(s);
        this.cargando.set(false);
      },
      error: e => {
        this.cargando.set(false);
        this.mensajes.error(e);
      },
    });
  }

  nuevo() {
    this.editandoId.set(null);
    this.form.reset();
    this.mostrandoForm.set(true);
  }

  editar(s: Sitio) {
    this.editandoId.set(s.id);
    this.form.reset({ nombre: s.nombre, direccion: s.direccion ?? '', cliente: s.cliente });
    this.mostrandoForm.set(true);
  }

  cancelar() {
    this.mostrandoForm.set(false);
  }

  guardar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.guardando.set(true);

    const v = this.form.getRawValue();
    const datos: SitioRequest = {
      nombre: v.nombre,
      direccion: v.direccion || null,
      cliente: v.cliente,
    };
    const id = this.editandoId();
    const peticion = id === null
      ? this.service.crear(datos)
      : this.service.actualizar(id, datos);

    peticion.subscribe({
      next: () => {
        this.guardando.set(false);
        this.mostrandoForm.set(false);
        this.mensajes.exito(id === null ? 'Sitio creado' : 'Sitio actualizado');
        this.cargar();
      },
      error: e => {
        this.guardando.set(false);
        this.mensajes.error(e);
      },
    });
  }

  eliminar(s: Sitio) {
    if (!confirm(`¿Eliminar el sitio "${s.nombre}"?`)) return;
    this.service.eliminar(s.id).subscribe({
      next: () => {
        this.mensajes.exito('Sitio eliminado');
        this.cargar();
      },
      error: e => this.mensajes.error(e), // 409 si el sitio todavía tiene equipos (R5)
    });
  }
}