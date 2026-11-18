-- Usuarios de prueba
-- admin@losmaestros.cl   / admin123
-- cliente@losmaestros.cl / cliente123

INSERT INTO usuario (nombre, apellido, correo, contrasena, telefono, rol, activo, fecha_creacion) VALUES
('Admin', 'Los Maestros', 'admin@losmaestros.cl', '$2b$10$.6DEAZldu2O3EXV4O8t4SOsCVMEoyThb3Ei0ZPdx/Tc8L8cykaEei', '+56911111111', 'ADMIN', 1, NOW(6)),
('Cliente', 'Prueba', 'cliente@losmaestros.cl', '$2b$10$CzwqmR9DtPdG1qugPQ9Q1erRRRAFx84OJhe833OytF.3vE/Vq1rWa', '+56922222222', 'CLIENTE', 1, NOW(6));