import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { EquipoRequest, TIPOS_EQUIPO, TipoEquipo } from '../../../core/models/equipo.model';
import { Sitio } from '../../../core/models/sitio.model';
import { EquipoService } from '../../../core/services/equipo.service';
import { MensajeService } from '../../../core/services/mensaje.service';
import { SitioService } from '../../../core/services/sitio.service';

@Component({
  selector: 'app-equipo-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './equipo-form.html',
  styleUrl: './equipo-form.scss',
})
export class EquipoForm implements OnInit {
  private fb = inject(FormBuilder);
  private service = inject(EquipoService);
  private sitioService = inject(SitioService);
  private mensajes = inject(MensajeService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  readonly tipos = TIPOS_EQUIPO;
  readonly hoy = this.fechaLocal();

  sitios = signal<Sitio[]>([]);
  errorServidor = signal('');
  guardando = signal(false);
  equipoId: number | null = null; // null = crear; con valor = editar

  form = this.fb.nonNullable.group({
    sitioId: [0, [Validators.required, Validators.min(1)]],
    tipo: ['CAMARA' as TipoEquipo, Validators.required],
    marca: ['', [Validators.required, Validators.maxLength(50)]],
    modelo: ['', [Validators.required, Validators.maxLength(50)]],
    numeroSerie: ['', [Validators.required, Validators.maxLength(50)]],
    fechaInstalacion: ['', Validators.required],
  });

  get esEdicion(): boolean {
    return this.equipoId !== null;
  }

  ngOnInit() {
    this.sitioService.listar().subscribe({
      next: s => this.sitios.set(s),
      error: () => this.errorServidor.set('No se pudo cargar la lista de sitios'),
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.equipoId = Number(id);
      this.service.obtener(this.equipoId).subscribe({
        next: e =>
          this.form.patchValue({
            sitioId: e.sitioId,
            tipo: e.tipo,
            marca: e.marca,
            modelo: e.modelo,
            numeroSerie: e.numeroSerie,
            fechaInstalacion: e.fechaInstalacion,
          }),
        error: () => this.router.navigate(['/equipos']),
      });
    }
  }

  guardar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.errorServidor.set('');
    this.guardando.set(true);

    const datos: EquipoRequest = this.form.getRawValue();
    const peticion = this.equipoId === null
      ? this.service.crear(datos)
      : this.service.actualizar(this.equipoId, datos);

    peticion.subscribe({
      next: e => {
        this.mensajes.exito(this.esEdicion ? 'Equipo actualizado' : 'Equipo creado');
        this.router.navigate(['/equipos', e.id]);
      },
      error: err => {
        this.guardando.set(false);
        const campos = err.error?.campos;
        this.errorServidor.set(
          campos
            ? Object.values(campos).join('. ')
            : (err.error?.mensaje ?? 'Error inesperado')
        );
      },
    });
  }

  // Fecha de hoy en hora local, formato YYYY-MM-DD (la API no admite fechas futuras)
  private fechaLocal(): string {
    const d = new Date();
    const mes = String(d.getMonth() + 1).padStart(2, '0');
    const dia = String(d.getDate()).padStart(2, '0');
    return `${d.getFullYear()}-${mes}-${dia}`;
  }
}