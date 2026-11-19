package cl.losmaestros.autenticacion.config;

import cl.losmaestros.autenticacion.exception.AccesoDenegadoException;
import cl.losmaestros.autenticacion.exception.TokenInvalidoException;
import cl.losmaestros.autenticacion.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SeguridadInterceptor implements HandlerInterceptor {

    private static final String MENSAJE_DENEGADO = "No tienes permiso para acceder a este recurso";

    private final JwtService jwtService;

    public SeguridadInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String metodo = request.getMethod();
        String ruta = request.getRequestURI().substring(request.getContextPath().length());

        if ("OPTIONS".equalsIgnoreCase(metodo) || esPublica(metodo, ruta)) {
            return true;
        }

        String encabezado = request.getHeader("Authorization");
        if (encabezado == null || !encabezado.startsWith("Bearer ")) {
            throw new TokenInvalidoException("Falta el token de autenticación");
        }

        Claims claims = jwtService.validar(encabezado.substring(7).trim());

        if (esBusquedaPorId(metodo, ruta)) {
            return true;
        }

        if (ruta.startsWith("/usuarios") && !"ADMIN".equals(claims.get("rol", String.class))) {
            throw new AccesoDenegadoException(MENSAJE_DENEGADO);
        }

        return true;
    }

    private boolean esPublica(String metodo, String ruta) {
        if (ruta.startsWith("/swagger-ui") || ruta.startsWith("/v3/api-docs") || ruta.startsWith("/error")) {
            return true;
        }
        return "POST".equalsIgnoreCase(metodo) && ("/auth/login".equals(ruta) || "/auth/registro".equals(ruta));
    }

    private boolean esBusquedaPorId(String metodo, String ruta) {
        return "GET".equalsIgnoreCase(metodo) && ruta.matches("^/usuarios/buscar/\\d+$");
    }
}
