package cl.losmaestros.autenticacion.model;

import lombok.Data;

@Data
public class LoginRespuestaDTO {

    private String token;
    private String tipo = "Bearer";
    private UsuarioMostrarDTO usuario;
}
