package cl.losmaestros.mensaje.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cl.losmaestros.mensaje.exception.AccesoDenegadoException;
import cl.losmaestros.mensaje.exception.TokenInvalidoException;

@RestControllerAdvice
public class ManejadorExcepciones {

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<Map<String, Object>> manejarTokenInvalido(TokenInvalidoException e) {
        return construirRespuesta(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<Map<String, Object>> manejarAccesoDenegado(AccesoDenegadoException e) {
        return construirRespuesta(HttpStatus.FORBIDDEN, e.getMessage());
    }

    private ResponseEntity<Map<String, Object>> construirRespuesta(HttpStatus status, String mensaje) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("estado", status.value());
        cuerpo.put("mensaje", mensaje);
        return ResponseEntity.status(status).body(cuerpo);
    }
}
