
DROP DATABASE IF EXISTS hotel_acuario;

CREATE DATABASE hotel_acuario CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hotel_acuario;

SET default_storage_engine=INNODB;

CREATE TABLE clientes (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  apellido VARCHAR(100) NOT NULL,
  tipo_documento ENUM('DNI','CARNET_EXTRANJERIA','PASAPORTE','OTRO') NOT NULL DEFAULT 'DNI',
  documento_identidad VARCHAR(20) NOT NULL,
  email VARCHAR(150),
  celular CHAR(9) NOT NULL,
  direccion VARCHAR(200),
  fecha_registro DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  CONSTRAINT pk_clientes PRIMARY KEY (id),
  CONSTRAINT uk_cliente_documento UNIQUE (documento_identidad)
) ENGINE=InnoDB;

CREATE TABLE habitaciones (
  id BIGINT NOT NULL AUTO_INCREMENT,
  numero VARCHAR(10) NOT NULL,
  tipo VARCHAR(50) NOT NULL,
  capacidad INT NOT NULL,
  precio_por_noche DECIMAL(10,2) NOT NULL,
  estado ENUM('DISPONIBLE','OCUPADA','MANTENIMIENTO') NOT NULL DEFAULT 'DISPONIBLE',
  CONSTRAINT pk_habitaciones PRIMARY KEY (id),
  CONSTRAINT uk_habitacion_numero UNIQUE (numero)
) ENGINE=InnoDB;

CREATE TABLE reservas (
  id BIGINT NOT NULL AUTO_INCREMENT,
  cliente_id BIGINT NOT NULL,
  habitacion_id BIGINT NOT NULL,
  fecha_reserva DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  fecha_ingreso DATE NOT NULL,
  fecha_salida DATE NOT NULL,
  estado ENUM('PENDIENTE','CONFIRMADA','CANCELADA','FINALIZADA') NOT NULL DEFAULT 'PENDIENTE',
  monto_total DECIMAL(10,2),
  CONSTRAINT pk_reservas PRIMARY KEY (id),
  CONSTRAINT fk_reserva_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
  CONSTRAINT fk_reserva_habitacion FOREIGN KEY (habitacion_id) REFERENCES habitaciones(id)
) ENGINE=InnoDB;

CREATE INDEX ix_reserva_habitacion ON reservas (habitacion_id);
CREATE INDEX ix_reserva_rango ON reservas (fecha_ingreso, fecha_salida);

CREATE TABLE hotel (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  ruc CHAR(11),
  direccion VARCHAR(200),
  celular CHAR(9),
  email VARCHAR(150),
  fecha_registro DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  CONSTRAINT pk_hotel PRIMARY KEY (id),
  CONSTRAINT uk_hotel_ruc UNIQUE (ruc)
) ENGINE=InnoDB;

CREATE TABLE servicios (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre_servicio VARCHAR(100) NOT NULL,
  descripcion VARCHAR(250),
  precio DECIMAL(10,2) NOT NULL,
  CONSTRAINT pk_servicios PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE reserva_servicios (
  id BIGINT NOT NULL AUTO_INCREMENT,
  reserva_id BIGINT NOT NULL,
  servicio_id BIGINT NOT NULL,
  cantidad INT DEFAULT 1,
  CONSTRAINT pk_reserva_servicios PRIMARY KEY (id),
  CONSTRAINT fk_rs_reserva FOREIGN KEY (reserva_id) REFERENCES reservas(id),
  CONSTRAINT fk_rs_servicio FOREIGN KEY (servicio_id) REFERENCES servicios(id)
) ENGINE=InnoDB;

CREATE INDEX ix_rs_reserva ON reserva_servicios (reserva_id);
CREATE INDEX ix_rs_servicio ON reserva_servicios (servicio_id);

CREATE TABLE usuarios (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre_usuario VARCHAR(50) NOT NULL,
  contrasena VARCHAR(255) NOT NULL,
  rol ENUM('ADMINISTRADOR','USUARIO') NOT NULL,
  estado ENUM('ACTIVO','INACTIVO') NOT NULL DEFAULT 'ACTIVO',
  fecha_creacion DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  CONSTRAINT pk_usuarios PRIMARY KEY (id),
  CONSTRAINT uk_usuario_nombre UNIQUE (nombre_usuario)
) ENGINE=InnoDB;

INSERT INTO clientes (nombre, apellido, tipo_documento, documento_identidad, email, celular, direccion)
VALUES ('Hector','Cconislla','DNI','12345678','hector@demo.com','987654321','Lima'),
       ('Carla','Gutierrez','DNI','87654321','carla@demo.com','123456789','Arequipa');

INSERT INTO habitaciones (numero, tipo, capacidad, precio_por_noche, estado)
VALUES ('101','Simple',2, 80.00,'DISPONIBLE'),
		 ('102','Doble',4, 120.00,'MANTENIMIENTO'),
		 ('103','Suite',6, 150.00,'DISPONIBLE'),
		 ('104','Simple',2, 80.00,'DISPONIBLE'),
		 ('105','Doble',4, 120.00,'DISPONIBLE'),
		 ('106','Suite',6, 150.00,'DISPONIBLE'),
		 ('201','Simple',2, 80.00,'DISPONIBLE'),
		 ('202','Doble',4, 120.00,'DISPONIBLE'),
		 ('203','Suite',6, 200.00,'DISPONIBLE');


INSERT INTO servicios (nombre_servicio, descripcion, precio)
VALUES ('SPA y Masajes', 'Servicio de spa con masajes relajantes y terapéuticos', 120.00),
       ('Restaurante', 'Servicio de restaurante con menú a la carta y buffet', 50.00),
       ('Lavandería', 'Servicio de lavado y planchado de ropa', 25.00),
       ('Room Service', 'Servicio de comida y bebidas a la habitación', 35.00),
       ('Gimnasio', 'Acceso al gimnasio con equipos modernos', 15.00),
       ('Piscina', 'Acceso a la piscina y área de recreación', 20.00),
       ('WiFi Premium', 'Internet de alta velocidad ilimitado', 10.00),
       ('Estacionamiento', 'Servicio de estacionamiento seguro', 12.00);

INSERT INTO reservas (cliente_id, habitacion_id, fecha_ingreso, fecha_salida, estado, monto_total)
VALUES (1, 1, '2025-12-10', '2025-12-13', 'PENDIENTE', 240.00);

INSERT INTO reservas (cliente_id, habitacion_id, fecha_ingreso, fecha_salida, estado, monto_total)
VALUES (2, 3, '2025-12-15', '2025-12-17', 'PENDIENTE', 480.00);

INSERT INTO reserva_servicios (reserva_id, servicio_id, cantidad)
VALUES (2, 1, 1),
       (2, 3, 2),
       (2, 7, 1);

INSERT INTO hotel (nombre, ruc, direccion, celular, email)
VALUES ('Hotel Acuario', '20123456789', 'Av. Principal 123, Lima', '987654321', 'info@hotelacuario.com');
