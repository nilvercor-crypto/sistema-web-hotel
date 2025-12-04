export type EstadoReserva = 'PENDIENTE' | 'CONFIRMADA' | 'CANCELADA' | 'FINALIZADA';

export interface ReservaServicio {
  id?: number;
  servicioId: number;
  servicioNombre?: string;
  servicioPrecio?: number;
  cantidad: number;
  subtotal?: number;
}

export interface ReservaRequest {
  clienteId: number;
  habitacionId: number;
  fechaIngreso: string;
  fechaSalida: string;
  servicios?: ReservaServicio[];
}

export interface ReservaResponse {
  id?: number;
  clienteId: number;
  clienteNombre?: string;
  clienteApellido?: string;
  habitacionId: number;
  habitacionNumero?: string;
  habitacionTipo?: string;
  fechaReserva?: string;
  fechaIngreso: string;
  fechaSalida: string;
  estado?: EstadoReserva;
  montoHabitacion?: number;
  montoServicios?: number;
  montoTotal?: number;
  servicios?: ReservaServicio[];
}

export interface Reserva {
  id?: number;
  clienteId: number;
  habitacionId: number;
  fechaReserva?: string;
  fechaIngreso: string;
  fechaSalida: string;
  estado?: EstadoReserva;
  montoTotal?: number;
  servicios?: ReservaServicio[];
}
