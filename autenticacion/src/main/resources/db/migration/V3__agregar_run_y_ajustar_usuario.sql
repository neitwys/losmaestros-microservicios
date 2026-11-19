ALTER TABLE usuario ADD COLUMN run VARCHAR(12) NULL;

UPDATE usuario SET run = '11111111-1' WHERE correo = 'admin@losmaestros.cl';
UPDATE usuario SET run = '22222222-2' WHERE correo = 'cliente@losmaestros.cl';

ALTER TABLE usuario MODIFY COLUMN run VARCHAR(12) NOT NULL;
ALTER TABLE usuario ADD CONSTRAINT uk_usuario_run UNIQUE (run);

ALTER TABLE usuario MODIFY COLUMN apellido VARCHAR(100) NULL;
ALTER TABLE usuario MODIFY COLUMN telefono VARCHAR(20) NOT NULL;
