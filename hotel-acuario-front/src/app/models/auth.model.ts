export type Rol = 'ADMINISTRADOR' | 'USUARIO';

export interface LoginRequest {
  nombreUsuario: string;
  contrasena: string;
}

export interface RegisterRequest {
  nombreUsuario: string;
  contrasena: string;
  rol?: Rol;
}

export interface AuthResponse {
  token: string;
  tipoToken: string;
  id: number;
  nombreUsuario: string;
  rol: Rol;
}

export interface Usuario {
  id: number;
  nombreUsuario: string;
  rol: Rol;
}

