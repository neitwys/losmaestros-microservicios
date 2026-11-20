package cl.losmaestros.carrito.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import cl.losmaestros.carrito.exception.AccesoDenegadoException;
import cl.losmaestros.carrito.exception.TokenInvalidoException;
import cl.losmaestros.carrito.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SeguridadInterceptor implements HandlerInterceptor {
    private static final Pattern RUTA_USUARIO = Pattern.compile("^/carritos/(?:buscar/)?usuario/([0-9]+)(?:/.*)?$");

    private final JwtService jwtService;

    public SeguridadInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String ruta = request.getRequestURI().substring(request.getContextPath().length());

        if (esPublica(request.getMethod(), ruta)) {
            return true;
        }

        String encabezado = request.getHeader("Authorization");
        if (encabezado == null || !encabezado.startsWith("Bearer ")) {
            throw new TokenInvalidoException("Falta el token de autenticación");
        }

        Claims claims = jwtService.validar(encabezado.substring(7).trim());

        Matcher coincidencia = RUTA_USUARIO.matcher(ruta);
        if (coincidencia.matches()) {
            Long idRuta = Long.valueOf(coincidencia.group(1));
            if (!idRuta.equals(claims.get("id", Long.class))) {
                throw new AccesoDenegadoException("No puedes acceder al carrito de otro usuario");
            }
        }
        return true;
    }

    private boolean esPublica(String metodo, String ruta) {
        return "OPTIONS".equalsIgnoreCase(metodo)
                || ruta.startsWith("/swagger-ui")
                || ruta.startsWith("/v3/api-docs")
                || ruta.startsWith("/error");
    }
}
