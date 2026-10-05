-- Ejecutar una sola vez en pgAdmin (los roles deben empezar con ROLE_)
INSERT INTO roles (nombre, descripcion) VALUES ('ROLE_ADMIN', 'Administrador');
INSERT INTO roles (nombre, descripcion) VALUES ('ROLE_INVESTIGADOR', 'Investigador de fauna: registra y valida avistamientos');
INSERT INTO roles (nombre, descripcion) VALUES ('ROLE_VOLUNTARIO', 'Voluntario ambiental: registra avistamientos');
-- usuario admin, contrasena: admin123
INSERT INTO usuarios (id_rol, nombre, apellido, correo, contrasena, fecha_registro, estado) VALUES ((SELECT id_rol FROM roles WHERE nombre = 'ROLE_ADMIN'), 'Admin', 'FaunaScan', 'admin@faunascan.com', '$2a$10$vYwemkoLJrsxQRWYrkpjReNRRePOCKu3XW5zObtXBuTWLU.BP9hwm', NOW(), true);
