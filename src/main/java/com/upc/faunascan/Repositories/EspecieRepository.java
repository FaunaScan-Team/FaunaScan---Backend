package com.upc.faunascan.Repositories;

import com.upc.faunascan.Entities.Especie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecieRepository extends JpaRepository<Especie, Long> {

    // Buscador de especies por nombre comun o cientifico (US46)
    List<Especie> findByNombreComunContainingIgnoreCaseOrNombreCientificoContainingIgnoreCase(
            String nombreComun, String nombreCientifico);

    List<Especie> findByFamilia_IdFamilia(Long idFamilia);

    List<Especie> findByCategoriaConservacion_IdCategoria(Long idCategoria);
}
