package cl.losmaestros.mensaje.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MensajeMostrarDTO {
    private Long id;
    private String nombre;
    private String correo;
    private String telefono;
    private String mensaje;
    private LocalDateTime fechaCreacion;
}
