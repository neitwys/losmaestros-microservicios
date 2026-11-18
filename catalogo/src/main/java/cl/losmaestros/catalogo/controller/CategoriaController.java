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

import cl.losmaestros.catalogo.model.CategoriaMostrarDTO;
import cl.losmaestros.catalogo.model.CategoriaRegistroDTO;
import cl.losmaestros.catalogo.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Categorías", description = "Operaciones relacionadas con las categorías de productos")
public class CategoriaController {
    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    // CREATE
    @Operation(summary = "Crear una nueva categoría", description = "Permite crear una nueva categoría en el catálogo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "409", description = "Ya existe una categoría con ese nombre"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/crear")
    public ResponseEntity<CategoriaMostrarDTO> crearCategoria(@Valid @RequestBody CategoriaRegistroDTO dto) {
        return ResponseEntity.status(201).body(service.guardarCategoria(dto));
    }

    // READ
    @Operation(summary = "Buscar categoría por ID", description = "Permite buscar una categoría utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<CategoriaMostrarDTO> buscarCategoriaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerCategoriaPorId(id));
    }

    @Operation(summary = "Listar todas las categorías", description = "Permite listar todas las categorías del catálogo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorías listadas exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<CategoriaMostrarDTO>> listarCategorias() {
        return ResponseEntity.ok(service.listarCategorias());
    }

    // UPDATE
    @Operation(summary = "Actualizar categoría por ID", description = "Permite actualizar una categoría utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe una categoría con ese nombre"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<CategoriaMostrarDTO> actualizarCategoria(@PathVariable Long id,
            @Valid @RequestBody CategoriaRegistroDTO dto) {
        return ResponseEntity.ok(service.actualizarCategoria(id, dto));
    }

    // DELETE
    @Operation(summary = "Eliminar categoría por ID", description = "Permite eliminar una categoría utilizando su ID. No se puede eliminar si tiene productos asociados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "409", description = "La categoría tiene productos asociados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        service.eliminarCategoria(id);
        return ResponseEntity.status(204).build();
    }
}