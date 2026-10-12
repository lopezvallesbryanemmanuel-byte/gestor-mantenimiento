export type Rol = 'ADMIN' | 'TECNICO';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  tipo: string;
  expiraEnMs: number;
  username: string;
  nombre: string;
  rol: Rol;
}

export interface UsuarioSesion {
  username: string;
  rol: Rol;
  exp: number; // segundos desde 1970 (viene en el JWT)
}

// Respuesta de GET /api/auth/me
export interface Usuario {
  id: number;
  username: string;
  nombre: string;
  rol: Rol;
}

// Cuerpo de los errores de la API; los de seguridad (401 y 403) solo traen status y mensaje
export interface ApiError {
  status: number;
  mensaje: string;
  error?: string;
  campos?: Record<string, string>;
  fecha?: string;
}
