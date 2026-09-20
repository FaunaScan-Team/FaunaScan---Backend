package com.upc.faunascan.Services;

import com.upc.faunascan.Entities.Especie;
import com.upc.faunascan.Repositories.EspecieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspecieService {

    private final EspecieRepository especieRepository;

    public List<Especie> listar() {
        return especieRepository.findAll();
    }

    public Especie obtenerPorId(Long id) {
        return especieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especie no encontrada con id: " + id));
    }

    // US46: buscar especies por nombre comun o cientifico
    public List<Especie> buscar(String texto) {
        return especieRepository.buscar(texto);
    }

    public List<Especie> listarPorFamilia(Long idFamilia) {
        return especieRepository.findByFamilia_IdFamilia(idFamilia);
    }

    public List<Especie> listarPorCategoria(Long idCategoria) {
        return especieRepository.findByCategoriaConservacion_IdCategoria(idCategoria);
    }

    public Especie crear(Especie especie) {
        return especieRepository.save(especie);
    }

    public Especie actualizar(Long id, Especie datos) {
        Especie especie = obtenerPorId(id);
        especie.setNombreComun(datos.getNombreComun());
        especie.setNombreCientifico(datos.getNombreCientifico());
        especie.setImagenReferencia(datos.getImagenReferencia());
        especie.setPistasIdentificacion(datos.getPistasIdentificacion());
        especie.setFamilia(datos.getFamilia());
        especie.setCategoriaConservacion(datos.getCategoriaConservacion());
        return especieRepository.save(especie);
    }

    public void eliminar(Long id) {
        especieRepository.delete(obtenerPorId(id));
    }
}
