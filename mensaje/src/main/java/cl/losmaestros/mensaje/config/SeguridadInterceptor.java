package cl.losmaestros.mensaje.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import cl.losmaestros.mensaje.exception.AccesoDenegadoException;
import cl.losmaestros.mensaje.exception.TokenInvalidoException;
import cl.losmaestros.mensaje.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SeguridadInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;

    public SeguridadInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String metodo = request.getMethod();
        String ruta = request.getRequestURI().substring(request.getContextPath().length());

        if (esPublica(metodo, ruta)) {
            return true;
        }

        String encabezado = request.getHeader("Authorization");
        if (encabezado == null || !encabezado.startsWith("Bearer ")) {
            throw new TokenInvalidoException("Falta el token de autenticación");
        }

        Claims claims = jwtService.validar(encabezado.substring(7).trim());
        if (!"ADMIN".equals(claims.get("rol", String.class))) {
            throw new AccesoDenegadoException("No tienes permiso para acceder a este recurso");
        }
        return true;
    }

    private boolean esPublica(String metodo, String ruta) {
        return "OPTIONS".equalsIgnoreCase(metodo)
                || ("POST".equalsIgnoreCase(metodo) && "/mensajes/crear".equals(ruta))
                || ruta.startsWith("/swagger-ui")
                || ruta.startsWith("/v3/api-docs")
                || ruta.startsWith("/error");
    }
}
