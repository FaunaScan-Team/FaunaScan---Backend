package com.upc.faunascan.Repositories;

import com.upc.faunascan.Entities.Avistamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvistamientoRepository extends JpaRepository<Avistamiento, Long> {

    // Historial / listado del usuario (US14) y filtros (US17, US25)
    List<Avistamiento> findByUsuario_IdUsuarioOrderByFechaAvistamientoDesc(Long idUsuario);

    List<Avistamiento> findByEspecie_IdEspecie(Long idEspecie);

    List<Avistamiento> findByUbicacion_IdUbicacion(Long idUbicacion);

    // Cola de avistamientos pendientes de validacion por un investigador (US45)
    List<Avistamiento> findByEstadoValidacion(String estadoValidacion);
}
