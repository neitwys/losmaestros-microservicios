package cl.losmaestros.autenticacion.service;

import cl.losmaestros.autenticacion.exception.TokenInvalidoException;
import cl.losmaestros.autenticacion.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey clave;
    private final long expiracionMinutos;

    public JwtService(@Value("${jwt.secreto}") String secreto,
                      @Value("${jwt.expiracion-minutos}") long expiracionMinutos) {
        byte[] bytes = secreto.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException(
                    "El secreto JWT debe tener al menos 32 bytes para firmar con HS256 (longitud actual: "
                            + bytes.length + " bytes)");
        }
        this.clave = Keys.hmacShaKeyFor(bytes);
        this.expiracionMinutos = expiracionMinutos;
    }

    public String generarToken(Usuario usuario) {
        Instant emitido = Instant.now();
        Instant expira = emitido.plusSeconds(expiracionMinutos * 60L);
        return Jwts.builder()
                .subject(usuario.getCorreo())
                .claim("id", usuario.getId())
                .claim("rol", usuario.getRol())
                .issuedAt(Date.from(emitido))
                .expiration(Date.from(expira))
                .signWith(clave)
                .compact();
    }

    public Claims validar(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(clave)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new TokenInvalidoException("Token inválido o expirado");
        }
    }
}
