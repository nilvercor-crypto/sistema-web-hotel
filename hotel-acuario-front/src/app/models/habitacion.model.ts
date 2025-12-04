export type EstadoHabitacion = 'DISPONIBLE' | 'OCUPADA' | 'MANTENIMIENTO';

export interface Habitacion {
  id?: number;
  numero: string;
  tipo: string;
  capacidad: number;
  precioPorNoche: number;
  estado?: EstadoHabitacion;
}
