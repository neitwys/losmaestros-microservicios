-- Crea la tabla

CREATE TABLE contacto (
    id BIGINT NOT NULL AUTO_INCREMENT, -- Identificador único generado automáticamente por MySQL.
    nombre_completo VARCHAR(255) NOT NULL, -- Nombre completo del contacto.
    correo VARCHAR(255) NOT NULL, -- Correo electrónico del contacto.
    mensaje TEXT NOT NULL, -- Mensaje del contacto. TEXT permite almacenar cadenas de texto de longitud variable.
    fecha_creacion DATETIME(6) NOT NULL, -- Fecha y hora de creación, asignadas desde el modelo con @PrePersist.
    PRIMARY KEY (id) -- Define el identificador como clave primaria.
)