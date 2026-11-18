package cl.losmaestros.autenticacion.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UsuarioMostrarDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String rol;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}