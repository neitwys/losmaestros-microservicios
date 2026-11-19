package cl.losmaestros.autenticacion.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import cl.losmaestros.autenticacion.exception.ConflictoException;
import cl.losmaestros.autenticacion.exception.CredencialesInvalidasException;
import cl.losmaestros.autenticacion.exception.RecursoNoEncontradoException;
import cl.losmaestros.autenticacion.exception.TokenInvalidoException;
import cl.losmaestros.autenticacion.model.LoginDTO;
import cl.losmaestros.autenticacion.model.LoginRespuestaDTO;
import cl.losmaestros.autenticacion.model.Usuario;
import cl.losmaestros.autenticacion.model.UsuarioActualizarDTO;
import cl.losmaestros.autenticacion.model.UsuarioMostrarDTO;
import cl.losmaestros.autenticacion.model.UsuarioRegistroDTO;
import cl.losmaestros.autenticacion.repository.UsuarioRepository;
import cl.losmaestros.autenticacion.validacion.RunValidador;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // REGISTRO (siempre crea un CLIENTE)
    public UsuarioMostrarDTO registrarUsuario(UsuarioRegistroDTO dto) {
        String correo = normalizarCorreo(dto.getCorreo());

        if (repository.existsByCorreo(correo)) {
            throw new ConflictoException("Ya existe un usuario registrado con el correo: " + correo);
        }

        String run = RunValidador.normalizar(dto.getRun());

        if (repository.existsByRun(run)) {
            throw new ConflictoException("Ya existe un usuario registrado con el RUN: " + run);
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre().trim());
        usuario.setRun(run);
        usuario.setApellido(limpiarApellido(dto.getApellido()));
        usuario.setCorreo(correo);
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        usuario.setTelefono(dto.getTelefono());
        usuario.setRol(Usuario.ROL_CLIENTE);
        usuario.setActivo(true);

        return convertirADTO(repository.save(usuario));
    }

    // LOGIN
    public LoginRespuestaDTO iniciarSesion(LoginDTO dto) {
        String correo = normalizarCorreo(dto.getCorreo());

        Usuario usuario = repository.findByCorreo(correo)
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña incorrectos"));

        if (!Boolean.TRUE.equals(usuario.getActivo())
                || !passwordEncoder.matches(dto.getContrasena(), usuario.getContrasena())) {
            throw new CredencialesInvalidasException("Correo o contraseña incorrectos");
        }

        LoginRespuestaDTO respuesta = new LoginRespuestaDTO();
        respuesta.setToken(jwtService.generarToken(usuario));
        respuesta.setUsuario(convertirADTO(usuario));
        return respuesta;
    }

    public UsuarioMostrarDTO obtenerPerfil(String autorizacion) {
        if (autorizacion == null || !autorizacion.startsWith("Bearer ")) {
            throw new TokenInvalidoException("Falta el token de autenticación");
        }
        Claims claims = jwtService.validar(autorizacion.substring(7));
        return obtenerUsuarioPorCorreo(claims.getSubject());
    }

    // READ
    public List<UsuarioMostrarDTO> listarUsuarios() {
        return repository.findAll().stream().map(this::convertirADTO).toList();
    }

    public UsuarioMostrarDTO obtenerUsuarioPorId(Long id) {
        return convertirADTO(buscarPorId(id));
    }

    public UsuarioMostrarDTO obtenerUsuarioPorCorreo(String correo) {
        Usuario usuario = repository.findByCorreo(normalizarCorreo(correo))
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con correo: " + correo));
        return convertirADTO(usuario);
    }

    // UPDATE
    public UsuarioMostrarDTO actualizarUsuario(Long id, UsuarioActualizarDTO dto) {
        Usuario usuario = buscarPorId(id);
        usuario.setNombre(dto.getNombre().trim());
        usuario.setApellido(limpiarApellido(dto.getApellido()));
        usuario.setTelefono(dto.getTelefono());
        return convertirADTO(repository.save(usuario));
    }

    // DELETE
    public void eliminarUsuario(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    // Auxiliares
    private Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));
    }

    private String normalizarCorreo(String correo) {
        return correo.trim().toLowerCase();
    }

    private String limpiarApellido(String apellido) {
        return apellido == null || apellido.isBlank() ? null : apellido.trim();
    }

    private UsuarioMostrarDTO convertirADTO(Usuario u) {
        UsuarioMostrarDTO dto = new UsuarioMostrarDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setRun(u.getRun());
        dto.setApellido(u.getApellido());
        dto.setCorreo(u.getCorreo());
        dto.setTelefono(u.getTelefono());
        dto.setRol(u.getRol());
        dto.setActivo(u.getActivo());
        dto.setFechaCreacion(u.getFechaCreacion());
        return dto;
    }
}