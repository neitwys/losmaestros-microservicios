package cl.losmaestros.carrito.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CarritoItemMostrarDTO {
    private Long productoId;
    private String nombreProducto;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private BigDecimal subtotal;
}