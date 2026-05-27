DROP DATABASE IF EXISTS quick_delivery_db;
CREATE DATABASE quick_delivery_db;
USE quick_delivery_db;

-- ==========================================
-- 1. GESTIÓN DE TABLAS ENUMS Y CONSTRAINTS
-- ==========================================

CREATE TABLE roles (
    id_rol INT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE tipos_vehiculo (
    id_tipoVehiculo INT PRIMARY KEY,
    descripcion VARCHAR(50) NOT NULL
);

CREATE TABLE estado_vehiculos (
    id_estado_vehiculo INT PRIMARY KEY,
    descripcion VARCHAR(50) NOT NULL
); 

CREATE TABLE estados_paquete (
    id_estado INT PRIMARY KEY, 
    descripcion VARCHAR(50) NOT NULL
);

-- ==========================================
-- 2. GESTIÓN DE SEGURIDAD Y ROLES
-- ==========================================

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL, 
    apellido VARCHAR(50) NOT NULL, 
    fechaNacimiento DATE,
    dni VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE,
    telefono VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    id_rol INT,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

-- ==========================================
-- 3. GESTIÓN DE FLOTA
-- ==========================================

CREATE TABLE vehiculos (
    id_vehiculo INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(20) NOT NULL UNIQUE,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    id_tipoVehiculo INT,
    id_estado_vehiculo INT,
    FOREIGN KEY (id_tipoVehiculo) REFERENCES tipos_vehiculo(id_tipoVehiculo),
    FOREIGN KEY (id_estado_vehiculo) REFERENCES estado_vehiculos(id_estado_vehiculo) 
);

-- ==========================================
-- 4. PERSONAL OPERATIVO
-- ==========================================
CREATE TABLE conductores (
    id_conductor INT PRIMARY KEY,
    id_vehiculo_asignado INT NULL, 
    FOREIGN KEY (id_conductor) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_vehiculo_asignado) REFERENCES vehiculos(id_vehiculo)
);

-- ==========================================
-- 5. GESTIÓN DE ENTREGAS Y PAQUETES
-- ==========================================

CREATE TABLE paquetes (
    id_paquete INT AUTO_INCREMENT PRIMARY KEY,
    descripcion TEXT,
    remitente VARCHAR(100),
    destinatario VARCHAR(100),
    direccion_entrega TEXT NOT NULL,
    peso DECIMAL(10, 2),
    id_estado INT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    FOREIGN KEY (id_estado) REFERENCES estados_paquete(id_estado) 
);

CREATE TABLE asignaciones (
    id_asignacion INT AUTO_INCREMENT PRIMARY KEY,
    id_paquete INT,
    id_conductor INT,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_paquete) REFERENCES paquetes(id_paquete),
    FOREIGN KEY (id_conductor) REFERENCES conductores(id_conductor)
);

-- ==========================================
-- 6. MONITOREO Y TRAZABILIDAD
-- ==========================================
CREATE TABLE ubicaciones_vehiculos (
    id_ubicacion INT AUTO_INCREMENT PRIMARY KEY,
    id_vehiculo INT,
    latitud DECIMAL(10, 8),
    longitud DECIMAL(11, 8),
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_vehiculo) REFERENCES vehiculos(id_vehiculo)
);

CREATE TABLE incidencias (
    id_incidencia INT AUTO_INCREMENT PRIMARY KEY,
    id_paquete INT,
    id_conductor INT,
    descripcion TEXT NOT NULL,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_paquete) REFERENCES paquetes(id_paquete),
    FOREIGN KEY (id_conductor) REFERENCES conductores(id_conductor)
);

-- ==========================================
-- 7. AUDITORÍA DEL SISTEMA
-- ==========================================
CREATE TABLE logs_sistema (
    id_log INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NULL, 
    accion TEXT NOT NULL, 
    detalles TEXT,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- ==========================================
-- DATA SEEDING (INSERCIONES BASE)
-- ==========================================
INSERT INTO roles (id_rol, nombre) VALUES 
(1, 'Administrador'),
(2, 'Conductor');

INSERT INTO tipos_vehiculo (id_tipoVehiculo, descripcion) VALUES 
(1, 'Camioneta'), 
(2, 'Motocicleta'), 
(3, 'Camión pesado');

INSERT INTO estados_paquete (id_estado, descripcion) VALUES 
(1, 'En Bodega'),
(2, 'Asignado a Ruta o En Tránsito'),
(3, 'Entregado'),
(4, 'Con Incidencias');

INSERT INTO estado_vehiculos (id_estado_vehiculo, descripcion) VALUES 
(1, 'Disponible'),
(2, 'En_Ruta'),
(3, 'En_Mantenimiento');

-- ==========================================
-- Creación de usuarios de prueba
-- ==========================================
INSERT INTO usuarios (nombre, apellido, fechaNacimiento, dni, telefono, email, password, id_rol) 
VALUES ('Admin', 'Sistema', '1990-01-01', '12345678', '88888888', 'admin@mail.com', 'admin123', 1);

INSERT INTO usuarios (nombre, apellido, fechaNacimiento, dni, telefono, email, password, id_rol) 
VALUES ('Juan', 'Perez', '1990-02-02', '87654321', '11111111', 'juan@mail.com', 'juan123', 2);