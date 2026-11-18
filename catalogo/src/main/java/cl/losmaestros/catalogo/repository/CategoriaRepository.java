package cl.losmaestros.catalogo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.losmaestros.catalogo.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}