package cl.losmaestros.mensaje.service;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cl.losmaestros.mensaje.exception.TokenInvalidoException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final SecretKey clave;

    public JwtService(@Value("${jwt.secreto}") String secreto) {
        byte[] bytes = secreto.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException("El secreto JWT debe tener al menos 32 bytes");
        }
        this.clave = Keys.hmacShaKeyFor(bytes);
    }

    public Claims validar(String token) {
        try {
            return Jwts.parser().verifyWith(clave).build().parseSignedClaims(token).getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new TokenInvalidoException("Token inválido o expirado");
        }
    }
}
