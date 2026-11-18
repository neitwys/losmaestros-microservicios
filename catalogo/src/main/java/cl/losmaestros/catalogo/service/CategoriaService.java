package cl.losmaestros.catalogo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.losmaestros.catalogo.exception.ConflictoException;
import cl.losmaestros.catalogo.exception.RecursoNoEncontradoException;
import cl.losmaestros.catalogo.model.Categoria;
import cl.losmaestros.catalogo.model.CategoriaMostrarDTO;
import cl.losmaestros.catalogo.model.CategoriaRegistroDTO;
import cl.losmaestros.catalogo.repository.CategoriaRepository;

@Service
public class CategoriaService {
    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public CategoriaMostrarDTO guardarCategoria(CategoriaRegistroDTO dto) {
        if (repository.existsByNombreIgnoreCase(dto.getNombre())) {
            throw new ConflictoException("Ya existe una categoría con el nombre: " + dto.getNombre());
        }
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return convertirADTO(repository.save(categoria));
    }

    // READ
    public List<CategoriaMostrarDTO> listarCategorias() {
        return repository.findAll().stream().map(this::convertirADTO).toList();
    }

    public CategoriaMostrarDTO obtenerCategoriaPorId(Long id) {
        return convertirADTO(buscarPorId(id));
    }

    // UPDATE
    public CategoriaMostrarDTO actualizarCategoria(Long id, CategoriaRegistroDTO dto) {
        Categoria categoria = buscarPorId(id);

        if (repository.existsByNombreIgnoreCaseAndIdNot(dto.getNombre(), id)) {
            throw new ConflictoException("Ya existe una categoría con el nombre: " + dto.getNombre());
        }

        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return convertirADTO(repository.save(categoria));
    }

    // DELETE
    public void eliminarCategoria(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Categoría no encontrada con ID: " + id);
        }
        repository.deleteById(id);
    }

    // Auxiliares
    private Categoria buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con ID: " + id));
    }

    private CategoriaMostrarDTO convertirADTO(Categoria c) {
        CategoriaMostrarDTO dto = new CategoriaMostrarDTO();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setDescripcion(c.getDescripcion());
        return dto;
    }
}