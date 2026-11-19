package cl.losmaestros.carrito.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.losmaestros.carrito.client.AutenticacionClient;
import cl.losmaestros.carrito.client.CatalogoClient;
import cl.losmaestros.carrito.exception.ConflictoException;
import cl.losmaestros.carrito.exception.RecursoNoEncontradoException;
import cl.losmaestros.carrito.model.ActualizarCantidadDTO;
import cl.losmaestros.carrito.model.AgregarItemDTO;
import cl.losmaestros.carrito.model.Carrito;
import cl.losmaestros.carrito.model.CarritoItem;
import cl.losmaestros.carrito.model.CarritoItemMostrarDTO;
import cl.losmaestros.carrito.model.CarritoMostrarDTO;
import cl.losmaestros.carrito.model.ProductoExternoDTO;
import cl.losmaestros.carrito.model.UsuarioExternoDTO;
import cl.losmaestros.carrito.repository.CarritoRepository;

@Service
public class CarritoService {
    private final CarritoRepository repository;
    private final CatalogoClient catalogoClient;
    private final AutenticacionClient autenticacionClient;

    public CarritoService(CarritoRepository repository, CatalogoClient catalogoClient,
            AutenticacionClient autenticacionClient) {
        this.repository = repository;
        this.catalogoClient = catalogoClient;
        this.autenticacionClient = autenticacionClient;
    }

    // READ (crea el carrito vacío si el usuario aún no tiene uno)
    @Transactional
    public CarritoMostrarDTO obtenerCarritoPorUsuario(Long usuarioId) {
        validarUsuario(usuarioId);
        return convertirADTO(obtenerOCrearCarrito(usuarioId));
    }

    // CREATE
    @Transactional
    public CarritoMostrarDTO agregarProducto(Long usuarioId, AgregarItemDTO dto) {
        validarUsuario(usuarioId);
        Carrito carrito = obtenerOCrearCarrito(usuarioId);

        Optional<CarritoItem> existente = buscarItem(carrito, dto.getProductoId());
        int cantidadFinal = dto.getCantidad() + existente.map(CarritoItem::getCantidad).orElse(0);

        ProductoExternoDTO producto = obtenerProductoDisponible(dto.getProductoId(), cantidadFinal);

        CarritoItem item = existente.orElseGet(() -> {
            CarritoItem nuevo = new CarritoItem();
            nuevo.setCarrito(carrito);
            nuevo.setProductoId(producto.getId());
            carrito.getItems().add(nuevo);
            return nuevo;
        });
        item.setNombreProducto(producto.getNombre());
        item.setPrecioUnitario(producto.getPrecio());
        item.setCantidad(cantidadFinal);

        return guardar(carrito);
    }

    // UPDATE
    @Transactional
    public CarritoMostrarDTO actualizarCantidad(Long usuarioId, Long productoId, ActualizarCantidadDTO dto) {
        Carrito carrito = buscarCarrito(usuarioId);
        CarritoItem item = buscarItem(carrito, productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El producto " + productoId + " no está en el carrito"));

        ProductoExternoDTO producto = obtenerProductoDisponible(productoId, dto.getCantidad());

        item.setNombreProducto(producto.getNombre());
        item.setPrecioUnitario(producto.getPrecio());
        item.setCantidad(dto.getCantidad());

        return guardar(carrito);
    }

    // DELETE
    @Transactional
    public CarritoMostrarDTO eliminarProducto(Long usuarioId, Long productoId) {
        Carrito carrito = buscarCarrito(usuarioId);
        CarritoItem item = buscarItem(carrito, productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El producto " + productoId + " no está en el carrito"));

        carrito.getItems().remove(item);
        return guardar(carrito);
    }

    @Transactional
    public void vaciarCarrito(Long usuarioId) {
        Carrito carrito = buscarCarrito(usuarioId);
        carrito.getItems().clear();
        guardar(carrito);
    }

    // Auxiliares
    private void validarUsuario(Long usuarioId) {
        UsuarioExternoDTO usuario = autenticacionClient.obtenerUsuario(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + usuarioId));
        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new ConflictoException("El usuario " + usuarioId + " está inactivo");
        }
    }

    private ProductoExternoDTO obtenerProductoDisponible(Long productoId, int cantidadSolicitada) {
        ProductoExternoDTO producto = catalogoClient.obtenerProducto(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));

        if (!Boolean.TRUE.equals(producto.getActivo())) {
            throw new ConflictoException("El producto '" + producto.getNombre() + "' no está disponible");
        }
        if (producto.getStock() < cantidadSolicitada) {
            throw new ConflictoException("Stock insuficiente para '" + producto.getNombre()
                    + "'. Disponible: " + producto.getStock());
        }
        return producto;
    }

    private Carrito obtenerOCrearCarrito(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId).orElseGet(() -> {
            Carrito nuevo = new Carrito();
            nuevo.setUsuarioId(usuarioId);
            return repository.save(nuevo);
        });
    }

    private Carrito buscarCarrito(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El usuario " + usuarioId + " no tiene un carrito"));
    }

    private Optional<CarritoItem> buscarItem(Carrito carrito, Long productoId) {
        return carrito.getItems().stream()
                .filter(i -> i.getProductoId().equals(productoId))
                .findFirst();
    }

    private CarritoMostrarDTO guardar(Carrito carrito) {
        carrito.setFechaActualizacion(LocalDateTime.now());
        return convertirADTO(repository.save(carrito));
    }

    private CarritoMostrarDTO convertirADTO(Carrito carrito) {
        List<CarritoItemMostrarDTO> items = carrito.getItems().stream().map(i -> {
            CarritoItemMostrarDTO itemDTO = new CarritoItemMostrarDTO();
            itemDTO.setProductoId(i.getProductoId());
            itemDTO.setNombreProducto(i.getNombreProducto());
            itemDTO.setPrecioUnitario(i.getPrecioUnitario());
            itemDTO.setCantidad(i.getCantidad());
            itemDTO.setSubtotal(i.getPrecioUnitario().multiply(BigDecimal.valueOf(i.getCantidad())));
            return itemDTO;
        }).toList();

        CarritoMostrarDTO dto = new CarritoMostrarDTO();
        dto.setId(carrito.getId());
        dto.setUsuarioId(carrito.getUsuarioId());
        dto.setItems(items);
        dto.setCantidadProductos(items.stream().mapToInt(CarritoItemMostrarDTO::getCantidad).sum());
        dto.setTotal(items.stream().map(CarritoItemMostrarDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        dto.setFechaActualizacion(carrito.getFechaActualizacion());
        return dto;
    }
}