package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Familia;
import com.upc.faunascan.Repositories.FamiliaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FamiliaService {

    private final FamiliaRepository familiaRepository;

    public List<Familia> listar() {
        return familiaRepository.findAll();
    }

    public Familia obtenerPorId(Long id) {
        return familiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Familia no encontrada con id: " + id));
    }

    public Familia crear(Familia familia) {
        return familiaRepository.save(familia);
    }

    public Familia actualizar(Long id, Familia datos) {
        Familia familia = obtenerPorId(id);
        familia.setNombre(datos.getNombre());
        familia.setDescripcion(datos.getDescripcion());
        return familiaRepository.save(familia);
    }

    public void eliminar(Long id) {
        familiaRepository.delete(obtenerPorId(id));
    }
}
