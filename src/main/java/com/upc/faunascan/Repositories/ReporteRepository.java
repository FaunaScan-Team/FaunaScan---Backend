package com.upc.faunascan.Repositories;

import com.upc.faunascan.Entities.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    // HU-49: mis reportes generados
    List<Reporte> findByUsuario_IdUsuarioOrderByFechaGeneracionDesc(Long idUsuario);

    // HU-46: abrir un reporte desde su enlace compartido
    Optional<Reporte> findByEnlaceCompartido(String enlaceCompartido);

    boolean existsByIdReporteAndUsuario_Correo(Long idReporte, String correo);

    // HU-52: reportes generados por el investigador
    long countByUsuario_IdUsuario(Long idUsuario);
}
