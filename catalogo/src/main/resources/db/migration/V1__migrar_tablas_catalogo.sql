-- Crea las tablas del catálogo

CREATE TABLE categoria (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT uk_categoria_nombre UNIQUE (nombre)
);

CREATE TABLE producto (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    imagen_url VARCHAR(500),
    activo BIT(1) NOT NULL,
    fecha_creacion DATETIME(6) NOT NULL,
    categoria_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria (id)
);