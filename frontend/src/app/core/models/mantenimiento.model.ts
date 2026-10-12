export type TipoMantenimiento = 'PREVENTIVO' | 'CORRECTIVO';
export type EstadoMantenimiento = 'PENDIENTE' | 'EN_PROCESO' | 'TERMINADO';

export const TIPOS_MANTENIMIENTO: TipoMantenimiento[] = ['PREVENTIVO', 'CORRECTIVO'];

export interface Mantenimiento {
  id: number;
  equipoId: number;
  equipoNumeroSerie: string;
  fecha: string; // 'YYYY-MM-DD'
  tipo: TipoMantenimiento;
  descripcion: string;
  tecnico: string;
  estado: EstadoMantenimiento;
}

// El equipo va en la ruta (POST /api/equipos/{id}/mantenimientos), no en el cuerpo
export type MantenimientoRequest =
  Omit<Mantenimiento, 'id' | 'equipoId' | 'equipoNumeroSerie' | 'estado'>;

export interface CambioEstadoRequest {
  estado: EstadoMantenimiento;
}
