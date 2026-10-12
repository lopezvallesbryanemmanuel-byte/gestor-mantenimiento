import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { AuthService } from '../../../core/auth/auth.service';
import { Equipo, TIPOS_EQUIPO, TipoEquipo } from '../../../core/models/equipo.model';
import { Sitio } from '../../../core/models/sitio.model';
import { EquipoService } from '../../../core/services/equipo.service';
import { MensajeService } from '../../../core/services/mensaje.service';
import { SitioService } from '../../../core/services/sitio.service';

@Component({
  selector: 'app-equipo-list',
  imports: [RouterLink, FormsModule],
  templateUrl: './equipo-list.html',
  styleUrl: './equipo-list.scss',
})
export class EquipoList implements OnInit {
  readonly auth = inject(AuthService);
  private equipoService = inject(EquipoService);
  private sitioService = inject(SitioService);
  private mensajes = inject(MensajeService);

  readonly tipos = TIPOS_EQUIPO;

  equipos = signal<Equipo[]>([]);
  sitios = signal<Sitio[]>([]);
  cargando = signal(true);

  sitioId: number | null = null;
  tipo: TipoEquipo | null = null;

  ngOnInit() {
    this.sitioService.listar().subscribe({
      next: s => this.sitios.set(s),
      error: e => this.mensajes.error(e),
    });
    this.cargar();
  }

  cambiarSitio(valor: number | null) {
    this.sitioId = valor;
    this.cargar();
  }

  cambiarTipo(valor: TipoEquipo | null) {
    this.tipo = valor;
    this.cargar();
  }

  cargar() {
    this.cargando.set(true);
    this.equipoService.listar(this.sitioId ?? undefined, this.tipo ?? undefined).subscribe({
      next: e => {
        this.equipos.set(e);
        this.cargando.set(false);
      },
      error: e => {
        this.cargando.set(false);
        this.mensajes.error(e);
      },
    });
  }

  desactivar(e: Equipo) {
    if (!confirm(`¿Desactivar el equipo ${e.numeroSerie}? Conservará su historial.`)) return;
    this.equipoService.desactivar(e.id).subscribe({
      next: () => {
        this.mensajes.exito('Equipo desactivado');
        this.cargar();
      },
      error: err => this.mensajes.error(err),
    });
  }
}