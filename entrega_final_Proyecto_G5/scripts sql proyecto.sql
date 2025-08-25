create database clinicdb;
use clinicdb;

SHOW TABLES;
-- ver contenido de la vista
select * from new_view;

-- creación de tablas
 -- tabla usuarios generales
 CREATE TABLE personas(
 cedula VARCHAR(10) PRIMARY KEY,
 nombre VARCHAR(15) NOT NULL,
 apellido VARCHAR(15) NOT NULL,
 edad INT NOT NULL,
 correo VARCHAR(30) NOT NULL UNIQUE,
 telefono VARCHAR(10) NOT NULL,
 direccion1 VARCHAR(20) NOT NULL,
 usuario VARCHAR(10) NOT NULL UNIQUE,
 contraseña VARCHAR(10) NOT NULL UNIQUE,
 rol ENUM('admin', 'staff', 'enfermero', 'cliente') NOT NULL
 );
 
 -- staff doctores
 CREATE TABLE doctores(
 cedula VARCHAR(10) PRIMARY KEY,
 especialidad VARCHAR(20) NOT NULL,
 FOREIGN KEY (cedula) REFERENCES personas(cedula) ON DELETE CASCADE
 );
 
 -- staff enfermeros
 CREATE TABLE enfermeros(
 cedula VARCHAR(10) PRIMARY KEY,
 area VARCHAR(30) NOT NULL,
 FOREIGN KEY (cedula) REFERENCES personas(cedula) ON DELETE CASCADE
 );
 
 -- staff administradores 
CREATE TABLE administradores (
    cedula VARCHAR(20) PRIMARY KEY,
    nivel_acceso ENUM('alto', 'medio', 'bajo') DEFAULT 'medio', -- diferentes niveles de administración / poder
    puede_crear_usuarios BOOLEAN DEFAULT TRUE,
    puede_eliminar_datos BOOLEAN DEFAULT FALSE,
    ultimo_acceso DATETIME,
    FOREIGN KEY (cedula) REFERENCES personas(cedula) ON DELETE CASCADE
);

-- Tabla clientes 
CREATE TABLE clientes (
    cedula VARCHAR(20) PRIMARY KEY,
    puede_agendar_citas BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (cedula) REFERENCES personas(cedula) ON DELETE CASCADE
);

-- Tabla citas
CREATE TABLE citas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cedula_doctor VARCHAR(20) NOT NULL,
    cedula_cliente VARCHAR(20) NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    departamento VARCHAR(50) NOT NULL,
    estado ENUM('pendiente', 'en_curso', 'completada', 'cancelada') DEFAULT 'pendiente',
    descripcion TEXT NOT NULL,
    observaciones TEXT NOT NULL,
    diagnostico TEXT NOT NULL,  
    FOREIGN KEY (cedula_doctor) REFERENCES doctores(cedula),
    FOREIGN KEY (cedula_cliente) REFERENCES clientes(cedula),
    CONSTRAINT uc_cita_doctor_hora UNIQUE (cedula_doctor, fecha, hora)  
);

-- tabla tratamientos
create table tratamientos(
id int primary key,
cita_id int not null,
cedula_doctor varchar(20) not null,
cedula_cliente varchar(20) not null,
receta text not null,
fecha datetime not null default now(),
FOREIGN KEY (cita_id) REFERENCES citas(id) ON DELETE CASCADE,
FOREIGN KEY (cedula_doctor) REFERENCES doctores(cedula),
FOREIGN KEY (cedula_cliente) REFERENCES clientes(cedula),
INDEX idx_tratamientos_doctor (cedula_doctor),
INDEX idx_tratamientos_cliente (cedula_cliente),
INDEX idx_tratamientos_cita (cita_id)
);


-- modificar el campo contraseñas para guardar hashedpasswords
ALTER TABLE personas MODIFY contraseña VARCHAR(255);
ALTER TABLE personas DROP column direccion2;

-- poblacion de tablas con datos de ejemplo
INSERT INTO personas (cedula, nombre, apellido, edad, correo, telefono, direccion1, usuario, contraseña, rol) VALUES
('0912345678', 'Juan', 'Pérez', 45, 'juan.perez@hospital.com', '0987654321', 'Av. Principal 123', 'jperez', '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin'),
('0923456789', 'María', 'González', 38, 'maria.gonzalez@hospital.com', '0976543210', 'Calle Secundaria 456', 'mgonzalez', '$2y$10$TKh8H1.PfQx37YgCzwiKb.KjNyWgaHb9cbcoQgdIVFlYg7B77UdFm', 'admin');

INSERT INTO personas (cedula, nombre, apellido, edad, correo, telefono, direccion1, usuario, contraseña, rol) VALUES
('0934567890', 'Carlos', 'Rodríguez', 42, 'carlos.rodriguez@hospital.com', '0965432109', 'Av. Médica 789', 'crodriguez', '$2y$10$E9bFqXl7d.oO4QjKGHh2k.9c8fOqFfLf.tGb9pqKm1Yo.6f8QvP1a', 'staff'),
('0945678901', 'Ana', 'Martínez', 35, 'ana.martinez@hospital.com', '0954321098', 'Calle Doctor 3', 'amartinez', '$2y$10$F8cGrYm8e.pP5RkLHIi3l.0d9gPrGgMg.uHc0qrLn2Zp.7g9RwQ2b', 'staff');

INSERT INTO personas (cedula, nombre, apellido, edad, correo, telefono, direccion1, usuario, contraseña, rol) VALUES
('0956789012', 'Luis', 'Fernández', 29, 'luis.fernandez@hospital.com', '0943210987', 'Av. Enfer 6', 'lfernandez', '$2y$10$G9dHsZn9f.qQ6SmMJj4m.1e0hQsHhNh.vId1rsMo3Aq.8h0SwR3c', 'enfermero'),
('0967890123', 'Carmen', 'López', 31, 'carmen.lopez@hospital.com', '0932109876', 'Calle Cuidados 9', 'clopez', '$2y$10$H0eItAo0g.rR7TnNKk5n.2f1iRtIiOi.wJe2stNp4Br.9i1TxS4d', 'enfermero');

INSERT INTO personas (cedula, nombre, apellido, edad, correo, telefono, direccion1, usuario, contraseña, rol) VALUES
('0978901234', 'Pedro', 'Silva', 55, 'pedro.silva@gmail.com', '0921098765', 'Urb Norte 1', 'psilva', '$2y$10$I1fJuBp1h.sS8UoOLl6o.3g2jSuJjPj.xKf3tuOq5Cs.0j2UyT5e', 'cliente'),
('0989012345', 'Rosa', 'Herrera', 48, 'rosa.herrera@outlook.com', '0910987654', 'Sector Sur 2', 'rherrera', '$2y$10$J2gKvCq2i.tT9VpPMm7p.4h3kTvKkQk.yLg4uvPr6Dt.1k3VzU6f', 'cliente');

INSERT INTO personas (cedula, nombre, apellido, edad, correo, telefono, direccion1, usuario, contraseña, rol) VALUES
('0990123456', 'Samuel', 'Villa', 33, 'samuel.villa@hospital.com', '0909876543', 'Calle Nueva 789', 'samvillasp_registrar_usuariolim', '$2y$10$K3hLwDr3j.uU0WqQNn8q.5i4lUwLlRl.zMh5vwQs7Eu.2l4WzV7g', 'admin');
select * from personas;

-- Insertar datos en tabla administradores
INSERT INTO administradores (cedula, nivel_acceso, puede_crear_usuarios, puede_eliminar_datos, ultimo_acceso) VALUES
('0912345678', 'alto', TRUE, TRUE, '2024-08-07 08:30:00'),
('0923456789', 'medio', TRUE, FALSE, '2024-08-06 14:15:00'),
('0990123456', 'alto', TRUE, TRUE, '2024-08-06 14:15:00');
select * from administradores;


-- Insertar datos en tabla doctores
INSERT INTO doctores (cedula, especialidad) VALUES
('0934567890', 'Cardiología'),
('0945678901', 'Pediatría');
select * from doctores;

-- Insertar datos en tabla enfermeros
INSERT INTO enfermeros (cedula, area) VALUES
('0956789012', 'Urgencias'),
('0967890123', 'Hospitalización');
select * from enfermeros;

-- Insertar datos en tabla clientes
INSERT INTO clientes (cedula, puede_agendar_citas) VALUES
('0978901234', TRUE),
('0989012345', TRUE);
select * from clientes;

-- Insertar datos en tabla citas
INSERT INTO citas (cedula_doctor, cedula_cliente, fecha, hora, departamento, estado, descripcion, observaciones, diagnostico) VALUES
('0934567890', '0978901234', '2024-08-15', '09:00:00', 'Cardiología', 'pendiente', 'Control rutinario cardiovascular', 'Paciente refiere dolores en el pecho ocasionales', 'Pendiente evaluación'),
('0945678901', '0989012345', '2024-08-16', '14:30:00', 'Pediatría', 'completada', 'Consulta pediátrica de control', 'Niño presenta síntomas de gripe leve', 'Infección viral leve, tratamiento sintomático');
select * from citas;

-- indices para las tablas

-- indice en la tabla personas, por su usuario
create unique index idx_usuario on personas(usuario);
show index from personas;
ALTER TABLE personas DROP INDEX idx_usuario;
-- por su rol
create unique index idx_personas_rol ON personas(rol);
-- por su correo
create unique index idx_personas_correo ON personas(correo);

-- indices en la tabla enfermeros
-- por su rol
create unique index idx_area on enfermeros(area);

-- indices en la tabla enfermeros
-- por su especialidad
create unique index idx_especialidad on doctores(especialidad);

-- indices en la tabla citas
CREATE INDEX idx_citas_cliente ON citas(cedula_cliente);
-- estado de la cita / consulta
CREATE INDEX idx_citas_estado ON citas(estado);
-- por fecha
CREATE INDEX idx_citas_fecha ON citas(fecha);

-- triggers
-- validación de fechas de citas, cambiar su estado a vencida si el cliente no llega a tiempo
DELIMITER //
create trigger tgr_actualizar_fecha_citas
before insert on citas
for each row
begin
	if new.fecha < curdate() then
	set new.estado = 'vencida';
	end if;
end//
DELIMITER ;

-- mostrar triggers
show triggers from clinicdb;

-- procedimientos almacenados (pendientes todavía)
-- registrar usuarios (admins)
DELIMITER //
CREATE PROCEDURE sp_registrar_usuario(
    IN p_cedula VARCHAR(10),
    IN p_nombre VARCHAR(15),
    IN p_apellido VARCHAR(15),
    IN p_edad INT,
    IN p_correo VARCHAR(30),
    IN p_telefono VARCHAR(10),
    IN p_direccion VARCHAR(20),
    IN p_usuario VARCHAR(10),
    IN p_contrasena VARCHAR(255),
    IN p_rol ENUM('admin', 'staff', 'enfermero', 'cliente'),
    IN p_especialidad VARCHAR(20) -- Solo para doctores si aplica
)
BEGIN
    START TRANSACTION;
    -- Insertar en tabla personas
    INSERT INTO personas (cedula, nombre, apellido, edad, correo, telefono, direccion1, usuario, contraseña, rol)
    VALUES (p_cedula, p_nombre, p_apellido, p_edad, p_correo, p_telefono, p_direccion, p_usuario, p_contrasena, p_rol);
    
    -- Insertar en tabla específica según rol
    -- doctores
    IF p_rol = 'staff' AND p_especialidad IS NOT NULL THEN
        INSERT INTO doctores (cedula, especialidad) VALUES (p_cedula, p_especialidad);
	-- admins
    ELSEIF p_rol = 'admin' THEN
        INSERT INTO administradores (cedula, nivel_acceso) VALUES (p_cedula, 'medio');
    END IF;
    
    COMMIT;
    SELECT 'Registro exitoso' AS mensaje;
END //
DELIMITER ;

drop procedure sp_registrar_usuario;
-- llamada (call) al procedimiento en el java

-- scripts de consulta
-- 1. Listar todos los usuarios del sistema con sus roles
SELECT cedula, nombre, apellido, rol FROM personas ORDER BY rol, apellido;

-- cantidad de personas por rol
SELECT rol, COUNT(*) as cantidad 
FROM personas 
GROUP BY rol 
ORDER BY cantidad DESC;

-- citas por doctor
CREATE OR REPLACE VIEW view_citas_doctor AS
SELECT
  c.id AS cita_id,
  c.cedula_doctor,
  pD.nombre  AS doctor_nombre,
  pD.apellido AS doctor_apellido,
  c.cedula_cliente,
  pC.nombre  AS cliente_nombre,
  pC.apellido AS cliente_apellido,
  c.fecha, c.hora, c.departamento, c.estado,
  c.descripcion, c.observaciones, c.diagnostico
FROM citas c
JOIN personas pD ON pD.cedula = c.cedula_doctor
JOIN personas pC ON pC.cedula = c.cedula_cliente;

-- vistas para tratamientos
CREATE OR REPLACE VIEW vw_tratamientos_doctor AS
SELECT
  t.id AS tratamiento_id,
  t.cita_id,
  t.cedula_doctor,
  pD.nombre  AS doctor_nombre,
  pD.apellido AS doctor_apellido,
  t.cedula_cliente,
  pC.nombre  AS cliente_nombre,
  pC.apellido AS cliente_apellido,
  t.receta,
  t.fecha
FROM tratamientos t
JOIN personas pD ON pD.cedula = t.cedula_doctor
JOIN personas pC ON pC.cedula = t.cedula_cliente;

-- vista usuarios para admin
CREATE OR REPLACE VIEW vw_personas_resumen AS
SELECT cedula, nombre, apellido, edad, correo, telefono, direccion1 AS direccion, usuario, rol
FROM personas;

-- stored procedure para que el admin registre un usuario
DELIMITER //
CREATE PROCEDURE sp_admin_registrar_usuario(
  IN p_cedula VARCHAR(10),
  IN p_nombre VARCHAR(15),
  IN p_apellido VARCHAR(15),
  IN p_edad INT,
  IN p_correo VARCHAR(30),
  IN p_telefono VARCHAR(10),
  IN p_direccion VARCHAR(20),
  IN p_usuario VARCHAR(10),
  IN p_contrasena VARCHAR(255),
  IN p_rol ENUM('admin','staff','enfermero','cliente'),
  IN p_especialidad VARCHAR(20),
  IN p_area VARCHAR(30),
  IN p_nivel_acceso ENUM('alto','medio','bajo')
)
BEGIN
  START TRANSACTION;

  INSERT INTO personas(cedula, nombre, apellido, edad, correo, telefono, direccion1, usuario, contraseña, rol)
  VALUES (p_cedula, p_nombre, p_apellido, p_edad, p_correo, p_telefono, p_direccion, p_usuario, p_contrasena, p_rol);

  IF p_rol = 'staff' THEN
    INSERT INTO doctores(cedula, especialidad) VALUES (p_cedula, COALESCE(p_especialidad,'General'));
  ELSEIF p_rol = 'enfermero' THEN
    INSERT INTO enfermeros(cedula, area) VALUES (p_cedula, COALESCE(p_area,'General'));
  ELSEIF p_rol = 'admin' THEN
    INSERT INTO administradores(cedula, nivel_acceso, puede_crear_usuarios, puede_eliminar_datos, ultimo_acceso)
    VALUES (p_cedula, COALESCE(p_nivel_acceso,'medio'), TRUE, FALSE, NOW());
  ELSEIF p_rol = 'cliente' THEN
    INSERT INTO clientes(cedula, puede_agendar_citas) VALUES (p_cedula, TRUE);
  END IF;

  COMMIT;
END//
DELIMITER ;

-- admin: eliminar usuario por nombre o correo
DELIMITER //
CREATE PROCEDURE sp_admin_eliminar_usuario(
  IN p_nombre VARCHAR(15),
  IN p_correo VARCHAR(30)
)
BEGIN
  -- elimina por match exacto en nombre o correo
  DELETE FROM personas
   WHERE (p_nombre IS NOT NULL AND nombre = p_nombre)
      OR (p_correo IS NOT NULL AND correo = p_correo);
END//
DELIMITER ;

-- Vista para las citas de clientes
CREATE OR REPLACE VIEW vw_citas_cliente AS
SELECT
  c.id AS cita_id,
  c.cedula_doctor,
  pD.nombre  AS doctor_nombre,
  pD.apellido AS doctor_apellido,
  d.especialidad AS doctor_especialidad,
  c.cedula_cliente,
  pC.nombre  AS cliente_nombre,
  pC.apellido AS cliente_apellido,
  c.fecha, 
  c.hora, 
  c.departamento, 
  c.estado,
  c.descripcion, 
  c.observaciones, 
  c.diagnostico
FROM citas c
JOIN personas pD ON pD.cedula = c.cedula_doctor
JOIN personas pC ON pC.cedula = c.cedula_cliente
JOIN doctores d ON d.cedula = c.cedula_doctor
ORDER BY c.fecha DESC, c.hora DESC;

-- Procedimiento para eliminar cita
DELIMITER //
CREATE PROCEDURE sp_eliminar_cita(
    IN p_cita_id INT,
    IN p_cedula_cliente VARCHAR(20)
)
BEGIN
    DELETE FROM citas 
    WHERE id = p_cita_id 
    AND cedula_cliente = p_cedula_cliente;
END//
DELIMITER ;
 
 
-- Procedimiento almacenado para crear citas
DELIMITER //
CREATE PROCEDURE sp_crear_cita(
    IN p_cedula_doctor VARCHAR(20),
    IN p_cedula_cliente VARCHAR(20),
    IN p_fecha DATE,
    IN p_hora TIME,
    IN p_departamento VARCHAR(50),
    IN p_descripcion TEXT,
    IN p_observaciones TEXT,
    IN p_diagnostico TEXT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    -- Verificar que el doctor existe y está disponible
    IF NOT EXISTS (
        SELECT 1 FROM doctores d 
        JOIN personas p ON d.cedula = p.cedula 
        WHERE d.cedula = p_cedula_doctor AND p.rol = 'staff'
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El doctor especificado no existe o no está activo';
    END IF;
    
    -- Verificar que el cliente existe
    IF NOT EXISTS (
        SELECT 1 FROM clientes c 
        JOIN personas p ON c.cedula = p.cedula 
        WHERE c.cedula = p_cedula_cliente AND p.rol = 'cliente'
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El cliente especificado no existe';
    END IF;
    
    -- Verificar que no haya conflicto de horario
    IF EXISTS (
        SELECT 1 FROM citas 
        WHERE cedula_doctor = p_cedula_doctor 
        AND fecha = p_fecha 
        AND hora = p_hora 
        AND estado NOT IN ('cancelada')
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El doctor ya tiene una cita programada en ese horario';
    END IF;
    
    -- Insertar la nueva cita
    INSERT INTO citas (
        cedula_doctor, 
        cedula_cliente, 
        fecha, 
        hora, 
        departamento, 
        estado, 
        descripcion, 
        observaciones, 
        diagnostico
    ) VALUES (
        p_cedula_doctor,
        p_cedula_cliente,
        p_fecha,
        p_hora,
        p_departamento,
        'pendiente',
        p_descripcion,
        p_observaciones,
        p_diagnostico
    );
    
    COMMIT;
END//
DELIMITER ;
 
