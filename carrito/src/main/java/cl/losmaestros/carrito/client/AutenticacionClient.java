package cl.losmaestros.carrito.client;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import cl.losmaestros.carrito.exception.ServicioNoDisponibleException;
import cl.losmaestros.carrito.model.UsuarioExternoDTO;

@Component
public class AutenticacionClient {
    private final RestClient restClient;

    public AutenticacionClient(@Qualifier("balanceado") RestClient.Builder builder, @Value("${servicios.autenticacion.url}") String url) {
        this.restClient = builder.baseUrl(url).build();
    }

    public Optional<UsuarioExternoDTO> obtenerUsuario(Long usuarioId) {
        try {
            return Optional.ofNullable(restClient.get()
                    .uri("/usuarios/buscar/{id}", usuarioId)
                    .retrieve()
                    .body(UsuarioExternoDTO.class));
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (RestClientException e) {
            throw new ServicioNoDisponibleException("El servicio de autenticación no está disponible");
        }
    }
}