package com.upc.faunascan.Repositories;

import com.upc.faunascan.Entities.AvistamientoCompartido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvistamientoCompartidoRepository extends JpaRepository<AvistamientoCompartido, Long> {

    // HU-56: bandeja de recibidos
    @Query("select ac from AvistamientoCompartido ac " +
           "join fetch ac.avistamiento a join fetch ac.investigadorOrigen o " +
           "where ac.investigadorDestino.idUsuario = :idUsuario " +
           "order by ac.fechaCompartido desc")
    List<AvistamientoCompartido> findByInvestigadorDestino_IdUsuarioOrderByFechaCompartidoDesc(@Param("idUsuario") Long idUsuario);

    // HU-56: enviados
    @Query("select ac from AvistamientoCompartido ac " +
           "join fetch ac.avistamiento a join fetch ac.investigadorDestino d " +
           "where ac.investigadorOrigen.idUsuario = :idUsuario " +
           "order by ac.fechaCompartido desc")
    List<AvistamientoCompartido> findByInvestigadorOrigen_IdUsuarioOrderByFechaCompartidoDesc(@Param("idUsuario") Long idUsuario);

    boolean existsByIdCompartidoAndInvestigadorOrigen_Correo(Long idCompartido, String correo);
}
