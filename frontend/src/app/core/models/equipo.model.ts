export type TipoEquipo = 'CAMARA' | 'PANEL_ALARMA' | 'LECTOR_ACCESO' | 'DETECTOR_HUMO' | 'OTRO';

export const TIPOS_EQUIPO: TipoEquipo[] =
  ['CAMARA', 'PANEL_ALARMA', 'LECTOR_ACCESO', 'DETECTOR_HUMO', 'OTRO'];

export interface Equipo {
  id: number;
  sitioId: number;
  sitioNombre: string;
  tipo: TipoEquipo;
  marca: string;
  modelo: string;
  numeroSerie: string;
  fechaInstalacion: string; // 'YYYY-MM-DD'
  activo: boolean;
}

export type EquipoRequest = Omit<Equipo, 'id' | 'sitioNombre' | 'activo'>;
