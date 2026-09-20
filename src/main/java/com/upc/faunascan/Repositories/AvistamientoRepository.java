package com.upc.faunascan.Repositories;

import com.upc.faunascan.Entities.Avistamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvistamientoRepository extends JpaRepository<Avistamiento, Long> {

    // US14: historial del usuario, con especie y ubicacion ya cargadas
    @Query("select a from Avistamiento a " +
           "join fetch a.especie e join fetch a.ubicacion u " +
           "where a.usuario.idUsuario = :idUsuario " +
           "order by a.fechaAvistamiento desc")
    List<Avistamiento> findByUsuario_IdUsuarioOrderByFechaAvistamientoDesc(@Param("idUsuario") Long idUsuario);

    List<Avistamiento> findByEspecie_IdEspecie(Long idEspecie);

    List<Avistamiento> findByUbicacion_IdUbicacion(Long idUbicacion);

    // US45: cola de revision del investigador
    @Query("select a from Avistamiento a " +
           "join fetch a.usuario us join fetch a.especie e " +
           "where a.estadoValidacion = :estado order by a.fechaRegistro asc")
    List<Avistamiento> findByEstadoValidacion(@Param("estado") String estadoValidacion);
}
