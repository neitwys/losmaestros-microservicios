package cl.losmaestros.catalogo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProductoMostrarDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String imagenUrl;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private Long categoriaId;
    private String categoriaNombre;
}
