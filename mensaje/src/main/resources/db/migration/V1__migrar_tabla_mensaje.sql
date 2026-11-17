-- Crea la tabla

CREATE TABLE mensaje (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    correo VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    mensaje TEXT NOT NULL,
    fecha_creacion DATETIME(6) NOT NULL,
    PRIMARY KEY (id)
)