package cl.losmaestros.carrito.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class CarritoMostrarDTO {
    private Long id;
    private Long usuarioId;
    private List<CarritoItemMostrarDTO> items;
    private Integer cantidadProductos;
    private BigDecimal total;
    private LocalDateTime fechaActualizacion;
}