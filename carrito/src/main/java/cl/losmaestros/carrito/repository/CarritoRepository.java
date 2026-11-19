package cl.losmaestros.carrito.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.losmaestros.carrito.model.Carrito;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuarioId(Long usuarioId);
}