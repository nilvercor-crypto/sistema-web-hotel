export type TipoDocumento = 'DNI' | 'CARNET_EXTRANJERIA' | 'PASAPORTE' | 'OTRO';

export interface Cliente {
  id?: number;
  nombre: string;
  apellido: string;
  tipoDocumento?: TipoDocumento;
  documentoIdentidad?: string;
  email?: string;
  celular?: string;
  direccion?: string;
  fechaRegistro?: string;
}
