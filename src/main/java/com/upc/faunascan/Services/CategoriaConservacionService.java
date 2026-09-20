package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.CategoriaConservacion;
import com.upc.faunascan.Repositories.CategoriaConservacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaConservacionService {

    private final CategoriaConservacionRepository categoriaConservacionRepository;

    public List<CategoriaConservacion> listar() {
        return categoriaConservacionRepository.findAll();
    }

    public CategoriaConservacion obtenerPorId(Long id) {
        return categoriaConservacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria de conservacion no encontrada con id: " + id));
    }

    public CategoriaConservacion crear(CategoriaConservacion categoria) {
        return categoriaConservacionRepository.save(categoria);
    }

    public CategoriaConservacion actualizar(Long id, CategoriaConservacion datos) {
        CategoriaConservacion categoria = obtenerPorId(id);
        categoria.setNombre(datos.getNombre());
        categoria.setDescripcion(datos.getDescripcion());
        return categoriaConservacionRepository.save(categoria);
    }

    public void eliminar(Long id) {
        categoriaConservacionRepository.delete(obtenerPorId(id));
    }
}
