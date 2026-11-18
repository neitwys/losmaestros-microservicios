package cl.losmaestros.catalogo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.losmaestros.catalogo.model.ProductoMostrarDTO;
import cl.losmaestros.catalogo.model.ProductoRegistroDTO;
import cl.losmaestros.catalogo.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Productos", description = "Operaciones relacionadas con los productos del catálogo")
public class ProductoController {
    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // CREATE
    @Operation(summary = "Crear un nuevo producto", description = "Permite crear un nuevo producto en el catálogo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/crear")
    public ResponseEntity<ProductoMostrarDTO> crearProducto(@Valid @RequestBody ProductoRegistroDTO dto) {
        return ResponseEntity.status(201).body(service.guardarProducto(dto));
    }

    // READ
    @Operation(summary = "Buscar producto por ID", description = "Permite buscar un producto utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<ProductoMostrarDTO> buscarProductoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerProductoPorId(id));
    }

    @Operation(summary = "Buscar productos por nombre", description = "Permite buscar productos cuyo nombre contenga el texto indicado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<ProductoMostrarDTO>> buscarProductosPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(service.buscarProductosPorNombre(nombre));
    }

    @Operation(summary = "Listar todos los productos", description = "Permite listar todos los productos del catálogo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos listados exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<ProductoMostrarDTO>> listarProductos() {
        return ResponseEntity.ok(service.listarProductos());
    }

    @Operation(summary = "Listar productos por categoría", description = "Permite listar los productos de una categoría específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Productos listados exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoMostrarDTO>> listarProductosPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(service.listarProductosPorCategoria(categoriaId));
    }

    // UPDATE
    @Operation(summary = "Actualizar producto por ID", description = "Permite actualizar un producto utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Producto o categoría no encontrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ProductoMostrarDTO> actualizarProducto(@PathVariable Long id,
            @Valid @RequestBody ProductoRegistroDTO dto) {
        return ResponseEntity.ok(service.actualizarProducto(id, dto));
    }

    // DELETE
    @Operation(summary = "Eliminar producto por ID", description = "Permite eliminar un producto utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        service.eliminarProducto(id);
        return ResponseEntity.status(204).build();
    }
}