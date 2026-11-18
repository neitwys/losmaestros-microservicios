package cl.losmaestros.catalogo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.losmaestros.catalogo.exception.RecursoNoEncontradoException;
import cl.losmaestros.catalogo.model.Categoria;
import cl.losmaestros.catalogo.model.Producto;
import cl.losmaestros.catalogo.model.ProductoMostrarDTO;
import cl.losmaestros.catalogo.model.ProductoRegistroDTO;
import cl.losmaestros.catalogo.repository.CategoriaRepository;
import cl.losmaestros.catalogo.repository.ProductoRepository;

@Service
public class ProductoService {
    private final ProductoRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository repository, CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    // CREATE
    @Transactional
    public ProductoMostrarDTO guardarProducto(ProductoRegistroDTO dto) {
        Producto producto = new Producto();
        copiarDatos(producto, dto);
        return convertirADTO(repository.save(producto));
    }

    // READ
    @Transactional(readOnly = true)
    public List<ProductoMostrarDTO> listarProductos() {
        return repository.findAll().stream().map(this::convertirADTO).toList();
    }

    @Transactional(readOnly = true)
    public ProductoMostrarDTO obtenerProductoPorId(Long id) {
        return convertirADTO(buscarPorId(id));
    }

    @Transactional(readOnly = true)
    public List<ProductoMostrarDTO> listarProductosPorCategoria(Long categoriaId) {
        return repository.findByCategoriaId(categoriaId).stream().map(this::convertirADTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoMostrarDTO> buscarProductosPorNombre(String nombre) {
        return repository.findByNombreContainingIgnoreCase(nombre).stream().map(this::convertirADTO).toList();
    }

    // UPDATE
    @Transactional
    public ProductoMostrarDTO actualizarProducto(Long id, ProductoRegistroDTO dto) {
        Producto producto = buscarPorId(id);
        copiarDatos(producto, dto);
        return convertirADTO(repository.save(producto));
    }

    // DELETE
    public void eliminarProducto(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Producto no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    // Auxiliares
    private Producto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));
    }

    private void copiarDatos(Producto producto, ProductoRegistroDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Categoría no encontrada con ID: " + dto.getCategoriaId()));

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        producto.setCategoria(categoria);
    }

    private ProductoMostrarDTO convertirADTO(Producto p) {
        ProductoMostrarDTO dto = new ProductoMostrarDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setPrecio(p.getPrecio());
        dto.setStock(p.getStock());
        dto.setImagenUrl(p.getImagenUrl());
        dto.setActivo(p.getActivo());
        dto.setFechaCreacion(p.getFechaCreacion());
        dto.setCategoriaId(p.getCategoria().getId());
        dto.setCategoriaNombre(p.getCategoria().getNombre());
        return dto;
    }
}