export interface Sitio {
  id: number;
  nombre: string;
  direccion: string | null;
  cliente: string;
  creadoEn: string; // fecha y hora ISO, por ejemplo '2026-10-08T23:59:10.412'
}

export type SitioRequest = Omit<Sitio, 'id' | 'creadoEn'>;
