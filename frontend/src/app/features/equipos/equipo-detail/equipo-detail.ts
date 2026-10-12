import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { AuthService } from '../../../core/auth/auth.service';
import { Equipo } from '../../../core/models/equipo.model';
import { Mantenimiento } from '../../../core/models/mantenimiento.model';
import { EquipoService } from '../../../core/services/equipo.service';
import { MantenimientoService } from '../../../core/services/mantenimiento.service';
import { MensajeService } from '../../../core/services/mensaje.service';
import { MantenimientoForm } from '../../mantenimientos/mantenimiento-form/mantenimiento-form';
import { MantenimientoList } from '../../mantenimientos/mantenimiento-list/mantenimiento-list';

@Component({
  selector: 'app-equipo-detail',
  imports: [RouterLink, MantenimientoForm, MantenimientoList],
  templateUrl: './equipo-detail.html',
  styleUrl: './equipo-detail.scss',
})
export class EquipoDetail implements OnInit {
  readonly auth = inject(AuthService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private equipoService = inject(EquipoService);
  private mantenimientoService = inject(MantenimientoService);
  private mensajes = inject(MensajeService);

  equipo = signal<Equipo | null>(null);
  historial = signal<Mantenimiento[]>([]);
  cargando = signal(true);
  mostrandoForm = signal(false);
  private id = 0;

  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.equipoService.obtener(this.id).subscribe({
      next: e => {
        this.equipo.set(e);
        this.cargando.set(false);
      },
      error: err => {
        this.mensajes.error(err); // 404 si el id no existe (R6)
        this.router.navigate(['/equipos']);
      },
    });
    this.cargarHistorial();
  }

  cargarHistorial() {
    this.mantenimientoService.historial(this.id).subscribe({
      next: h => this.historial.set(h),
      error: e => this.mensajes.error(e),
    });
  }

  alRegistrar() {
    this.mostrandoForm.set(false);
    this.mensajes.exito('Mantenimiento registrado');
    this.cargarHistorial();
  }
}