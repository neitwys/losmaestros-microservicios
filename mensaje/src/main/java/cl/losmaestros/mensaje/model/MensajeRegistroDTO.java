package cl.losmaestros.mensaje.model;

import lombok.Data;

@Data
public class MensajeRegistroDTO {
    private String nombre;
    private String correo;
    private String telefono;
    private String mensaje;
}
