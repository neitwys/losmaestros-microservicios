package cl.losmaestros.mensaje.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import cl.losmaestros.mensaje.model.Mensaje;
import cl.losmaestros.mensaje.model.MensajeMostrarDTO;
import cl.losmaestros.mensaje.model.MensajeRegistroDTO;
import cl.losmaestros.mensaje.repository.MensajeRepository;

@Service
public class MensajeService {
    private final MensajeRepository repository;

    public MensajeService(MensajeRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public Mensaje guardarMensaje(MensajeRegistroDTO mensajeRegistroDTO) {
        Mensaje mensaje = new Mensaje();
        mensaje.setNombre(mensajeRegistroDTO.getNombre());
        mensaje.setCorreo(mensajeRegistroDTO.getCorreo());
        mensaje.setTelefono(mensajeRegistroDTO.getTelefono());
        mensaje.setMensaje(mensajeRegistroDTO.getMensaje());
        return repository.save(mensaje);
    }

    // READ
    public List<MensajeMostrarDTO> listarMensajes() {
        List<Mensaje> mensajes = repository.findAll();
        List<MensajeMostrarDTO> listaDTO = new ArrayList<>();

        for (Mensaje m : mensajes) {
            MensajeMostrarDTO dto = new MensajeMostrarDTO();
            dto.setId(m.getId());
            dto.setNombre(m.getNombre());
            dto.setCorreo(m.getCorreo());
            dto.setTelefono(m.getTelefono());
            dto.setMensaje(m.getMensaje());
            dto.setFechaCreacion(m.getFechaCreacion());
            listaDTO.add(dto);
        }
        return listaDTO;
    }

    public MensajeMostrarDTO obtenerMensajePorId(Long id) {
        Mensaje m = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado con ID: " + id));

        MensajeMostrarDTO dto = new MensajeMostrarDTO();
        dto.setId(m.getId());
        dto.setNombre(m.getNombre());
        dto.setCorreo(m.getCorreo());
        dto.setTelefono(m.getTelefono());
        dto.setMensaje(m.getMensaje());
        dto.setFechaCreacion(m.getFechaCreacion());

        return dto;
    }

    public MensajeMostrarDTO obtenerMensajePorCorreo(String correo) {
        Mensaje m = repository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado con correo: " + correo));

        MensajeMostrarDTO dto = new MensajeMostrarDTO();
        dto.setId(m.getId());
        dto.setNombre(m.getNombre());
        dto.setCorreo(m.getCorreo());
        dto.setTelefono(m.getTelefono());
        dto.setMensaje(m.getMensaje());
        dto.setFechaCreacion(m.getFechaCreacion());

        return dto;
    }

    // UPDATE
    public Mensaje actualizarMensaje(Long id, MensajeRegistroDTO mensajeRegistroDTO) {
        Mensaje mensajeExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado con ID: " + id));

        mensajeExistente.setNombre(mensajeRegistroDTO.getNombre());
        mensajeExistente.setCorreo(mensajeRegistroDTO.getCorreo());
        mensajeExistente.setTelefono(mensajeRegistroDTO.getTelefono());
        mensajeExistente.setMensaje(mensajeRegistroDTO.getMensaje());

        return repository.save(mensajeExistente);
    }

    // DELETE
    public void eliminarMensaje(Long id) {
        repository.deleteById(id);
    }

}
