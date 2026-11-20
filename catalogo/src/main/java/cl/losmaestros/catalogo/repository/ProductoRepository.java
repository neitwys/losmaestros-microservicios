package cl.losmaestros.catalogo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.losmaestros.catalogo.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoriaId(Long categoriaId);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Long id);
}