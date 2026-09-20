package com.upc.faunascan.Repositories;

import com.upc.faunascan.Entities.AvistamientoCompartido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvistamientoCompartidoRepository extends JpaRepository<AvistamientoCompartido, Long> {

    // Compartir avistamientos entre investigadores (US48)
    List<AvistamientoCompartido> findByInvestigadorDestino_IdUsuarioOrderByFechaCompartidoDesc(Long idUsuario);

    List<AvistamientoCompartido> findByInvestigadorOrigen_IdUsuarioOrderByFechaCompartidoDesc(Long idUsuario);
}
