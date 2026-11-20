package cl.losmaestros.catalogo.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductoRegistroDTO {
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20, message = "El código no puede tener más de 20 caracteres")
    private String codigo;

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 100, message = "La marca no puede tener más de 100 caracteres")
    private String marca;

    @NotBlank(message = "La unidad es obligatoria")
    @Size(max = 30, message = "La unidad no puede tener más de 30 caracteres")
    private String unidad;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede tener más de 150 caracteres")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio tiene un formato inválido")
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @Size(max = 500, message = "La URL de la imagen no puede tener más de 500 caracteres")
    private String imagenUrl;

    private Boolean activo = true;

    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;
}