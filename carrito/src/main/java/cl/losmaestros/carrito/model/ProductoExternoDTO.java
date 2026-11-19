package cl.losmaestros.carrito.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductoExternoDTO {
    private Long id;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private Boolean activo;
}