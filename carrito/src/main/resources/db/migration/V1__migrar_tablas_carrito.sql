-- Crea las tablas del carrito

CREATE TABLE carrito (
    id BIGINT NOT NULL AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    fecha_creacion DATETIME(6) NOT NULL,
    fecha_actualizacion DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_carrito_usuario UNIQUE (usuario_id)
);

CREATE TABLE carrito_item (
    id BIGINT NOT NULL AUTO_INCREMENT,
    carrito_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    nombre_producto VARCHAR(150) NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    cantidad INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_item_carrito FOREIGN KEY (carrito_id) REFERENCES carrito (id) ON DELETE CASCADE,
    CONSTRAINT uk_item_carrito_producto UNIQUE (carrito_id, producto_id)
);