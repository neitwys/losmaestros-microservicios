package cl.losmaestros.autenticacion.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.losmaestros.autenticacion.model.UsuarioActualizarDTO;
import cl.losmaestros.autenticacion.model.UsuarioMostrarDTO;
import cl.losmaestros.autenticacion.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // READ
    @Operation(summary = "Buscar usuario por ID", description = "Permite buscar un usuario utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<UsuarioMostrarDTO> buscarUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerUsuarioPorId(id));
    }

    @Operation(summary = "Buscar usuario por correo", description = "Permite buscar un usuario utilizando su correo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/correo/{correo}")
    public ResponseEntity<UsuarioMostrarDTO> buscarUsuarioPorCorreo(@PathVariable String correo) {
        return ResponseEntity.ok(service.obtenerUsuarioPorCorreo(correo));
    }

    @Operation(summary = "Listar todos los usuarios", description = "Permite listar todos los usuarios del sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios listados exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioMostrarDTO>> listarUsuarios() {
        return ResponseEntity.ok(service.listarUsuarios());
    }

    // UPDATE
    @Operation(summary = "Actualizar usuario por ID", description = "Permite actualizar nombre, apellido y teléfono de un usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<UsuarioMostrarDTO> actualizarUsuario(@PathVariable Long id,
            @Valid @RequestBody UsuarioActualizarDTO dto) {
        return ResponseEntity.ok(service.actualizarUsuario(id, dto));
    }

    // DELETE
    @Operation(summary = "Eliminar usuario por ID", description = "Permite eliminar un usuario utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        service.eliminarUsuario(id);
        return ResponseEntity.status(204).build();
    }
}