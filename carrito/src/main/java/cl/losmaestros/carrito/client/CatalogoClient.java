package cl.losmaestros.carrito.client;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import cl.losmaestros.carrito.exception.ServicioNoDisponibleException;
import cl.losmaestros.carrito.model.ProductoExternoDTO;

@Component
public class CatalogoClient {
    private final RestClient restClient;

    public CatalogoClient(RestClient.Builder builder, @Value("${servicios.catalogo.url}") String url) {
        this.restClient = builder.baseUrl(url).build();
    }

    public Optional<ProductoExternoDTO> obtenerProducto(Long productoId) {
        try {
            return Optional.ofNullable(restClient.get()
                    .uri("/productos/buscar/{id}", productoId)
                    .retrieve()
                    .body(ProductoExternoDTO.class));
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (RestClientException e) {
            throw new ServicioNoDisponibleException("El servicio de catálogo no está disponible");
        }
    }
}