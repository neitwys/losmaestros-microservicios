package cl.losmaestros.autenticacion.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.losmaestros.autenticacion.model.LoginDTO;
import cl.losmaestros.autenticacion.model.LoginRespuestaDTO;
import cl.losmaestros.autenticacion.model.UsuarioMostrarDTO;
import cl.losmaestros.autenticacion.model.UsuarioRegistroDTO;
import cl.losmaestros.autenticacion.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Autenticación", description = "Registro e inicio de sesión de usuarios")
public class AuthController {
    private final UsuarioService service;

    public AuthController(UsuarioService service) {
        this.service = service;
    }

    @Operation(summary = "Registrar un nuevo usuario", description = "Crea una cuenta de tipo CLIENTE.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "409", description = "El correo ya está registrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/registro")
    public ResponseEntity<UsuarioMostrarDTO> registrar(@Valid @RequestBody UsuarioRegistroDTO dto) {
        return ResponseEntity.status(201).body(service.registrarUsuario(dto));
    }

    @Operation(summary = "Iniciar sesión", description = "Valida las credenciales y devuelve los datos del usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginRespuestaDTO> login(@Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(service.iniciarSesion(dto));
    }}