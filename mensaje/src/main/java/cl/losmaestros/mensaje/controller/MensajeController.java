package cl.losmaestros.mensaje.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.losmaestros.mensaje.model.Mensaje;
import cl.losmaestros.mensaje.model.MensajeMostrarDTO;
import cl.losmaestros.mensaje.model.MensajeRegistroDTO;
import cl.losmaestros.mensaje.service.MensajeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/mensajes")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Mensajes", description = "Operaciones relacionadas con los mensajes")
public class MensajeController {
    private final MensajeService service;

    public MensajeController(MensajeService service) {
        this.service = service;
    }

    // CREATE
    @Operation(summary = "Crear un nuevo mensaje", description = "Permite crear un nuevo mensaje en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mensaje creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/crear")
    public ResponseEntity<MensajeMostrarDTO> crearMensaje(@Valid @RequestBody MensajeRegistroDTO dto) {
        Mensaje nuevoMensaje = service.guardarMensaje(dto);

        MensajeMostrarDTO mostrarDTO = new MensajeMostrarDTO();

        mostrarDTO.setId(nuevoMensaje.getId());
        mostrarDTO.setNombre(nuevoMensaje.getNombre());
        mostrarDTO.setCorreo(nuevoMensaje.getCorreo());
        mostrarDTO.setTelefono(nuevoMensaje.getTelefono());
        mostrarDTO.setMensaje(nuevoMensaje.getMensaje());
        mostrarDTO.setFechaCreacion(nuevoMensaje.getFechaCreacion());

        return ResponseEntity.status(201).body(mostrarDTO);
    }

    // READ
    @Operation(summary = "Buscar mensaje por ID", description = "Permite buscar un mensaje en el sistema utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mensaje encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Mensaje no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<MensajeMostrarDTO> buscarMensajePorId(@PathVariable Long id) {
        try {
            MensajeMostrarDTO mensajeDTO = service.obtenerMensajePorId(id);
            return ResponseEntity.ok(mensajeDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @Operation(summary = "Buscar mensaje por correo", description = "Permite buscar un mensaje en el sistema utilizando su correo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mensaje encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Mensaje no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar/correo/{correo}")
    public ResponseEntity<MensajeMostrarDTO> buscarMensajePorCorreo(@PathVariable String correo) {
        try {
            MensajeMostrarDTO mensajeDTO = service.obtenerMensajePorCorreo(correo);
            return ResponseEntity.ok(mensajeDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @Operation(summary = "Listar todos los mensajes", description = "Permite listar todos los mensajes almacenados en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mensajes listados exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<List<MensajeMostrarDTO>> listarMensajes() {
        return ResponseEntity.ok(service.listarMensajes());
    }

    // UPDATE
    @Operation(summary = "Actualizar mensaje por ID", description = "Permite actualizar un mensaje en el sistema utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mensaje actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Mensaje no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<MensajeMostrarDTO> actualizar(@PathVariable Long id, @RequestBody MensajeRegistroDTO dto) {
        try {
            Mensaje actualizado = service.actualizarMensaje(id, dto);

            MensajeMostrarDTO respuesta = new MensajeMostrarDTO();
            respuesta.setId(actualizado.getId());
            respuesta.setNombre(actualizado.getNombre());
            respuesta.setCorreo(actualizado.getCorreo());
            respuesta.setTelefono(actualizado.getTelefono());
            respuesta.setMensaje(actualizado.getMensaje());
            respuesta.setFechaCreacion(actualizado.getFechaCreacion());

            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
    }

    // DELETE
    @Operation(summary = "Eliminar mensaje por ID", description = "Permite eliminar un mensaje en el sistema utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Mensaje eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Mensaje no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarMensaje(id);
        return ResponseEntity.status(204).build();
    }
}
