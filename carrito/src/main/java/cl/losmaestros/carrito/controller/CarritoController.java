package cl.losmaestros.carrito.controller;

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

import cl.losmaestros.carrito.model.ActualizarCantidadDTO;
import cl.losmaestros.carrito.model.AgregarItemDTO;
import cl.losmaestros.carrito.model.CarritoMostrarDTO;
import cl.losmaestros.carrito.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/carritos")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Carritos", description = "Operaciones relacionadas con el carrito de compras")
public class CarritoController {
    private final CarritoService service;

    public CarritoController(CarritoService service) {
        this.service = service;
    }

    // READ
    @Operation(summary = "Obtener el carrito de un usuario", description = "Devuelve el carrito del usuario con sus productos y total. Si no existe, se crea vacío.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "503", description = "Servicio externo no disponible")
    })
    @GetMapping("/buscar/usuario/{usuarioId}")
    public ResponseEntity<CarritoMostrarDTO> buscarCarritoPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.obtenerCarritoPorUsuario(usuarioId));
    }

    // CREATE
    @Operation(summary = "Agregar un producto al carrito", description = "Agrega un producto. Si ya estaba, suma la cantidad. Valida existencia y stock en el catálogo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto agregado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Usuario o producto no encontrado"),
            @ApiResponse(responseCode = "409", description = "Producto no disponible o sin stock suficiente"),
            @ApiResponse(responseCode = "503", description = "Servicio externo no disponible")
    })
    @PostMapping("/usuario/{usuarioId}/agregar")
    public ResponseEntity<CarritoMostrarDTO> agregarProducto(@PathVariable Long usuarioId,
            @Valid @RequestBody AgregarItemDTO dto) {
        return ResponseEntity.ok(service.agregarProducto(usuarioId, dto));
    }

    // UPDATE
    @Operation(summary = "Actualizar la cantidad de un producto", description = "Cambia la cantidad de un producto que ya está en el carrito.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cantidad actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Carrito o producto no encontrado"),
            @ApiResponse(responseCode = "409", description = "Producto no disponible o sin stock suficiente"),
            @ApiResponse(responseCode = "503", description = "Servicio externo no disponible")
    })
    @PutMapping("/usuario/{usuarioId}/actualizar/{productoId}")
    public ResponseEntity<CarritoMostrarDTO> actualizarCantidad(@PathVariable Long usuarioId,
            @PathVariable Long productoId, @Valid @RequestBody ActualizarCantidadDTO dto) {
        return ResponseEntity.ok(service.actualizarCantidad(usuarioId, productoId, dto));
    }

    // DELETE
    @Operation(summary = "Eliminar un producto del carrito", description = "Quita un producto del carrito del usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Carrito o producto no encontrado")
    })
    @DeleteMapping("/usuario/{usuarioId}/eliminar/{productoId}")
    public ResponseEntity<CarritoMostrarDTO> eliminarProducto(@PathVariable Long usuarioId,
            @PathVariable Long productoId) {
        return ResponseEntity.ok(service.eliminarProducto(usuarioId, productoId));
    }

    @Operation(summary = "Vaciar el carrito", description = "Elimina todos los productos del carrito del usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Carrito vaciado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @DeleteMapping("/usuario/{usuarioId}/vaciar")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        service.vaciarCarrito(usuarioId);
        return ResponseEntity.status(204).build();
    }
}