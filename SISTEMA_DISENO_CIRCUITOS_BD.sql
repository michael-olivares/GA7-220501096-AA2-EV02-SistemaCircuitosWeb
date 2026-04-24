-- ============================================================
--  SCRIPT SQL — Sistema de Diseño Integrado de Circuitos
--  GA7-220501096-AA3-EV02 — Módulos de Software Codificados
--  Autor : Michael Ronald Olivares Giraldo
--  SENA  — Ficha 3118306 — Abril 2026
--  Motor : MySQL 8.x
-- ============================================================

-- 1. Crear y seleccionar la base de datos
CREATE DATABASE IF NOT EXISTS sistema_diseno_circuitos
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE sistema_diseno_circuitos;

-- ============================================================
--  TABLA: usuario
-- ============================================================
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario      INT             NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(100)    NOT NULL,
    email           VARCHAR(150)    NOT NULL UNIQUE,
    contrasena_hash CHAR(64)        NOT NULL COMMENT 'SHA-256 hexadecimal',
    rol             ENUM('disenador','administrador','colaborador')
                                    NOT NULL DEFAULT 'disenador',
    nivel_acceso    ENUM('basico','intermedio','avanzado','total')
                                    NOT NULL DEFAULT 'basico',
    estado          ENUM('activo','inactivo','suspendido')
                                    NOT NULL DEFAULT 'activo',
    fecha_creacion  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_usuario PRIMARY KEY (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
--  TABLA: proyecto
-- ============================================================
CREATE TABLE IF NOT EXISTS proyecto (
    id_proyecto         INT             NOT NULL AUTO_INCREMENT,
    nombre              VARCHAR(150)    NOT NULL,
    descripcion         TEXT,
    tipo_circuito       ENUM('analogico','microcontrolador','fpga','asic','mixto')
                                        NOT NULL DEFAULT 'mixto',
    estado              ENUM('activo','finalizado','archivado')
                                        NOT NULL DEFAULT 'activo',
    version_actual      VARCHAR(20)     NOT NULL DEFAULT '1.0',
    id_usuario_creador  INT             NOT NULL,
    fecha_creacion      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_proyecto    PRIMARY KEY (id_proyecto),
    CONSTRAINT fk_proy_user   FOREIGN KEY (id_usuario_creador)
                              REFERENCES usuario(id_usuario)
                              ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
--  TABLA: diseno
-- ============================================================
CREATE TABLE IF NOT EXISTS diseno (
    id_diseno       INT             NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(120)    NOT NULL,
    descripcion     TEXT,
    tipo            ENUM('esquematico','pcb','simulacion','3d')
                                    NOT NULL DEFAULT 'esquematico',
    estado          ENUM('borrador','revision','aprobado','rechazado')
                                    NOT NULL DEFAULT 'borrador',
    id_proyecto     INT             NOT NULL,
    version         VARCHAR(20)     NOT NULL DEFAULT '1.0',
    fecha_creacion  TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_diseno      PRIMARY KEY (id_diseno),
    CONSTRAINT fk_dis_proy    FOREIGN KEY (id_proyecto)
                              REFERENCES proyecto(id_proyecto)
                              ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
--  DATOS DE PRUEBA — Usuarios
--  Contraseñas: admin123 | diseno123 | colab123
--  Hash calculado con: SHA2('contraseña', 256)
-- ============================================================
INSERT INTO usuario (nombre, email, contrasena_hash, rol, nivel_acceso, estado) VALUES
('Administrador SENA',   'admin@sena.edu.co',
 SHA2('admin123',  256), 'administrador', 'total',      'activo'),
('Diseñador Prueba',     'disenador@sena.edu.co',
 SHA2('diseno123', 256), 'disenador',     'intermedio', 'activo'),
('Colaborador Prueba',   'colaborador@sena.edu.co',
 SHA2('colab123',  256), 'colaborador',   'basico',     'activo');

-- ============================================================
--  DATOS DE PRUEBA — Proyectos
-- ============================================================
INSERT INTO proyecto (nombre, descripcion, tipo_circuito, estado, version_actual, id_usuario_creador) VALUES
('Control Industrial FPGA',
 'Sistema de control de motores industriales implementado en FPGA Xilinx.',
 'fpga', 'activo', '2.1', 1),
('Fuente Conmutada 12V',
 'Diseño de fuente de alimentación conmutada con regulación PWM.',
 'analogico', 'activo', '1.3', 2),
('Medidor de Temperatura IoT',
 'Dispositivo basado en microcontrolador ESP32 con sensor DS18B20.',
 'microcontrolador', 'finalizado', '3.0', 1);

-- ============================================================
--  DATOS DE PRUEBA — Diseños
-- ============================================================
INSERT INTO diseno (nombre, descripcion, tipo, estado, id_proyecto, version) VALUES
('Esquemático Principal FPGA',
 'Diagrama esquemático completo del sistema FPGA con interfaces de E/S.',
 'esquematico', 'aprobado', 1, '2.1'),
('PCB Placa Principal FPGA',
 'Layout PCB de 4 capas para la placa principal del sistema FPGA.',
 'pcb', 'revision', 1, '1.8'),
('Esquemático Fuente PWM',
 'Circuito de la fuente conmutada con topología flyback.',
 'esquematico', 'aprobado', 2, '1.3'),
('Simulación Thermal Fuente',
 'Análisis térmico simulado de los componentes de potencia.',
 'simulacion', 'aprobado', 2, '1.0'),
('Esquemático Medidor IoT',
 'Diseño del circuito sensor con comunicación WiFi integrada.',
 'esquematico', 'aprobado', 3, '3.0'),
('Modelo 3D Medidor IoT',
 'Modelo tridimensional del encapsulado del medidor de temperatura.',
 '3d', 'borrador', 3, '1.2');

-- ============================================================
--  VERIFICACIÓN
-- ============================================================
SELECT 'Usuarios:'  AS tabla, COUNT(*) AS registros FROM usuario
UNION ALL
SELECT 'Proyectos:', COUNT(*) FROM proyecto
UNION ALL
SELECT 'Diseños:',   COUNT(*) FROM diseno;
