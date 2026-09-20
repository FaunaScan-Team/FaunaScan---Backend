package com.upc.faunascan.Repositories;

import com.upc.faunascan.Entities.Especie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecieRepository extends JpaRepository<Especie, Long> {

    // US46: buscador por nombre comun o cientifico
    @Query("select e from Especie e where " +
           "lower(e.nombreComun) like lower(concat('%', :texto, '%')) or " +
           "lower(e.nombreCientifico) like lower(concat('%', :texto, '%'))")
    List<Especie> buscar(@Param("texto") String texto);

    List<Especie> findByFamilia_IdFamilia(Long idFamilia);

    List<Especie> findByCategoriaConservacion_IdCategoria(Long idCategoria);
}
