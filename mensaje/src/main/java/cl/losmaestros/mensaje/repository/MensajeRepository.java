package cl.losmaestros.mensaje.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.losmaestros.mensaje.model.Mensaje;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    Optional<Mensaje> findByCorreo(String correo);
}
